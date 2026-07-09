# Agent Guide for meta-qcom-3rdparty (wrynose / LTS branch)

This file guides automation agents to run builds / checks the same way CI does:

- use **kas-container** (isolated from host),
- keep `DL_DIR` and `SSTATE_DIR` outside the repo so caches are shared,
- run `yocto-patchreview` routinely, and run `yocto-check-layer` before
  opening/updating a PR, via the CI helper scripts.

> **This is the `wrynose` LTS branch.** It is not the primary development
> branch. Changes are expected to land on **main** first and then be
> **backported** here with `git cherry-pick -x`, unless they are specific to
> `wrynose`. See [section 6](#6-pull-request--contribution-workflow-wrynose-lts)
> for the full workflow.

## Project Overview

meta-qcom-3rdparty is an OpenEmbedded / Yocto Project BSP layer for
Third-Party Maintained Qualcomm based platforms. It depends on `meta-qcom`
(the official Qualcomm reference layer) and provides machine configurations and
recipes for boards not officially maintained by Qualcomm.

`wrynose` is the **LTS Stable branch**, focused on long term support and
compatibility with the most recent Yocto Project LTS release. `main` is the
primary development branch.

## 1) Prerequisites

1. `kas-container` available on PATH, or set `KAS_CONTAINER=/abs/path/to/kas-container`
   (from [kas-container](https://github.com/siemens/kas/blob/master/kas-container)).
2. Container runtime access (Docker/Podman backend used by `kas-container`).
3. Work directories outside the repository for build outputs and shared caches.

### Container runtime smoke test (required order)

Run Docker first:

```sh
docker run --rm hello-world
```

Then check Podman:

```sh
if command -v podman >/dev/null 2>&1; then
  podman run --rm hello-world
else
  echo "podman not installed; continue with Docker backend"
fi
```

Notes:

- Do not use `sudo` unless the host setup explicitly requires it.
- Do not create or modify user groups as part of this workflow.
- If Podman is unavailable, Docker-only operation is acceptable.

## 2) Recommended environment

If `KAS_WORK_DIR`, `DL_DIR`, and `SSTATE_DIR` are already set in the environment, use them
directly — do not override them. Only set defaults when they are absent:

```sh
export REPO_DIR="$(pwd)"                               # meta-qcom-3rdparty checkout
export KAS_WORK_DIR="${KAS_WORK_DIR:-/path/to/kas-work}"      # outside repo to avoid polling the checkout
export DL_DIR="${DL_DIR:-/path/to/shared-cache/downloads}"
export SSTATE_DIR="${SSTATE_DIR:-/path/to/shared-cache/sstate-cache}"
mkdir -p "${DL_DIR}" "${SSTATE_DIR}" "${KAS_WORK_DIR}"
```

## 3) Build with kas-container (CI style)

CI build composition pattern:
`:ci/<machine>.yml[:distro.yml]`

Machines available at `conf/machines`, kas fragments at `ci/<machine>.yml`.

Example builds:

```sh
# Build for uno-q with the Qualcomm distro
export KAS_YAMLS="ci/uno-q.yml:ci/qcom-distro.yml"
"${KAS_CONTAINER:-kas-container}" build "${KAS_YAMLS}"

# World build (all machines in this layer)
export KAS_YAMLS="ci/base.yml:ci/world.yml"
"${KAS_CONTAINER:-kas-container}" build "${KAS_YAMLS}"
```

## 4) Run routine checks via CI helper scripts

For routine local validation, run:

```sh
ci/kas-container-shell-helper.sh ci/yocto-patchreview.sh
```

Run `yocto-check-layer` only before opening/updating a pull request:

```sh
ci/kas-container-shell-helper.sh ci/yocto-check-layer.sh
```

## 5) Direct kas shell alternative (no helper wrapper)

For one-off commands:

```sh
kas-container shell --skip repos_checkout ci/uno-q.yml -c "bitbake core-image-base"
```

Use the helper scripts for CI parity whenever possible.

## 6) Pull request / contribution workflow (wrynose LTS)

`wrynose` is the LTS branch. **Propose changes against `main` first.**
We expect every change to be backported to `wrynose` unless it is specific to
`wrynose` (e.g. it does not apply to `main`, or `main` has diverged in a way
that makes the change meaningless there).

The full backport workflow — the default `git cherry-pick -x` path from
`main`, the exception for wrynose-only changes, the CI-equivalent checks to
run before opening a PR, and the `[Backport wrynose]` commit message
conventions — is documented in [BACKPORTING.md](BACKPORTING.md).

If the change **cannot** be submitted to `main` (it is specific to
`wrynose`), then submit it directly against `wrynose`, and **explain in the
commit body and PR description why it is wrynose-only** and not a backport.
The general contribution flow (fork, topic branch, rebase on latest upstream
`wrynose`, open a GitHub pull request, iterate via PR discussion) and the
constraints below still apply.

Important constraints from `docs/contributing.md`:

- **No recipe forks:** do not copy recipes from `meta-qcom`, `meta-qcom-hwe`, or
  base OE / Yocto layers. Use `.bbappend` files instead.
- **Machine-specific isolation:** use machine overrides (`:machine` / `:append:machine`)
  to confine board-specific logic. SoC-generic behavior must go upstream to `meta-qcom`.
- **BSP scope only:** limit changes to kernel, firmware, device tree, drivers, and
  partition configs. Distribution-specific logic belongs in a separate distro layer.
- **No branch or folder segregation per vendor:** all boards live together in the layer.

Follow Yocto submission guidance referenced in README:
[Preparing Changes for Submission](https://docs.yoctoproject.org/dev/contributor-guide/submit-changes.html#preparing-changes-for-submission)

Before opening/updating a PR, run CI-equivalent checks in this order:

```sh
ci/kas-container-shell-helper.sh ci/yocto-patchreview.sh
ci/kas-container-shell-helper.sh ci/yocto-check-layer.sh
```

## 7) Commit message best practices (project style)

Use the style seen in recent history:

- `component: imperative summary` (preferred when scoped), e.g.
  - `conf: add machine configuration for Arduino UNO Q`
  - `packagegroup-uno-q: add recipe`
  - `ci: add uno-q.yml kas fragment`
- Or concise imperative summary when cross-cutting, e.g.
  - `workflows: add build for arduino uno-q`

Every commit **must** include a `Signed-off-by` trailer using the identity from
the local git configuration:

```sh
git commit -s   # or pass --signoff; fetches user.name / user.email from git config
```

If committing programmatically, append the trailer explicitly:

```text
Signed-off-by: $(git config user.name) <$(git config user.email)>
```

Never fabricate a name or email; always read from `git config`.

Guidelines:

- Keep subject line short and specific; capture intent, not a file-by-file dump.
- Use imperative mood (`Add`, `Update`, `Drop`, `Enable`, `Revert`).
- Add a body for non-trivial changes explaining **why** and key design decisions.
- Wrap body lines for readability (~72 chars).
- Use consistent recipe bump wording for version updates, e.g.
  `recipe-name: Update to vX.Y.Z`.
- Avoid mixing unrelated changes in one commit; split logically.
- Each patch must be logically coherent, self-contained, and independently buildable.
- The tree must remain in a functional state after every commit.
- Fixups within the same patch series are not allowed; changes should be corrected
  in the patch where they are introduced.
