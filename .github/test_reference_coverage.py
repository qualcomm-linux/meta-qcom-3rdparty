# Copyright (c) 2026 DevDocs
# SPDX-License-Identifier: BSD-3-Clause
"""Find every function definition and check its native reference.

Usage: test_reference_coverage.py generate | check

docs/source/conf.py runs "generate" before Sphinx reads its sources: it fails
on source formats without a configured extractor, on functions without a
configured renderer, and on undocumented functions, then writes one reference
page per source file to docs/source/contributing/.generated/. The Makefile
runs "check" after the HTML build: it fails when a function has no rendered
reference entry.

Functions are found in tracked and staged files with parsers, independently of
the renderers; nothing is executed. Python's ast module reads Python files,
and tree-sitter-bash reads shell scripts, workflow run steps, and Makefile
recipes. BitBake's own statement parser, from the pinned BitBake that the
Makefile's setup fetches, splits recipes, appends, classes, and include files
into statements without evaluating them; their shell tasks then go to
tree-sitter-bash and shdoc. BitBake Python functions, functions nested in a
task, and anything other than configuration statements in a .conf file or a
kas local.conf fragment have no configured renderer and stop the build.
Adapting a repository means adding its source formats to discover() and a
renderer for each language that defines functions to RENDERERS.
"""
import ast
import html as htmllib
import re
import subprocess
import sys
from pathlib import Path

import tree_sitter_bash
import yaml
from docutils.nodes import make_id
from tree_sitter import Language, Parser

ROOT = Path(__file__).resolve().parents[1]
# BitBake's library, installed by the Makefile's setup at its pinned revision.
sys.path.insert(0, str(ROOT / ".venv/bitbake/lib"))
from bb.parse import ast as bitbake_ast  # noqa: E402
from bb.parse.parse_py import BBHandler, ConfHandler  # noqa: E402

PAGES = ROOT / "docs/source/contributing/.generated"
SITE = ROOT / "docs/site/docs/source/contributing/.generated"
SHELL = Parser(Language(tree_sitter_bash.language()))
# Formats verified to hold no function definitions: kernel configuration
# fragments (.cfg) hold only CONFIG_ lines, and .markdownlint.yaml only rules.
PLAIN_SUFFIXES = (".md", ".txt", ".lock", ".cfg")
PLAIN_NAMES = ("LICENSE", "NOTICE", "CODEOWNERS", ".gitignore", ".markdownlint.yaml")
# BitBake metadata that BBHandler reads; .conf files go to ConfHandler.
BITBAKE_SUFFIXES = (".bb", ".bbappend", ".bbclass", ".inc")


def page_name(path):
    """Return the generated page name for a repository-relative source path.

    Args:
        path (str): Source path, such as ``ci/build.sh``.

    Returns:
        str: The page name without a suffix, such as ``ci-build.sh``.

    Example:
        ``page_name("ci/build.sh")`` returns ``"ci-build.sh"``.
    """
    return path.replace("/", "-")


def comment_block(lines, start):
    """Return the comment lines directly above a definition.

    Args:
        lines (list[str]): The file's lines.
        start (int): The zero-based index of the definition's first line.

    Returns:
        str: The comment lines, stripped, joined by newlines.

    Example:
        ``comment_block(["# @description Run.", "f() {"], 1)`` returns ``"# @description Run."``.
    """
    comments = []
    while start - len(comments) > 0 and lines[start - len(comments) - 1].lstrip().startswith("#"):
        comments.insert(0, lines[start - len(comments) - 1].strip())
    return "\n".join(comments)


def shell_functions(path, where, text):
    """List the shell functions defined in a shell text.

    Args:
        path (str): Source path, used in reports.
        where (str): Location inside the file, such as a workflow step, or "".
        text (str): Shell code.

    Returns:
        list[dict]: One entry per function with its name, line, and comment block.

    Raises:
        ValueError: The shell parser cannot read the text.

    Example:
        ``shell_functions("ci/build.sh", "", "f() { :; }")`` finds ``f``.
    """
    tree = SHELL.parse(text.encode())
    if tree.root_node.has_error:
        raise ValueError(f"{path}{where}: the shell parser cannot read it")
    found, stack = [], [tree.root_node]
    while stack:
        node = stack.pop()
        if node.type == "function_definition":
            found.append((node.start_point[0], node.child_by_field_name("name").text.decode()))
        stack.extend(node.children)
    lines = text.splitlines()
    return [{"language": "shell", "path": path, "where": where, "name": name, "line": start + 1,
             "doc": comment_block(lines, start)} for start, name in sorted(found)]


