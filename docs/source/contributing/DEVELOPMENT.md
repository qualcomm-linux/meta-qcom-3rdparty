# Set up your development environment

This walkthrough prepares a checkout, installs the documentation tools, and
checks the documentation.

## Prerequisites

Tested versions are in brackets.

- Git [2.55], GNU Make [4.4], curl, GNU Awk [5.4] for shdoc, and
  [uv](https://docs.astral.sh/uv/getting-started/installation/) [0.12.3], which
  installs Python 3.12 for the documentation tools.
- Chromium [151] for the offline browser check, or Playwright's browser (step 4).
- For layer builds and checks: Docker [29.7] or Podman, and
  [kas-container](https://kas.readthedocs.io/en/latest/userguide/kas-container.html) [kas 4.8.2].

The Makefile uses a POSIX shell; on Windows, run the walkthrough in WSL.

## 1. Clone the branch with the documentation tools

```sh
git clone -b docs/layer-documentation https://github.com/devdocsorg/meta-qcom-3rdparty.git
cd meta-qcom-3rdparty
git switch -c my-change
```

Send your change as the [contribution guidelines](CONTRIBUTING.md#21--pull-request-workflow)
describe.

## 2. Install the documentation tools

```sh
make -f docs/source/Makefile setup
```

This creates `.venv/` with the locked Python packages (Sphinx, MyST, Playwright,
and tree-sitter-bash), shdoc, and BitBake's parser, each at a pinned version.
Git ignores `.venv/`.

## 3. Configure layer builds

The documentation needs no settings. For layer builds, copy `.env.example` to
`.env`, set its directories, and export them with the command in its header.

## 4. Build and check the documentation

```sh
make -f docs/source/Makefile check
```

`check` rebuilds the site, then opens a copy of it offline in Chromium. Without
system Chromium, run `make -f docs/source/Makefile browser` once first.
`make -f docs/source/Makefile html` builds the site without the browser check.

Expected result: exit status 0. The output reports the functions as
`documented and rendered`, and prints a JSON line from the offline check
whose `network_requests` and `browser_errors` lists are empty. Open the
generated, Git-ignored `docs/site/index.html` directly in a browser.

## 5. Check layer changes

For changes to recipes, configuration, or CI files, run the CI-equivalent
checks from the agent guide: [yocto-patchreview routinely](AGENTS.md#4-run-routine-checks-via-ci-helper-scripts),
and yocto-check-layer before opening or updating a pull request, in the
[order it gives](AGENTS.md#6-pull-request--contribution-workflow). It also shows
how to [build with kas-container](AGENTS.md#3-build-with-kas-container-ci-style).
CI also lints Markdown and BitBake files; the
[configuration reference](../user/CONFIGURATION.md#ci-workflows) lists every
workflow.

## Function reference

[test_reference_coverage.py](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/.github/test_reference_coverage.py)
finds every function without running it: Python's parser reads Python files,
tree-sitter-bash reads shell scripts and workflow `run` steps, and BitBake's
statement parser reads recipes, appends, classes, includes, configuration
files, and kas configuration headers. Document each function directly above its
definition: a docstring with an `Example` section in Python, and shdoc's
`@description`, `@arg` or `@noargs`, `@exitcode`, and `@example` annotations for
shell functions and BitBake shell tasks. Keep a BitBake task's comment above the
task, never inside it, because the task body is part of its signature.

The build fails on an undocumented function, a missing rendered entry, or a
format without a configured extractor; add formats to `discover()` and
renderers to `RENDERERS` in that script.

## Update the tools

[requirements.txt](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/docs/source/requirements.txt)
declares the direct documentation packages, and
[requirements.lock](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/docs/source/requirements.lock)
pins them with their dependencies. After an intentional update, run
`uv pip compile --python-version 3.12 docs/source/requirements.txt -o docs/source/requirements.lock`,
then setup, and rebuild before committing. The
[Makefile](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/docs/source/Makefile)
pins shdoc and BitBake, and CI's documentation workflow runs its `setup` and
`check` targets.
