# Copyright (c) 2026 DevDocs
# SPDX-License-Identifier: BSD-3-Clause
"""Build the repository's README files and guides into one site, as GitHub shows them.

The repository root is the Sphinx source directory. Each README's links to the pages
in its folder and to its subfolders' READMEs become the site's navigation, so the
site follows the same folder structure readers browse on GitHub. A link to a file or
folder that is not a page opens it on GitHub at the built revision, and a link to a
path that does not exist stops the build. Mermaid diagrams are drawn as SVG during
the build, in headless Chromium, so the site needs no script or network to show them.
"""
import posixpath
import re
import shutil
import subprocess
from pathlib import Path

from docutils import nodes
from sphinx.addnodes import download_reference, pending_xref
from sphinx.errors import ExtensionError

CODE_SPAN = re.compile(r"(`+).+?\1", re.S)
LINK = re.compile(r"(\]\(\s*<?)([^)\s>]+)(>?(?:\s+\"[^\"]*\")?\s*\))")
REFERENCE = re.compile(r"^((?: {0,3}>)* {0,3}\[(?!\^)[^\]]+\]:\s*<?)(\S+?)(>?(?:\s.*)?)$")


def git(root, *args):
    """Return the output lines of a Git command run in the repository.

    Args:
        root (pathlib.Path): The repository root.
        *args (str): The Git arguments.

    Returns:
        list[str]: The command's output lines.

    Raises:
        subprocess.CalledProcessError: Git fails, for example outside a checkout.

    Example:
        ``git(Path("."), "rev-parse", "HEAD")`` returns ``["<commit>"]``.
    """
    return subprocess.run(["git", *args], cwd=root, capture_output=True, text=True,
                          check=True).stdout.splitlines()


def configure(app, config):
    """Record the repository's files and read only its Markdown pages.

    Pages are the tracked and new, not ignored, Markdown files outside dot-prefixed
    folders, plus the generated function reference. Links can name only tracked
    files, which exist at the built revision.

    Args:
        app (sphinx.application.Sphinx): The running Sphinx application.
        config (sphinx.config.Config): The configuration, edited in place.

    Raises:
        sphinx.errors.ExtensionError: ``readme_site_repository`` is not set.

    Example:
        ``app.connect("config-inited", configure)``
    """
    if not config.readme_site_repository.startswith("https://"):
        raise ExtensionError("Set readme_site_repository in conf.py to the repository's https:// URL")
    root = Path(app.srcdir)
    tracked = set(git(root, "ls-files"))
    new = set(git(root, "ls-files", "--others", "--exclude-standard"))
    app.readme_site = {
        "files": tracked,
        "folders": {str(parent) for path in tracked for parent in Path(path).parents if str(parent) != "."},
        "pages": {path for path in tracked | new if path.endswith(".md")
                  and not any(part.startswith(".") for part in Path(path).parts)},
        "revision": git(root, "rev-parse", "HEAD")[0],
        "browser": None,
    }
    config.include_patterns = sorted(app.readme_site["pages"]) + [
        "docs/source/contributing/.generated/*.md"]


def is_image(line, end):
    """Return whether the link whose text ends at ``end`` is an image.

    Args:
        line (str): A Markdown line.
        end (int): The index of the ``]`` that closes the link text.

    Returns:
        bool: True for ``![alt](path)``; Sphinx copies images into the site itself.

    Example:
        ``is_image("![map](map.png)", 5)`` returns ``True``.
    """
    depth = 0
    for index in range(end, -1, -1):
        depth += {"]": 1, "[": -1}.get(line[index], 0)
        if depth == 0:
            return index > 0 and line[index - 1] == "!"
    return False


