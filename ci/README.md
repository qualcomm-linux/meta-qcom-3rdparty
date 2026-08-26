<!-- Generated from ci/ at wrynose @ c811139d (2026-07-09) by docsgen on 2026-08-25. Manual edits will be overwritten on regeneration; change the source, not this page. -->

# ci/

The kas build-environment fragments a build composes, and the shell scripts CI runs inside that environment to build and validate the layer on top of `meta-qcom`.

## Contents

| File | Description | Machine |
| --- | --- | --- |
| [ci/base.yml](base.yml) | kas fragment. Includes `ci/base.yml` from the `meta-qcom` repository (external). Declares repositories `meta-qcom` (branch `wrynose`), `meta-qcom-3rdparty` (this repository). | Not set |
| [ci/ci.yml](ci.yml) | kas fragment. Includes `ci/meta-qcom.yml` from this repository, `ci/ci.yml` from the `meta-qcom` repository (external). | Not set |
| [ci/kas-container-shell-helper.sh](kas-container-shell-helper.sh) | Shell script run with `/bin/sh -e`. Usage: `kas-container-shell-helper.sh /path/to/script`. | Not set |
| [ci/linux-qcom-next.yml](linux-qcom-next.yml) | kas fragment. Adds `local_conf_header` entries `kernelprovider`. | Not set |
| [ci/meta-qcom.yml](meta-qcom.yml) | kas fragment. Declares repositories `meta-qcom` (branch `wrynose`). | Not set |
| [ci/mirror.yml](mirror.yml) | kas fragment. Includes `ci/meta-qcom.yml` from this repository, `ci/mirror.yml` from the `meta-qcom` repository (external). | Not set |
| [ci/qcom-distro.yml](qcom-distro.yml) | kas fragment. Includes `ci/meta-qcom.yml` from this repository, `ci/qcom-distro.yml` from the `meta-qcom` repository (external). | Not set |
| [ci/radxa-dragon-q6a.yml](radxa-dragon-q6a.yml) | kas fragment. Includes `ci/base.yml` from this repository. | [`radxa-dragon-q6a`](../conf/machine/radxa-dragon-q6a.md) |
| [ci/uno-q.yml](uno-q.yml) | kas fragment. Includes `ci/base.yml` from this repository. | [`uno-q`](../conf/machine/uno-q.md) |
| [ci/world.yml](world.yml) | kas fragment. Adds `local_conf_header` entries `world_build`. Builds `world`. | Not set |
| [ci/yocto-buildstats.sh](yocto-buildstats.sh) | Shell script run with `/bin/sh -e`. Usage: `yocto-buildstats.sh REPO_DIR WORK_DIR`. | Not set |
| [ci/yocto-check-layer.sh](yocto-check-layer.sh) | Shell script run with `/bin/bash -e`. Usage: `yocto-check-layer.sh REPO_DIR WORK_DIR`. | Not set |
| [ci/yocto-patchreview.sh](yocto-patchreview.sh) | Shell script run with `/bin/sh -e`. Usage: `yocto-patchreview.sh REPO_DIR WORK_DIR`. | Not set |

## Description

kas is a declarative front end for BitBake and OpenEmbedded builds: a fragment declares the repositories to fetch, the layers and machine to configure, and `local_conf_header` blocks that are concatenated into `local.conf`. Fragments compose on a kas command line or through a header's own includes, with later-merged content overriding scalar keys and extending lists.

The fragments here layer on top of `meta-qcom`'s environment through cross-repository includes. The base fragment includes the like-named fragment from the external `meta-qcom` repository, where the distro, the core repositories and the shared `local_conf_header` entries live, and adds two declarations of its own: `meta-qcom` pinned to a branch, and this repository itself with no URL, which resolves to the checkout kas runs from. The two per-machine fragments each include that base and set `machine`, the minimal delta that makes a build concrete.

Of the six fragments that remain, one carries the `meta-qcom` repository pin alone, which the three cross-repository option fragments each include ahead of the external fragment they pull in: CI signature and package-class settings, an sstate mirror, and the full qcom-distro layer set. The last two stand alone. One pins `PREFERRED_PROVIDER_virtual/kernel`. The other sets `EXCLUDE_FROM_WORLD` for everything and then clears it again for the two Qualcomm layers, so that its `world` target builds the recipes those two layers provide rather than one image.

The shell scripts implement the CI-side checks that run inside a kas environment: a container shell helper that shells into the base environment and runs a named script, an OpenEmbedded-Core patch review that fails on malformed `Signed-off-by` or `Upstream-Status` tags, a `yocto-check-layer` compliance run that derives the machines to validate from `conf/machine/*.conf`, and a buildstats post-processor. Three pieces of this content, `world.yml`, `mirror.yml` and `yocto-buildstats.sh`, are not referenced by this repository's workflows, which are indexed in [.github/workflows/](../.github/workflows/README.md).
