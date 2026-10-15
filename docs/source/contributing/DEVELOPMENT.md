# Set up your development environment

This walkthrough prepares a checkout, builds the layer's documentation site, and
checks it. Building images and running the layer's own checks use `kas-container`,
as the [agent guide](AGENTS.md) describes.

## Prerequisites

The site build needs Git, Make, curl, `sha256sum`, GNU Awk (which runs [shdoc](https://github.com/reconquest/shdoc)
for shell functions), [uv](https://docs.astral.sh/uv/getting-started/installation/),
and Python 3.12, which uv uses when installed and otherwise downloads. Tested with
Git 2.43, GNU Make 4.3, curl 8.5, GNU coreutils 9.4 `sha256sum`, GNU Awk 5.2,
uv 0.12.3, and Python 3.12.13. Install them on Ubuntu:

```sh
sudo apt install git make curl gawk
curl -LsSf https://astral.sh/uv/0.12.3/install.sh | sh
```

On macOS 15 or newer, which includes `sha256sum`, with [Homebrew](https://brew.sh/):

```sh
xcode-select --install
brew install gawk
curl -LsSf https://astral.sh/uv/0.12.3/install.sh | sh
```

The build draws diagrams in Chromium, tested with [Playwright](https://github.com/microsoft/playwright-python) 1.62's Chromium 151.
It uses system Chromium when installed; otherwise setup installs Playwright's
user-local Chromium, which on Ubuntu also needs its system libraries:
`sudo .venv/bin/playwright install-deps chromium` after setup.
Dependency installation needs network access; reading the built site does not.

Building images and running `yocto-patchreview` and `yocto-check-layer` also need
`kas-container` from [kas](https://github.com/siemens/kas) 5.5 and Docker, tested
with Docker 29.7; the [usage tutorial](../user/USAGE.md#prerequisites) links their
installation guides.

## 1. Clone and create a branch

```sh
git clone https://github.com/qualcomm-linux/meta-qcom-3rdparty.git
cd meta-qcom-3rdparty
git switch -c docs/my-change
```

The default branch holds the documentation tools this walkthrough uses; the
README's [branch table](../../../README.md#branches) names the branch that takes
your contribution. When contributing through a fork, clone your fork instead.

## 2. Install the documentation tools

```sh
make -f docs/source/Makefile setup
```

Setup creates `.venv` with the Python packages at the versions
`requirements.lock` pins, and downloads shdoc, the standalone script of
[Mermaid](https://github.com/mermaid-js/mermaid), and
[BitBake](https://github.com/openembedded/bitbake) at the revision
[meta-qcom](https://github.com/qualcomm-linux/meta-qcom)'s lock file pins, each
checked against its pinned hash or commit. Keep `.venv` out of commits, and keep
project dependencies and custom tools in their own environments. On Windows, run
the walkthrough in WSL.

## 3. Configure

The documentation build reads no settings. To build images, copy
[`.env.example`](../../../.env.example) to `.env` and load it, as the
[configuration reference](../user/CONFIGURATION.md#environment) explains.

## 4. Edit and build

The site is built from the README files and the guides under `docs/source/`; each
README's links to its folder's pages and subfolders form the navigation. Keep
policy files at the repository root; link to those originals instead of
maintaining another policy in the docs. Link other files relatively, as on
GitHub: a link to a missing file or folder fails the build.

```sh
make -f docs/source/Makefile html
```

Expected result: exit status 0, `docs/site/index.html`, and a final line reporting
that every function is documented and rendered. `docs/site/` is generated and
ignored by Git. The build replaces it, removing pages whose source was deleted.

## 5. Check the site

```sh
make -f docs/source/Makefile check
```

Expected result: exit status 0. The check rebuilds the site, opens a copy with
Playwright and networking disabled, follows every local link and anchor, and runs
a search. Then open `docs/site/index.html` directly in a browser: the repository
map at the bottom of the homepage draws as a diagram, and search for
`environment` lists the agent guide.

The layer's own checks run in `kas-container`: `yocto-patchreview` routinely,
and `yocto-patchreview` then `yocto-check-layer` before opening or updating a
pull request, through the CI helper as section 4 of the
[agent guide](AGENTS.md#4-run-routine-checks-via-ci-helper-scripts) shows.

## 6. Document functions

[`test_reference_coverage.py`](../../../.github/test_reference_coverage.py) finds
every function in the tracked sources, independently of the renderers, and fails
the build on formats it cannot read, on undocumented functions, and on missing
rendered entries. Document each one with a comment block directly above its
definition:

- Shell functions, in scripts, workflow `run` steps, and BitBake shell tasks, take
  shdoc annotations: `@description`,
  `@arg` or `@noargs`, `@exitcode`, and `@example`.
- In BitBake metadata, never put the comment inside a task: a task's body is
  part of its signature. A function nested inside a task, a BitBake Python
  function, and anything but configuration in a `.conf` file or a kas
  `local_conf_header` stop the build until a renderer is configured for them.
- Python functions take Google-style docstrings with an `Example` section.

## 7. Update the documentation tools

`docs/source/requirements.txt` declares the documentation packages and
`docs/source/requirements.lock` pins them with their dependencies. After changing
the requirements, regenerate the lock with
`uv pip compile --python-version 3.12 docs/source/requirements.txt -o docs/source/requirements.lock`,
then run setup and check. The
[documentation workflow](../../../.github/workflows/documentation.yml) runs the
same targets.