def site_target(app, docname, target, children):
    """Return the link target the site uses for one Markdown link target.

    Args:
        app (sphinx.application.Sphinx): The running Sphinx application.
        docname (str): The page holding the link, without its suffix.
        target (str): The link target as written, relative to the page.
        children (list[str]): The README's navigation entries, extended in place.

    Returns:
        str: A page-relative Markdown path, a GitHub URL, or the target unchanged.

    Example:
        ``site_target(app, "README", "LICENSE", [])`` returns
        ``"https://github.com/<owner>/<repo>/blob/<commit>/LICENSE"``.
    """
    site = app.readme_site
    folder = posixpath.dirname(docname)
    if re.match(r"[a-z][a-z0-9+.-]*:|#", target, re.I):
        return target
    path, _, fragment = target.partition("#")
    path = posixpath.normpath(path.lstrip("/") if path.startswith("/") else posixpath.join(folder, path))
    anchor = f"#{fragment}" if fragment else ""
    readme = "README.md" if path == "." else f"{path}/README.md"
    if (path == "." or path in site["folders"]) and readme in site["pages"]:
        path = readme
    if path in site["pages"]:
        parent = posixpath.dirname(path)
        own_page = parent == folder and path != f"{docname}.md"
        subfolder = (posixpath.basename(path) == "README.md" and parent != folder
                     and posixpath.dirname(parent) == folder)
        relative = posixpath.relpath(path, folder or ".")
        if posixpath.basename(docname) == "README" and (own_page or subfolder) and relative[:-3] not in children:
            children.append(relative[:-3])
        return relative + anchor
    kind = "blob" if path in site["files"] else "tree" if path in site["folders"] else None
    if kind is None:
        return target
    return f"{app.config.readme_site_repository}/{kind}/{site['revision']}/{path}{anchor}"


def rewrite_prose(app, docname, line, children):
    """Rewrite the link targets in prose, leaving code spans and images.

    Args:
        app (sphinx.application.Sphinx): The running Sphinx application.
        docname (str): The page holding the line, without its suffix.
        line (str): Text MyST reads as prose, such as a paragraph's lines.
        children (list[str]): The README's navigation entries, extended in place.

    Returns:
        str: The line with each link target replaced by ``site_target``'s result.

    Example:
        ``rewrite_prose(app, "README", "See [LICENSE](LICENSE).", [])``
    """
    pieces, last = [], 0
    for span in [*CODE_SPAN.finditer(line), None]:
        end = span.start() if span else len(line)
        text = line[last:end]
        pieces.append(LINK.sub(lambda m, text=text: m[0] if is_image(text, m.start())
                               else m[1] + site_target(app, docname, m[2], children) + m[3], text))
        pieces.append(span[0] if span else "")
        last = span.end() if span else end
    return "".join(pieces)


def resolve_links(app, docname, source):
    """Rewrite a page's links to non-page files and folders before MyST reads it.

    A folder link opens the folder's README page when it has one. Any other tracked
    file or folder opens on GitHub at the built revision, keeping its ``#`` fragment.
    Links to pages, other sites, and missing paths are left for MyST, so a missing
    path stops the build. Code blocks and code spans are not changed. Each README then gains a
    hidden toctree of the pages in its folder and its subfolders' READMEs it links to.

    Args:
        app (sphinx.application.Sphinx): The running Sphinx application.
        docname (str): The page being read, without its suffix.
        source (list[str]): The page source, edited in place.

    Example:
        ``app.connect("source-read", resolve_links)``
    """
    from markdown_it.renderer import RendererHTML
    from myst_parser.config.main import MdParserConfig
    from myst_parser.parsers.mdit import create_md_parser

    # Rewrite only text MyST reads as prose or reference definitions, never code.
    prose, code = {}, set()
    for token in create_md_parser(MdParserConfig(), RendererHTML).parse(source[0]):
        if token.map and token.type == "inline":
            prose[token.map[0]] = token.map[1]
        elif token.map and token.type in ("code_block", "fence", "html_block"):
            code.update(range(*token.map))
    original, children, lines, number = source[0].splitlines(keepends=True), [], [], 0
    while number < len(original):
        if number in prose:
            # Rewrite a paragraph whole, so a code span can wrap across its lines.
            lines.append(rewrite_prose(app, docname, "".join(original[number:prose[number]]), children))
            number = prose[number]
            continue
        line = original[number]
        if number not in code:
            line = REFERENCE.sub(lambda m: m[1] + site_target(app, docname, m[2], children) + m[3], line)
        lines.append(line)
        number += 1
    source[0] = "".join(lines)
    if children:
        source[0] += "\n\n```{toctree}\n:hidden:\n\n" + "\n".join(children) + "\n```\n"