def python_functions(path, text):
    """List the functions and methods defined in a Python file.

    Args:
        path (str): Source path, used in reports.
        text (str): Python source.

    Returns:
        list[dict]: One entry per function with its qualified name, line, and
        docstring. Functions nested in other functions are included.

    Example:
        ``python_functions("tool.py", "def f():\\n    pass\\n")`` finds ``f``.
    """
    functions, stack = [], [(ast.parse(text, path), "")]
    while stack:
        node, prefix = stack.pop()
        for child in ast.iter_child_nodes(node):
            if isinstance(child, (ast.FunctionDef, ast.AsyncFunctionDef)):
                functions.append({"language": "python", "path": path, "where": "", "name": prefix + child.name,
                                  "line": child.lineno, "doc": ast.get_docstring(child) or ""})
            name = prefix + child.name + "." if isinstance(child, (ast.FunctionDef, ast.AsyncFunctionDef,
                                                                     ast.ClassDef)) else prefix
            stack.append((child, name))
    return sorted(functions, key=lambda function: function["line"])


def bitbake_functions(path, text):
    """List the shell tasks and functions in a BitBake recipe, append, class, or include file.

    BitBake's statement parser splits the file without evaluating it. Each shell
    function's body then goes to the shell parser, so a function nested inside a
    task is found too.

    Args:
        path (str): Source path, relative to the repository root.
        text (str): The file's text.

    Returns:
        tuple[list[dict], list[str]]: The shell functions found, with their
        documentation comments, and the definitions no renderer supports.

    Raises:
        ValueError: BitBake or the shell parser cannot read the file.

    Example:
        ``bitbake_functions("recipes-bsp/x/x.bb", "do_deploy() {\\n    :\\n}\\n")`` finds ``do_deploy``.
    """
    # get_statements keeps its parsing state in module globals; start each file clean.
    for name, value in (("__infunc__", []), ("__body__", []), ("__residue__", []),
                        ("__inpython__", False), ("__classname__", "")):
        setattr(BBHandler, name, value)
    try:
        statements = BBHandler.get_statements(path, str(ROOT / path), Path(path).name)
    except Exception as error:  # BitBake raises ParseError or, through bb.fatal, BBHandledException.
        raise ValueError(f"BitBake cannot parse it: {error}") from error
    lines, functions, problems = text.splitlines(), [], []
    for node in statements:
        if isinstance(node, bitbake_ast.PythonMethodNode) or (
                isinstance(node, bitbake_ast.MethodNode) and node.python):
            name = node.function if isinstance(node, bitbake_ast.PythonMethodNode) else node.func_name
            problems.append(f"{path}:{node.lineno}: BitBake Python function {name} is unsupported: "
                            "no BitBake Python renderer is configured in .github/test_reference_coverage.py")
        elif isinstance(node, bitbake_ast.MethodNode):
            start = node.lineno - len(node.body)
            # BitBake expands ${@...} before the shell runs.
            body = re.sub(r"\$\{@[^}]*\}", "BITBAKE_EXPRESSION", "\n".join(node.body))
            for nested in shell_functions(path, f" (task {node.func_name})", body):
                problems.append(f"{path}:{start + nested['line']}: {nested['name']} is defined inside "
                                f"BitBake task {node.func_name}, which is unsupported: a documentation "
                                "comment there would change the task signature; define it outside the task")
            functions.append({"language": "shell", "path": path, "where": " (BitBake shell function)",
                              "name": node.func_name, "line": start, "doc": comment_block(lines, start - 1)})
    return functions, problems


