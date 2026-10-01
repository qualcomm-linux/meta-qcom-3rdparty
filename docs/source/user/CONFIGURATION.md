# Configuration reference

This page lists the settings this layer chooses. The
[BitBake variable glossary](https://docs.yoctoproject.org/ref-manual/variables.html)
and the [kas project configuration](https://kas.readthedocs.io/en/latest/userguide/project-configuration.html)
define the fields themselves. In BitBake files, `=` sets a value, `?=` sets a
default that an earlier assignment overrides, `+=` and `:append` add to the
value, and a machine override such as `:rubikpi3` limits a line to that machine.
Environment settings for local builds are documented in
[.env.example](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/.env.example).

## Layer

BitBake reads
[conf/layer.conf](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/conf/layer.conf)
when the layer is listed in `bblayers.conf`, which kas writes from the `repos`
of its configuration.

| Setting | Purpose | Type | Value |
| --- | --- | --- | --- |
| `BBPATH` | Lets BitBake find this layer's `conf/machine/` files. | Colon-separated paths, appended | `:${LAYERDIR}` |
| `BBFILES` | Adds the recipes and appends in `recipes-*/*/`. | Space-separated globs | `${LAYERDIR}/recipes-*/*/*.bb`, `*.bbappend` |
| `BBFILE_COLLECTIONS` | Names the layer collection. | Word | `qcom-3rdparty` |
| `BBFILE_PATTERN_qcom-3rdparty` | Matches the files that belong to the collection. | Regular expression | `^${LAYERDIR}/` |
| `BBFILE_PRIORITY_qcom-3rdparty` | Ranks this layer's recipes and appends against other layers. | Integer | `5` |
| `LAYERDEPENDS_qcom-3rdparty` | Requires [openembedded-core](https://github.com/openembedded/openembedded-core) (`core`) and [meta-qcom](https://github.com/qualcomm-linux/meta-qcom) (`qcom`). | Collection names | `core qcom` |
| `LAYERSERIES_COMPAT_qcom-3rdparty` | Declares the Yocto Project release series the layer supports. | Release codenames | `blacksail` |
| `BBFILES_DYNAMIC` | Adds `dynamic-layers/qcom-distro/` only when the `qcom-distro` collection is present. | `collection:glob` pairs | `qcom-distro:${LAYERDIR}/dynamic-layers/qcom-distro/*/*/*.bb`, `*.bbappend` |

## Machines

Each file in `conf/machine/` is selected by setting `MACHINE` to its name. Both
machines use the QCS6490 SoC: they set the kernel provider, then
`require conf/machine/include/qcom-qcs6490.inc` from
[meta-qcom](https://github.com/qualcomm-linux/meta-qcom), which supplies the
defaults below. The comment lines at the top (`#@TYPE`, `#@NAME`,
`#@DESCRIPTION`) name the board for layer tools.

| Setting | Purpose | Type | meta-qcom default | `rubikpi3` | `radxa-dragon-q6a` |
| --- | --- | --- | --- | --- | --- |
| `PREFERRED_PROVIDER_virtual/kernel` | Kernel recipe; set before the SoC include because the first `?=` wins. | Recipe name | `linux-qcom-next` | `?= linux-qcom-next` | `?= linux-qcom-next` |
| `MACHINE_FEATURES` | Adds UEFI boot and PCI support. | Feature list | `alsa bluetooth usbgadget usbhost wifi` | `+= efi pci` | `+= efi pci` |
| `KERNEL_DEVICETREE` | Device tree built by the kernel. | DTB paths | Unset | `qcom/qcs6490-thundercomm-rubikpi3.dtb` | `qcom/qcs6490-radxa-dragon-q6a.dtb` |
| `QCOM_DTB_DEFAULT` | Device tree the boot firmware loads. | DTB name | `multi-dtb` | `?= qcs6490-thundercomm-rubikpi3` | `?= qcs6490-radxa-dragon-q6a` |
| `KERNEL_CMDLINE_EXTRA` | Extra kernel arguments for the UKI. | Arguments | Empty | `:append " deferred_probe_timeout=30"` (lets the HDMI bridge probe) | Not set |
| `QCOM_BOOT_FIRMWARE` | Recipe that deploys the boot binaries. | Recipe name, or empty for none | Empty | `firmware-qcom-boot-rubikpi3` | `""` (SPI NOR firmware is flashed separately) |
| `QCOM_BOOT_FILES_SUBDIR` | Deploy subfolder holding the boot binaries. | Path | Empty | `rubikpi3` | `""` |
| `QCOM_PARTITION_FILES_SUBDIR` | Deploy subfolder holding the partition tables from [qcom-ptool](https://github.com/qualcomm-linux/qcom-ptool). | Path | `${QCOM_BOOT_FILES_SUBDIR}` | `?= partitions/qcs6490-thundercomm-rubikpi3/ufs` | `""` |
| `QCOM_PARTITION_FILES_SUBDIR_SPINOR` | SPI NOR partition tables. | Path | Empty | Not set | `""` |
| `QCOM_PARTITION_CONF` | Recipe that deploys the partition tables. | Recipe name | `qcom-partition-conf` | Not set | `""` |
| `QCOM_CDT_FILE` | CDT file copied into the flash package as `cdt.bin`. | File name without `.bin` | Unset | `RubikPi3_CDT` | `""` |
| `WKS_FILE` | Disk layout for the `wic` image. | Kickstart file | Unset | Not set | `efi-uki-bootdisk.wks.in` (ESP with systemd-boot and a UKI) |
| `QCOM_ESP_IMAGE` | Separate ESP image recipe. | Recipe name | `esp-qcom-image` with `efi` | Not set | `""` (the WKS file creates the ESP) |
| `QCOM_VFAT_SECTOR_SIZE` | ESP sector size. | Bytes | `4096` | Not set | `?= 512` (SD cards) |
| `QCOM_BOOTIMG_ROOTFS` | Root device on the kernel command line. | Device spec | `PARTLABEL=rootfs` | Not set | `?= PARTLABEL=root` |
| `IMAGE_FSTYPES` | Image formats written to the deploy folder. | Type list | `ext4 qcomflash` | Not set | `+= wic.gz wic.bmap` |
| `MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS` | Boot packages and board firmware in every image. | Package list | The two SoC packagegroups | `+=` SoC packagegroups, `packagegroup-rubikpi3-firmware` | `+=` SoC packagegroups, `packagegroup-radxa-dragon-q6a-firmware`, `packagegroup-radxa-dragon-q6a-hexagon-dsp-binaries`, `qairt-sdk-hexagon-v68` |

The SoC packagegroups are `packagegroup-qcom-boot-essential` and
`packagegroup-machine-essential-qcom-qcs6490-soc`, which the SoC include
already recommends. "Not set" leaves the meta-qcom default in place.

## Recipe appends and kernel fragment

| File | Setting | Purpose and value |
| --- | --- | --- |
| [linux-qcom-next_git.bbappend](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/recipes-kernel/linux/linux-qcom-next_git.bbappend) | `FILESEXTRAPATHS:prepend:radxa-dragon-q6a` | Searches `radxa-dragon-q6a/` first for the kernel's local files: `${THISDIR}/radxa-dragon-q6a:`. |
| | `SRC_URI:append:radxa-dragon-q6a` | Merges `file://realtek-eth-8169.cfg` into the kernel configuration. |
| [qcom-multimedia-image.bbappend](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/dynamic-layers/qcom-distro/recipes-products/images/qcom-multimedia-image.bbappend) | `INCOMPATIBLE_LICENSE_EXCEPTIONS:append:rubikpi3` | Lets the qcom-distro multimedia image ship the RUBIK Pi 3 boot firmware under `LICENSE.qcom-2`: `firmware-qcom-boot-rubikpi3:LICENSE.qcom-2`. |

[realtek-eth-8169.cfg](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/recipes-kernel/linux/radxa-dragon-q6a/realtek-eth-8169.cfg)
builds the Radxa Dragon Q6A's Realtek Ethernet support into the kernel. Each
line is a [Kconfig](https://docs.kernel.org/kbuild/kconfig-language.html)
option; `y` builds it in, and an unlisted option keeps the kernel
configuration's value.

| Option | Purpose | Value |
| --- | --- | --- |
| `CONFIG_R8169` | Realtek RTL8169-family Ethernet driver. | `y` |
| `CONFIG_REALTEK_PHY` | Realtek PHY driver. | `y` |
| `CONFIG_REALTEK_PHY_HWMON` | PHY temperature sensor. | Not set (disabled) |
| `CONFIG_PHYLIB`, `CONFIG_FIXED_PHY` | PHY layer and fixed-link PHYs. | `y` |
| `CONFIG_MDIO_BUS`, `CONFIG_FWNODE_MDIO`, `CONFIG_OF_MDIO`, `CONFIG_ACPI_MDIO` | MDIO bus and its firmware, device tree, and ACPI descriptions. | `y` |
| `CONFIG_NET_SELFTESTS` | Network self-tests used by the PHY layer. | `y` |

## kas configuration

The files in `ci/` are kas configuration (format `version: 14`). A build
combines them with colons, as in `ci/rubikpi3.yml:ci/qcom-distro.yml`, and later
files override earlier ones. Each file's first line points YAML editors at the
kas schema.

| File | Sets |
| --- | --- |
| [base.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/ci/base.yml) | Adds [meta-qcom](https://github.com/qualcomm-linux/meta-qcom) (`master`) and this repository, and includes the meta-qcom `ci/base.yml`: [openembedded-core](https://github.com/openembedded/openembedded-core), [BitBake](https://github.com/openembedded/bitbake), `nodistro`, and the `core-image-base` target. |
| [meta-qcom.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/ci/meta-qcom.yml) | Declares the [meta-qcom](https://github.com/qualcomm-linux/meta-qcom) repository, so the files below can include its fragments. |
| [qcom-distro.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/ci/qcom-distro.yml), [ci.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/ci/ci.yml), [mirror.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/ci/mirror.yml) | Include the [meta-qcom](https://github.com/qualcomm-linux/meta-qcom) fragment of the same name: the Qualcomm Linux distribution and its images, CI build settings, and the sstate mirror. |
| [rubikpi3.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/ci/rubikpi3.yml), [radxa-dragon-q6a.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/ci/radxa-dragon-q6a.yml) | Include `base.yml` and set `machine`. |
| [linux-qcom-next.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/ci/linux-qcom-next.yml) | Sets `PREFERRED_PROVIDER_virtual/kernel = "linux-qcom-next"` in `local.conf`. |
| [world.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/ci/world.yml) | Builds the `world` target with only the [meta-qcom](https://github.com/qualcomm-linux/meta-qcom) and meta-qcom-3rdparty recipes: `EXCLUDE_FROM_WORLD = "1"`, then `"0"` for `layer-qcom` and `layer-qcom-3rdparty`. |

## CI workflows

| Workflow | Trigger | What it does |
| --- | --- | --- |
| [pr.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/.github/workflows/pr.yml) | Pull requests to `main`, except Markdown-only changes | Runs `build-yocto.yml` with the `pr` profile. |
| [push.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/.github/workflows/push.yml) | Pushes to `main` | Runs `build-yocto.yml`. |
| [nightly-build.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/.github/workflows/nightly-build.yml) | Daily, or manually | Runs `build-yocto.yml`, skipping inputs that already built successfully. |
| [nightly-build-wrynose.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/.github/workflows/nightly-build-wrynose.yml) | Daily | Starts the nightly build on `wrynose`. |
| [build-yocto.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/.github/workflows/build-yocto.yml) | Called by the three above | Locks the kas configuration, runs `yocto-patchreview` and `yocto-check-layer`, and builds every machine with `nodistro` and `qcom-distro` through the [meta-qcom](https://github.com/qualcomm-linux/meta-qcom) compile workflow. |
| [bitbake-lint.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/.github/workflows/bitbake-lint.yml) | Pull requests changing BitBake files outside `.github/` and `ci/` | Lints the changed lines with [bitbake-lint-action](https://github.com/qualcomm-linux/bitbake-lint-action), without failing the check. |
| [markdownlint.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/.github/workflows/markdownlint.yml) | Pull requests and pushes to `main` changing Markdown | Lints every Markdown file with the rules in `.github/.markdownlint.yaml`. |
| [documentation.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/.github/workflows/documentation.yml) | Pull requests and pushes to `main` | Runs the documentation `setup` and `check` targets. |
| [qcom-preflight-checks.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/.github/workflows/qcom-preflight-checks.yml) | Pull requests, pushes to `main`, or manually | Runs repolinter from [qcom-reusable-workflows](https://github.com/qualcomm/qcom-reusable-workflows); its other checks are disabled. |
| [test-pr.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/.github/workflows/test-pr.yml), [test.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/.github/workflows/test.yml), [test-distro.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/.github/workflows/test-distro.yml), [publish-results.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/.github/workflows/publish-results.yml) | After a pull request build | Boot tests on LAVA and result publishing; disabled until a machine has a LAVA device. |
| [backport.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/.github/workflows/backport.yml) | Merged pull requests to `main` | Opens a backport pull request for the `backport wrynose` label. |
| [stales.yml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/.github/workflows/stales.yml) | Daily | Marks inactive issues and pull requests stale, and closes stale pull requests. |

## Repository settings

- [CODEOWNERS](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/.github/CODEOWNERS)
  requests review from both maintainers for every path, documentation included.
- [.markdownlint.yaml](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/.github/.markdownlint.yaml)
  disables the line-length (`MD013`) and inline-HTML (`MD033`) rules, allows
  repeated headings in different sections (`MD024: siblings_only`), and requires
  a top-level heading on the first line (`MD041`).
- [.gitignore](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/.gitignore)
  and the documentation build files explain their settings in comments.