def check_links_and_draw(app, doctree):
    """Stop on links to missing files and draw Mermaid diagrams as SVG.

    Args:
        app (sphinx.application.Sphinx): The running Sphinx application.
        doctree (docutils.nodes.document): The page's document, edited in place.

    Raises:
        sphinx.errors.ExtensionError: A link names a missing or untracked file or
            folder, or a diagram does not render.

    Example:
        ``app.connect("doctree-read", check_links_and_draw, priority=100)``
    """
    site = app.readme_site
    folder = posixpath.dirname(app.env.docname)
    for node in list(doctree.findall(lambda n: isinstance(n, (download_reference, pending_xref)))):
        if node.get("reftype") != "myst" or node.get("refdomain") == "doc":
            continue
        # Links the source pass could not see, such as a link around an image badge.
        path = posixpath.normpath(posixpath.join(folder, node["reftarget"]))
        kind = "blob" if path in site["files"] else "tree" if path in site["folders"] else None
        if kind is None:
            raise ExtensionError(f"{app.env.docname}: link to a missing or untracked file or folder: "
                                 f"{node['reftarget']}")
        uri = f"{app.config.readme_site_repository}/{kind}/{site['revision']}/{path}"
        node.replace_self(nodes.reference("", "", *node.children, refuri=uri))
    for number, block in enumerate(list(doctree.findall(nodes.literal_block))):
        if block.get("language") == "mermaid":
            ident = "mermaid-" + re.sub(r"\W", "-", app.env.docname) + f"-{number}"
            svg = draw(app, ident, block.astext())
            block.replace_self(nodes.raw("", f'<div class="mermaid">{svg}</div>', format="html"))


def draw(app, ident, code):
    """Return a Mermaid diagram drawn as SVG by the pinned Mermaid script in Chromium.

    Args:
        app (sphinx.application.Sphinx): The running Sphinx application.
        ident (str): A page-unique identifier for the diagram's SVG element.
        code (str): The Mermaid source.

    Returns:
        str: The diagram's SVG markup.

    Raises:
        sphinx.errors.ExtensionError: Chromium cannot start or the diagram does not render.

    Example:
        ``draw(app, "mermaid-README-0", "flowchart LR\\n  a --> b")`` returns ``"<svg ...>"``.
    """
    from playwright.sync_api import Error, sync_playwright

    state = app.readme_site
    try:
        if state["browser"] is None:
            playwright = sync_playwright().start()
            browser = playwright.chromium.launch(executable_path=shutil.which("chromium"))
            page = browser.new_page()
            page.set_content("<!doctype html><html><body></body></html>")
            page.add_script_tag(path=str(Path(app.srcdir) / ".venv/mermaid/mermaid.min.js"))
            # As on GitHub, a diagram wider than the page scales down to fit it.
            page.evaluate('mermaid.initialize({startOnLoad: false, securityLevel: "strict"})')
            state["browser"] = (playwright, browser, page)
        page = state["browser"][2]
        return page.evaluate("([id, code]) => mermaid.render(id, code).then(result => result.svg)", [ident, code])
    except Error as error:
        raise ExtensionError(f"{app.env.docname}: Mermaid diagram {ident} did not render: {error.message}")


def close_browser(app, exception):
    """Close the diagram browser when the build ends.

    Args:
        app (sphinx.application.Sphinx): The running Sphinx application.
        exception (Exception | None): The build's error, if any.

    Example:
        ``app.connect("build-finished", close_browser)``
    """
    if getattr(app, "readme_site", {}).get("browser"):
        playwright, browser, _ = app.readme_site["browser"]
        browser.close()
        playwright.stop()


def setup(app):
    """Register the configuration value and the link, navigation, and diagram handlers.

    Args:
        app (sphinx.application.Sphinx): The running Sphinx application.

    Returns:
        dict: The extension's parallel-safety metadata.

    Example:
        ``extensions = ["readme_site"]`` in ``conf.py`` calls ``setup(app)``.
    """
    app.add_config_value("readme_site_repository", "", "env", str)
    app.connect("config-inited", configure)
    app.connect("source-read", resolve_links)
    # Run before Sphinx collects download files, so no file is copied by mistake.
    app.connect("doctree-read", check_links_and_draw, priority=100)
    app.connect("build-finished", close_browser)
    return {"parallel_read_safe": True, "parallel_write_safe": True}