def bitbake_configuration(path, where, text):
    """Check that a BitBake configuration text holds only configuration statements.

    BitBake configuration files and kas ``local_conf_header`` fragments accept
    assignments, includes, and exports, but no function definitions.

    Args:
        path (str): Source path, used in reports.
        where (str): Location inside the file, such as a kas fragment, or "".
        text (str): BitBake configuration text.

    Returns:
        list[str]: The statements ConfHandler does not accept, such as a function definition.

    Example:
        ``bitbake_configuration("conf/layer.conf", "", 'BBPATH .= ":${LAYERDIR}"')`` returns ``[]``.
    """
    statements, lines, number, problems = bitbake_ast.StatementGroup(), text.splitlines(), 0, []
    while number < len(lines):
        first, line = number + 1, lines[number].rstrip()
        number += 1
        if not line.strip():
            continue
        while line.endswith("\\") and number < len(lines):
            line, number = line[:-1] + lines[number].rstrip(), number + 1
        if line.startswith("#"):
            continue
        try:
            ConfHandler.feeder(first, line, path, statements)
        except Exception as error:  # ParseError: BitBake would reject this line.
            problems.append(f"{path}:{first}{where}: unsupported BitBake configuration statement; "
                            f"configure an extractor before defining functions here ({error})")
    return problems


def discover():
    """Find every function in the tracked sources.

    Returns:
        tuple[list[dict], list[str]]: The functions found, and the problems that
        stop the build, such as a source format without an extractor.

    Example:
        ``functions, problems = discover()``
    """
    listed = subprocess.run(["git", "ls-files", "-z"], cwd=ROOT, capture_output=True, text=True, check=True).stdout
    functions, problems = [], []
    for path in sorted(set(filter(None, listed.split("\0")))):
        full = ROOT / path
        if path.startswith("docs/site/") or not full.is_file():
            continue  # Generated output derives from these sources; deleted files have no content.
        text = full.read_text(errors="replace")
        try:
            if (full.suffix == ".sh" or full.name == ".env.example"
                    or re.match(r"#!.*\b(ba)?sh\b", text.split("\n", 1)[0])):
                # .env.example is read by the shell: set -a; . ./.env; set +a.
                functions += shell_functions(path, "", text)
            elif full.suffix in BITBAKE_SUFFIXES:
                found, unsupported = bitbake_functions(path, text)
                functions += found
                problems += unsupported
            elif full.suffix == ".conf":
                problems += bitbake_configuration(path, "", text)
            elif path.startswith("ci/") and full.suffix in (".yml", ".yaml"):
                # kas fragments: their local.conf and bblayers.conf headers are BitBake configuration.
                fragment = yaml.safe_load(text) or {}
                for key in ("local_conf_header", "bblayers_conf_header"):
                    for name, value in (fragment.get(key) or {}).items():
                        problems += bitbake_configuration(path, f" ({key} {name})", str(value))
            elif full.suffix == ".py":
                functions += python_functions(path, text)
            elif path.startswith(".github/workflows/") and full.suffix in (".yml", ".yaml"):
                workflow = yaml.safe_load(text) or {}
                default = ((workflow.get("defaults") or {}).get("run") or {}).get("shell", "bash")
                for job_id, job in (workflow.get("jobs") or {}).items():
                    shell = ((job.get("defaults") or {}).get("run") or {}).get("shell", default)
                    for number, step in enumerate(job.get("steps") or [], 1):
                        where = f" (job {job_id}, step {number})"
                        if "run" not in step:
                            continue
                        if not re.match(r"(ba)?sh\b", str(step.get("shell", shell))):
                            problems.append(f"{path}{where}: shell {step['shell']!r} has no configured extractor")
                            continue
                        # GitHub substitutes expressions before the shell runs.
                        run = re.sub(r"\$\{\{.*?\}\}", "GITHUB_EXPRESSION", str(step["run"]))
                        functions += shell_functions(path, where, run)
            elif full.name == "Makefile":
                # Each recipe line runs in the shell after make turns $$ into $.
                recipes = [line[1:].lstrip("@-+").replace("$$", "$")
                           for line in text.splitlines() if line.startswith("\t")]
                functions += shell_functions(path, " (recipes)", "\n".join(recipes))
            elif full.suffix == ".html" and "<script" not in text.lower():
                pass  # Templates without scripts define no functions.
            elif full.suffix not in PLAIN_SUFFIXES and full.name not in PLAIN_NAMES:
                problems.append(f"{path}: no function extractor is configured for this format; "
                                "add it to .github/test_reference_coverage.py")
        except (SyntaxError, ValueError) as error:
            problems.append(f"{path}: {error}")
    return functions, problems


