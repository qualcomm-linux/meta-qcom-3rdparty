# Configuration reference

The layer is configured through [BitBake](https://github.com/openembedded/bitbake) metadata in [`conf`](../../../conf/),
the recipes and appends it adds, the [kas](https://github.com/siemens/kas) fragments in [`ci`](../../../ci/),
and the environment that `kas-container` reads. The Yocto Project's
[variable glossary](https://docs.yoctoproject.org/ref-manual/variables.html) defines
each standard variable; this page explains the values this layer chooses.

## Loading and precedence

BitBake reads `conf/layer.conf` for every
layer in the build, then `local.conf` (which kas writes from each fragment's
`local_conf_header`), then the machine file that `MACHINE` names, then the
distribution. In the
[assignment syntax](https://docs.yoctoproject.org/bitbake/bitbake-user-manual/bitbake-user-manual-metadata.html#basic-syntax),
`=` replaces a value, `?=` applies only when nothing earlier set one, `??=` is a
weak default that any other assignment beats, `+=` and `.=` append, `:append`
applies after parsing, and an override suffix such as `:rubikpi3` limits a setting
to that machine. A machine file's `?=` therefore loses to a `local.conf`
assignment and to an earlier `?=` in an include it requires.

## Layer registration

[`conf/layer.conf`](../../../conf/layer.conf) registers the layer, following the
Yocto Project's [layer configuration](https://docs.yoctoproject.org/dev-manual/layers.html#creating-your-own-layer).

| Setting | Value and purpose | Type | Required | Unset behaviour |
| --- | --- | --- | --- | --- |
| `BBPATH` | Appends the layer directory, so BitBake finds its `conf/machine` files. | Colon-separated paths | Required | Machine files are not found. |
| `BBFILES` | Adds `recipes-*/*/*.bb` and `recipes-*/*/*.bbappend`. | Space-separated globs | Required | No recipe or append is parsed. |
| `BBFILE_COLLECTIONS`, `BBFILE_PATTERN_qcom-3rdparty` | Names the layer's collection `qcom-3rdparty` and matches its files by path. | String; regular expression | Required | The layer cannot be referenced or prioritised. |
| `BBFILE_PRIORITY_qcom-3rdparty` | `5`; when two layers provide the same recipe, the higher priority wins, and [`meta-qcom`](https://github.com/qualcomm-linux/meta-qcom) uses `6`. | Integer | Optional | BitBake sets one more than the highest priority among the layers this one depends on. |
| `LAYERDEPENDS_qcom-3rdparty` | `core qcom`: requires [openembedded-core](https://github.com/openembedded/openembedded-core) and meta-qcom; BitBake stops when either is missing. | Space-separated collections | Optional | Missing layers are not reported. |
| `LAYERSERIES_COMPAT_qcom-3rdparty` | `blacksail`: Yocto Project 6.1; BitBake stops when openembedded-core's release series is not listed. | Space-separated release names | Required | BitBake warns and accepts any series. |
| `BBFILES_DYNAMIC` | Parses [`dynamic-layers/qcom-distro`](../../../dynamic-layers/qcom-distro/) only when a layer with the `qcom-distro` collection, [`meta-qcom-distro`](https://github.com/qualcomm-linux/meta-qcom-distro), is present. | `collection:glob` list | Optional | Those appends are never parsed. |

## Machines

Each file in [`conf/machine`](../../../conf/machine/) is selected by
`MACHINE`, which the board's kas fragment sets. Both machines require meta-qcom's
[`qcom-qcs6490.inc`](https://github.com/qualcomm-linux/meta-qcom/blob/master/conf/machine/include/qcom-qcs6490.inc),
the QCS6490 SoC baseline, which also brings in `qcom-base.inc` and
`qcom-common.inc`. The `#@TYPE`, `#@NAME`, and `#@DESCRIPTION` comments describe the
board for readers and tools; BitBake ignores them. An empty value disables the
feature it names.

| Setting | `rubikpi3` | `radxa-dragon-q6a` | Purpose, type, and default |
| --- | --- | --- | --- |
| `PREFERRED_PROVIDER_virtual/kernel` | `?= "linux-qcom-next"` | `?= "linux-qcom-next"` | Recipe that builds the kernel. Set before the SoC include, whose `qcom-base.inc` sets the same default with `?=`; a `local.conf` assignment, such as [`ci/linux-qcom-next.yml`](../../../ci/linux-qcom-next.yml), overrides it. |
| `MACHINE_FEATURES` | `+= "efi pci"` | `+= "efi pci"` | Hardware features: `efi` boots through UEFI and selects meta-qcom's `esp-qcom-image` as the EFI system partition; `pci` adds PCI support. Appends to `qcom-common.inc`'s list. |
| `KERNEL_CMDLINE_EXTRA` | `:append = " deferred_probe_timeout=30"` | Not set | Extra kernel arguments, added to the UKI command line by `esp-qcom-image`. Gives the HDMI bridge time to probe, as the file's comment explains. |
| `KERNEL_DEVICETREE` | `qcom/qcs6490-thundercomm-rubikpi3.dtb` | `qcom/qcs6490-radxa-dragon-q6a.dtb` | Device trees the kernel builds. String; required. |
| `QCOM_DTB_DEFAULT` | `?= "qcs6490-thundercomm-rubikpi3"` | `?= "qcs6490-radxa-dragon-q6a"` | Device tree packed into the flashable package's `dtb.bin`. Default `multi-dtb`, a FIT image of every device tree. |
| `QCOM_BOOT_FIRMWARE` | `firmware-qcom-boot-rubikpi3` | `""` | Recipe that deploys the boot firmware packed into the flashable package. Radxa's SPI NOR firmware is flashed separately. Default `""`. |
| `QCOM_BOOT_FILES_SUBDIR` | `rubikpi3` | `""` | Directory under the deploy directory holding that firmware; matches the recipe's `QCOM_BOOT_IMG_SUBDIR`. Default `""`. |
| `QCOM_PARTITION_FILES_SUBDIR` | `?= "partitions/qcs6490-thundercomm-rubikpi3/ufs"` | `""` | Partition layout, from [`qcom-ptool`](https://github.com/qualcomm-linux/qcom-ptool) through meta-qcom's `qcom-partition-conf`. Default: the value of `QCOM_BOOT_FILES_SUBDIR`. |
| `QCOM_PARTITION_FILES_SUBDIR_SPINOR` | Not set | `""` | SPI NOR partition layout. Repeats the default, `""`. |
| `QCOM_PARTITION_CONF` | Not set | `""` | Recipe providing partition files; `""` builds no partition configuration. Default `qcom-partition-conf`. |
| `QCOM_CDT_FILE` | `RubikPi3_CDT` | `""` | Configuration data table, without `.bin`, packed as `cdt.bin`. Default unset. |
| `WKS_FILE` | Not set | `efi-uki-bootdisk.wks.in` | openembedded-core's disk layout: a GPT disk with an EFI system partition (systemd-boot) and an ext4 root partition labelled `root`. |
| `QCOM_ESP_IMAGE` | Not set | `""` | Disables the separate `esp-qcom-image`, because the disk layout above creates the EFI system partition. Default `esp-qcom-image` when `efi` is a machine feature. |
| `QCOM_VFAT_SECTOR_SIZE` | Not set | `?= "512"` | Sector size of the generated FAT images, 512 for SD cards; set 4096 for UFS. Default `4096`. |
| `QCOM_BOOTIMG_ROOTFS` | Not set | `?= "PARTLABEL=root"` | Root device for meta-qcom's boot-image command lines. Has no effect: `qcom-base.inc` sets `?= "PARTLABEL=rootfs"` first, and `bitbake-getvar` reports `PARTLABEL=rootfs` for this machine. |
| `IMAGE_FSTYPES` | Not set | `+= "wic.gz wic.bmap"` | Adds a compressed disk image and its block map to the SoC's `ext4 qcomflash`. |
| `MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS` | Boot and SoC packagegroups, `packagegroup-rubikpi3-firmware` | Boot and SoC packagegroups, `packagegroup-radxa-dragon-q6a-firmware`, `packagegroup-radxa-dragon-q6a-hexagon-dsp-binaries`, `qairt-sdk-hexagon-v68` | Packages every image recommends. `packagegroup-qcom-boot-essential` and `packagegroup-machine-essential-qcom-qcs6490-soc` repeat the SoC include's values. |

## Recipes and appends

| File | Setting | Purpose |
| --- | --- | --- |
| [`packagegroup-rubikpi3.bb`](../../../recipes-bsp/packagegroups/packagegroup-rubikpi3.bb) | `PACKAGES`, `RRECOMMENDS:${PN}-firmware` | One `-firmware` package recommending the board's firmware; GPU firmware only when `DISTRO_FEATURES` has `opencl`, `opengl`, or `vulkan`. Wi-Fi and Bluetooth firmware are omitted until upstream [linux-firmware](https://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git/) has them. |
| [`packagegroup-radxa-dragon-q6a.bb`](../../../recipes-bsp/packagegroups/packagegroup-radxa-dragon-q6a.bb) | `PACKAGES`, `RRECOMMENDS:${PN}-firmware`, `RDEPENDS:${PN}-hexagon-dsp-binaries` | A `-firmware` package, with the same GPU condition, and a `-hexagon-dsp-binaries` package that requires the board's audio and compute DSP binaries. |
| [`firmware-qcom-boot-rubikpi3_20260915.bb`](../../../recipes-bsp/firmware-boot/firmware-qcom-boot-rubikpi3_20260915.bb) | `SRC_URI`, `SRCREV` | Fetches the `qli2.0` branch of [rubikpi-ai/boot-assets](https://github.com/rubikpi-ai/boot-assets) at a fixed commit. |
| | `LICENSE`, `LIC_FILES_CHKSUM` | `LicenseRef-LICENSE.qcom-2`, checked against the fetched `LICENSE.txt`. |
| | `INHIBIT_DEFAULT_DEPS`, `do_configure[noexec]`, `do_compile[noexec]`, `inherit allarch deploy` | Prebuilt files: no toolchain, no configure or compile step, one package for every architecture, and a deploy task. |
| | `QCOM_BOOT_IMG_SUBDIR`, `COMPATIBLE_MACHINE` | Deploys into `rubikpi3/`; builds only for `rubikpi3`. |
| [`linux-qcom-next_git.bbappend`](../../../recipes-kernel/linux/linux-qcom-next_git.bbappend) | `FILESEXTRAPATHS:prepend:radxa-dragon-q6a`, `SRC_URI:append:radxa-dragon-q6a` | For Radxa only, adds the kernel configuration fragment below; meta-qcom's kernel recipe merges every `.cfg` in `SRC_URI` into its `defconfig`. |
| [`realtek-eth-8169.cfg`](../../../recipes-kernel/linux/radxa-dragon-q6a/realtek-eth-8169.cfg) | `CONFIG_*` | Builds the Realtek RTL8169 Ethernet driver (`R8169`) and Realtek PHY driver into the kernel, with the PHY, MDIO bus (device tree, firmware node, and ACPI), fixed-PHY, and network self-test support they use; leaves the Realtek PHY hardware-monitoring sensor (`REALTEK_PHY_HWMON`) disabled. |
| [`qcom-multimedia-image.bbappend`](../../../dynamic-layers/qcom-distro/recipes-products/images/qcom-multimedia-image.bbappend) | `INCOMPATIBLE_LICENSE_EXCEPTIONS:append:rubikpi3` | Lets meta-qcom-distro's `qcom-multimedia-image`, which rejects `LICENSE.qcom-2`, install the RUBIK Pi 3 boot firmware. |

## kas fragments

The fragments in [`ci`](../../../ci/) use kas's
[project configuration format](https://kas.readthedocs.io/en/latest/userguide/project-configuration.html),
`header: version: 14`. Combine them with colons, for example
`ci/rubikpi3.yml:ci/qcom-distro.yml`.

| File | Purpose |
| --- | --- |
| [`base.yml`](../../../ci/base.yml) | Includes meta-qcom's `ci/base.yml` (openembedded-core, BitBake, the `nodistro` distribution, the `core-image-base` target, and their locked revisions), takes meta-qcom from its `master` branch, and adds this checkout as a layer. |
| [`rubikpi3.yml`](../../../ci/rubikpi3.yml), [`radxa-dragon-q6a.yml`](../../../ci/radxa-dragon-q6a.yml) | Include `base.yml` and set `machine`. |
| [`meta-qcom.yml`](../../../ci/meta-qcom.yml) | Declares meta-qcom at `master`, for the fragments below. |
| [`qcom-distro.yml`](../../../ci/qcom-distro.yml), [`ci.yml`](../../../ci/ci.yml), [`mirror.yml`](../../../ci/mirror.yml) | Include meta-qcom's fragment of the same name: the `qcom-distro` distribution, its layers and images; CI build settings; and the Yocto Project shared-state mirror. |
| [`linux-qcom-next.yml`](../../../ci/linux-qcom-next.yml) | Sets `PREFERRED_PROVIDER_virtual/kernel = "linux-qcom-next"` in `local.conf`. No workflow includes it. |
| [`world.yml`](../../../ci/world.yml) | Builds the `world` target with `EXCLUDE_FROM_WORLD = "1"` for every recipe except those from meta-qcom and this layer (`layer-qcom`, `layer-qcom-3rdparty` set to `0`). |

## Environment

[`.env.example`](../../../.env.example) documents the environment variables that
`kas-container` and [`ci/kas-container-shell-helper.sh`](../../../ci/kas-container-shell-helper.sh)
read, with the command that loads them.

## CI workflows

| Workflow | Trigger | What it does |
| --- | --- | --- |
| [`pr.yml`](../../../.github/workflows/pr.yml) | Pull requests to `main`, unless they change only Markdown files and the Markdown lint settings | Runs `build-yocto.yml` with the `pr` profile. |
| [`push.yml`](../../../.github/workflows/push.yml) | Pushes to `main` | Runs `build-yocto.yml`. |
| [`nightly-build.yml`](../../../.github/workflows/nightly-build.yml) | Daily at 00:22 UTC, or manually | Runs `build-yocto.yml`, skipping a build already recorded as successful. |
| [`nightly-build-wrynose.yml`](../../../.github/workflows/nightly-build-wrynose.yml) | Daily at 23:22 UTC | Starts `nightly-build.yml` on `wrynose`. |
| [`build-yocto.yml`](../../../.github/workflows/build-yocto.yml) | Called by the workflows above; runs only in the `qualcomm-linux` organisation | Runs `yocto-patchreview` and `yocto-check-layer`, and builds each machine with `nodistro` (including `world`) and with `qcom-distro` through meta-qcom's `compile.yml`. |
| [`bitbake-lint.yml`](../../../.github/workflows/bitbake-lint.yml) | Pull requests changing BitBake files outside `.github` and `ci` | Lints the changed lines with [bitbake-lint-action](https://github.com/qualcomm-linux/bitbake-lint-action); findings do not fail the check. |
| [`markdownlint.yml`](../../../.github/workflows/markdownlint.yml) | Pushes to `main` and pull requests changing Markdown | Lints every Markdown file with the rules in [`.markdownlint.yaml`](../../../.github/.markdownlint.yaml). |
| [`documentation.yml`](../../../.github/workflows/documentation.yml) | Pull requests and pushes to `main` | Builds this site and checks its function reference and offline browsing. |
| [`qcom-preflight-checks.yml`](../../../.github/workflows/qcom-preflight-checks.yml) | Pull requests, pushes to `main`, or manually | Runs Qualcomm's [preflight checks](https://github.com/qualcomm/qcom-reusable-workflows), with only the repository-lint and repository-policy checks enabled. |
| [`backport.yml`](../../../.github/workflows/backport.yml) | A pull request to `main` is merged | Opens a backport pull request for each `backport wrynose` label. |
| [`test-pr.yml`](../../../.github/workflows/test-pr.yml), [`test.yml`](../../../.github/workflows/test.yml), [`test-distro.yml`](../../../.github/workflows/test-distro.yml), [`publish-results.yml`](../../../.github/workflows/publish-results.yml) | `test-pr.yml` runs after `Build on PR` completes and calls the others | Would boot-test the build in [LAVA](https://gitlab.com/lava/lava) and publish the results; the test job is disabled until a machine has a LAVA device. |
| [`stales.yml`](../../../.github/workflows/stales.yml) | Daily at 01:30 UTC | Marks issues and pull requests stale after 30 days without activity and closes stale pull requests 5 days later; `bug` and `enhancement` are exempt. |
