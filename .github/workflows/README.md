<!-- Generated from .github/workflows/ at wrynose @ c811139d (2026-07-09) by docsgen on 2026-08-25. Manual edits will be overwritten on regeneration; change the source, not this page. -->

# .github/workflows/

The GitHub Actions workflows this repository runs: lint gates, a reusable Yocto build pipeline driven by push, pull-request and nightly triggers, and a call out to Qualcomm's shared compliance workflows.

## Contents

| File | Description |
| --- | --- |
| [.github/workflows/bitbake-lint.yml](bitbake-lint.yml) | Bitbake Lint. Triggers: `pull_request`. Calls `qualcomm-linux/bitbake-lint-action`. |
| [.github/workflows/build-yocto.yml](build-yocto.yml) | Build Yocto. Triggers: `workflow_call`. Calls `qualcomm-linux/meta-qcom/.github/actions/compile`. Reusable (`workflow_call` only). |
| [.github/workflows/markdownlint.yml](markdownlint.yml) | Markdown Lint. Triggers: `push`, `pull_request`. Calls `DavidAnson/markdownlint-cli2-action`. |
| [.github/workflows/nightly-build.yml](nightly-build.yml) | Nightly Build. Triggers: `workflow_dispatch`, `schedule`. Calls `./.github/workflows/build-yocto.yml`. |
| [.github/workflows/pr.yml](pr.yml) | Build on PR. Triggers: `pull_request`. Calls `./.github/workflows/build-yocto.yml`. |
| [.github/workflows/publish-results.yml](publish-results.yml) | Publish test results. Triggers: `workflow_call`. Calls `EnricoMi/publish-unit-test-result-action`. Reusable (`workflow_call` only). |
| [.github/workflows/push.yml](push.yml) | Build on push. Triggers: `push`. Calls `./.github/workflows/build-yocto.yml`. |
| [.github/workflows/qcom-preflight-checks.yml](qcom-preflight-checks.yml) | QC Preflight Checks. Triggers: `pull_request`, `push`, `workflow_dispatch`. Calls `qualcomm/qcom-reusable-workflows/.github/workflows/reusable-qcom-preflight-checks-orchestrator.yml`. |

## Description

Across the folder the triggers in use are `pull_request`, `push`, `schedule`, `workflow_call`, `workflow_dispatch`.

The build path composes three layers. The push, pull-request and nightly workflows each delegate to the local reusable build workflow. That workflow, gated to the `qualcomm-linux` repository owner and run on self-hosted runners, first locks the kas fragment stack into a shared lockfile artifact, then fans out into a compliance job, which runs Yocto patch review and `yocto-check-layer` through the container shell helper in [ci/](../../ci/README.md), and a build matrix. Each matrix cell calls an external composite action from the `meta-qcom` repository, which assembles the kas fragment stack for the cell, runs a world pass followed by a targeted image build, optionally adds an SDK pass, and uploads results to object storage. The matrix crosses the two machines with `nodistro` and `qcom-distro`, plus one row building `uno-q` on `qcom-distro` with the `linux-qcom-next` kernel fragment substituted; a summary job aggregates the per-cell result URLs into the run summary.

Compliance and quality checks split across two independent mechanisms. The preflight workflow delegates to an external Qualcomm reusable-workflow orchestrator pinned at a major-version tag, with the repolinter gate as the one check enabled: semgrep, dependency review, copyright and license, commit email, commit message and ARMOR checks are all explicitly disabled in the call. The BitBake and Markdown lint workflows are standalone gates calling marketplace actions directly, and their trigger scopes differ: the BitBake gate runs on pull requests only, inferring the Yocto release series from `LAYERSERIES_COMPAT` in `conf/layer.conf` and linting the BitBake metadata a change touches, while the Markdown gate runs on pull requests and on pushes to the branch this layer tracks, linting all Markdown against the repository-local configuration. The JUnit result publisher is a `workflow_call` callee that no workflow in this repository invokes.