class PythonAutodoc:
    """Render Python docstrings with Sphinx autodoc and napoleon.

    autodoc imports each module, so it suits modules whose import has no side
    effects. The documentation helpers in .github/ are imported by file name.
    """

    def missing(self, function):
        """Return the required docstring parts a Python function lacks.

        Args:
            function (dict): A function found by ``python_functions``.

        Returns:
            list[str]: The missing parts; empty when the docstring is complete.

        Example:
            ``PythonAutodoc().missing({"doc": ""})`` returns ``["docstring", "Example"]``.
        """
        return [part for part, present in (("docstring", function["doc"].strip()),
                                           ("Example", "Example" in function["doc"])) if not present]

    def page(self, path, functions):
        """Return the reference page for one Python file.

        Args:
            path (str): Source path of a module importable by its file name.
            functions (list[dict]): The functions defined in it.

        Returns:
            str: A MyST page that renders the module with autodoc.

        Example:
            ``PythonAutodoc().page(".github/tool.py", functions)``
        """
        return (f"# {path}\n\n```{{eval-rst}}\n.. automodule:: {Path(path).stem}\n"
                "   :members:\n   :private-members:\n   :undoc-members:\n```\n")

    def rendered(self, function, html):
        """Return whether the built page holds the function's entry.

        Args:
            function (dict): A function found by ``python_functions``.
            html (str): The built reference page.

        Returns:
            bool: True when the page has an anchored entry for the function.

        Example:
            ``PythonAutodoc().rendered(function, html)``
        """
        return f'id="{Path(function["path"]).stem}.{function["name"]}"' in html


class Shdoc:
    """Render shell functions with the pinned shdoc, which the Makefile's setup installs.

    shdoc reads the ``@description``, ``@arg`` or ``@noargs``, ``@exitcode``, and
    ``@example`` annotations in the comment block above each definition, and runs
    on GNU Awk.
    """

    TAGS = (("@description",), ("@arg", "@noargs"), ("@exitcode",), ("@example",))

    def missing(self, function):
        """Return the required annotations a shell function's comment block lacks.

        Args:
            function (dict): A function found by ``shell_functions``.

        Returns:
            list[str]: The missing annotations, or a note that shdoc cannot render
            the name; empty when the function can be rendered completely.

        Example:
            ``Shdoc().missing({"name": "f", "doc": ""})`` returns all four annotations.
        """
        missing = ["/".join(tags) for tags in self.TAGS if not any(tag in function["doc"] for tag in tags)]
        if not re.fullmatch(r"[\w.:-]+", function["name"]):
            missing.append("a name shdoc can render")
        return missing

    def page(self, path, functions):
        """Return the reference page for one source file; a shdoc failure stops the build.

        Args:
            path (str): Source path.
            functions (list[dict]): The shell functions defined in it.

        Returns:
            str: A Markdown page holding shdoc's output.

        Example:
            ``Shdoc().page("ci/build.sh", functions)``
        """
        # Each comment block with a stub definition, so embedded functions render too.
        source = "".join(f"{self.escaped(function['doc'])}\n{function['name']}() {{\n    :\n}}\n\n"
                         for function in functions)
        output = subprocess.run(["gawk", "-f", str(ROOT / ".venv/bin/shdoc")], input=source,
                                capture_output=True, text=True, check=True).stdout
        return f"# {path}\n\n{output}"

    @staticmethod
    def escaped(doc):
        """Escape Markdown markup in a comment block, outside its example.

        shdoc copies descriptions into Markdown as they are, so ``*`` in a file
        pattern or ``<name>`` in a path would become emphasis or HTML.

        Args:
            doc (str): The comment block.

        Returns:
            str: The block with ``*`` and ``<`` escaped in every line outside ``@example``.

        Example:
            ``Shdoc.escaped("# @description Copy *.bin files.")`` returns
            ``"# @description Copy \\*.bin files."``.
        """
        lines, example = [], False
        for line in doc.splitlines():
            if re.match(r"#\s*@", line.strip()):
                example = line.strip().startswith("# @example")
            lines.append(line if example else re.sub(r"([*<])", r"\\\1", line))
        return "\n".join(lines)

    @staticmethod
    def description(doc):
        """Return a comment block's ``@description`` text with whitespace collapsed.

        Args:
            doc (str): The comment block.

        Returns:
            str: The description, joined from its continuation lines.

        Example:
            ``Shdoc.description("# @description Copy\\n#   files.")`` returns ``"Copy files."``.
        """
        text, inside = [], False
        for line in doc.splitlines():
            body = line.strip().lstrip("#").strip()
            if body.startswith("@"):
                inside = body.startswith("@description")
                body = body[len("@description"):] if inside else ""
            if inside:
                text.append(body)
        return " ".join(" ".join(text).split())

    def rendered(self, function, html):
        """Return whether the built page holds the function's entry with its description intact.

        Markup that survived into the page would change the rendered text, so the
        section must contain the whole ``@description``.

        Args:
            function (dict): A function found by ``shell_functions``.
            html (str): The built reference page.

        Returns:
            bool: True when the page has a section for the function whose text
            includes its description.

        Example:
            ``Shdoc().rendered({"name": "f", "doc": "# @description Run."},
            '<section id="f"><p>Run.</p>')`` returns ``True``.
        """
        start = html.find(f'<section id="{make_id(function["name"])}">')
        if start < 0:
            return False
        end = html.find("<section", start + 1)
        text = htmllib.unescape(re.sub(r"<[^>]+>", " ", html[start:end if end > 0 else len(html)]))
        text = text.translate(str.maketrans("\u2018\u2019\u201c\u201d\u2013\u2014", "\'\'\"\"--"))
        return self.description(function["doc"]) in " ".join(text.split())


