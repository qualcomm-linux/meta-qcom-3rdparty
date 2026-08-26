<!-- Generated from recipes-bsp/u-boot/u-boot-arduino_git.bb at wrynose @ c811139d (2026-07-09) by docsgen on 2026-08-25. Manual edits will be overwritten on regeneration; change the source, not this page. -->

# u-boot-arduino

| Field | Detail |
| --- | --- |
| Recipe | [recipes-bsp/u-boot/u-boot-arduino_git.bb](u-boot-arduino_git.bb) |
| Version | `git`, from the filename; `PV` set to `2025.10+2026.01-rc3+git` |
| License | not set here; it comes from the required files listed under Compatibility |

## Configuration variables

| Variable | Effect |
| --- | --- |
| `DEPENDS +=` | Build-time dependencies: recipes that must be staged before this one builds. This file adds `bc-native`, `dtc-native`, `gnutls-native`, `python3-pyelftools-native`, `skales-native` and `xxd-native`. |
| `SRC_URI =` | Source locations the recipe fetches and unpacks. |
| `SRCBRANCH =` | Recipe-local variable substituted into the SCM parameters of `SRC_URI`. This file sets it to `qcom-mainline`. |
| `SRCREV =` | Source revision fetched for the SCM entry in `SRC_URI`. This file sets it to `8008ca96a4dc53ddb3e51b96ea7e86d881ab7969`. |
| `PV =` | Package version. Left unset, the version is taken from the recipe filename. This file sets it to `2025.10+2026.01-rc3+git`. |
| `COMPATIBLE_MACHINE =` | Regular expression limiting the machines this recipe will build for. This file sets the pattern `(uno-q)`, which matches [`uno-q`](../../conf/machine/uno-q.md) in this layer. |
| `do_compile[depends] +=` | Task-level dependency: the named task of another recipe must complete before this task runs. |

## Packages

| Variable | Effect |
| --- | --- |
| `PROVIDES +=` | Additional names this recipe satisfies, so other recipes can depend on it by those names. Adds the `u-boot` provided names. |

## Tasks

| Task | Scope | Comment |
| --- | --- | --- |
| `uboot_compile_config:append()` | `append` | |
| `uboot_deploy_config:append:qcom()` | `append`, scope `qcom` (not a machine configured in this layer) | The file notes: "Symlink the 'main' u-boot.bin to boot.img so the qcom image bbclass pick it up". |

## Compatibility

- Machines: [`uno-q`](../../conf/machine/uno-q.md), from `COMPATIBLE_MACHINE = "(uno-q)"`.
- `require recipes-bsp/u-boot/u-boot-common.inc`: this file is not in this layer; BitBake resolves it through `BBPATH` from another layer in the build.
- `require recipes-bsp/u-boot/u-boot.inc`: this file is not in this layer; BitBake resolves it through `BBPATH` from another layer in the build.
- Layer dependencies: `LAYERDEPENDS_qcom-3rdparty = "core qcom"` in [conf/layer.conf](../../conf/layer.conf). This repository pins no revision of those layers; the build supplies them, and `LAYERSERIES_COMPAT_qcom-3rdparty = "wrynose"` records the Yocto release series they must match.