# Renderers by language. Each provides missing(function), page(path, functions),
# and rendered(function, html), as PythonAutodoc and Shdoc do.
RENDERERS = {"python": PythonAutodoc(), "shell": Shdoc()}


def main():
    """Generate the reference pages or check the rendered entries.

    Returns:
        None: Exits with a message when a problem is found.

    Example:
        ``python .github/test_reference_coverage.py check``
    """
    mode = sys.argv[1] if len(sys.argv) == 2 else ""
    if mode not in ("generate", "check"):
        raise SystemExit(__doc__)
    functions, problems = discover()
    for function in functions:
        location = f"{function['path']}:{function['line']}{function['where']}"
        renderer = RENDERERS.get(function["language"])
        if renderer is None:
            problems.append(f"{location}: {function['name']} is a {function['language']} function, and no "
                            f"{function['language']} renderer is configured; add one to RENDERERS")
        elif missing := renderer.missing(function):
            problems.append(f"{location}: {function['name']} is undocumented; missing {', '.join(missing)}")
    if not problems and mode == "generate":
        PAGES.mkdir(parents=True, exist_ok=True)
        for path in sorted({function["path"] for function in functions}):
            defined = [function for function in functions if function["path"] == path]
            page = RENDERERS[defined[0]["language"]].page(path, defined)
            # Generated pages follow the extractor's output, not the authored style rules.
            (PAGES / f"{page_name(path)}.md").write_text(f"<!-- markdownlint-disable-file -->\n{page}")
    elif not problems:
        for function in functions:
            built = SITE / f"{page_name(function['path'])}.html"
            content = built.read_text() if built.is_file() else ""
            if not RENDERERS[function["language"]].rendered(function, content):
                problems.append(f"{function['path']}:{function['line']}: {function['name']} has no complete "
                                f"rendered entry in {built.relative_to(ROOT)}")
    for problem in problems:
        print(f"reference coverage: {problem}", file=sys.stderr)
    if problems:
        raise SystemExit(f"Reference coverage failed with {len(problems)} problem(s).")
    print(f"Reference coverage: {len(functions)} function(s) documented"
          + (" and rendered." if mode == "check" else "; pages generated."))


if __name__ == "__main__":
    main()
