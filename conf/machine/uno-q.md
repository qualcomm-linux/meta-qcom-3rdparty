<!-- Generated from conf/machine/uno-q.conf at wrynose @ c811139d (2026-07-09) by docsgen on 2026-08-25. Manual edits will be overwritten on regeneration; change the source, not this page. -->

# uno-q

Machine configuration for Arduino UNO Q. Source: [conf/machine/uno-q.conf](uno-q.conf).

## Selection

```text
MACHINE = "uno-q"
```

## Configuration variables

| Variable | Effect |
| --- | --- |
| `MACHINEOVERRIDES =.` | Extends the machine's override tokens, so assignments scoped to an added token apply to this machine. This file adds `arduino:`. |
| `PREFERRED_PROVIDER_virtual/kernel ?=` | Selects the recipe that provides `virtual/kernel` for this machine. This file sets it to [`linux-arduino`](../../recipes-kernel/linux/linux-arduino.md). |
| `MACHINE_FEATURES =` | Hardware capability flags for the board. Distro policy and recipes test them to decide which support packages and configuration to include. Sets the machine features to `efi`, `usbhost`, `usbgadget`, `alsa`, `wifi` and `bluetooth`. |
| `PREFERRED_PROVIDER_virtual/bootloader ?=` | Selects the recipe that provides `virtual/bootloader` for this machine. This file sets it to [`u-boot-arduino`](../../recipes-bsp/u-boot/u-boot-arduino.md). |
| `UBOOT_CONFIG =` | The U-Boot configuration to build. This file sets it to `qrb2210-arduino-imola`. |
| `UBOOT_CONFIG[qrb2210-arduino-imola] =` | Maps a U-Boot configuration name to the defconfig that builds it. This file sets it to `qcom_defconfig`. |
| `UBOOT_INITIAL_ENV =` | Names the initial environment file that OpenEmbedded's `u-boot` class embeds. Set empty, no initial environment is embedded. |
| `QCOM_DTB_DEFAULT ?=` | Names this machine's default device tree, without the `.dtb` suffix. It doubles as a flag: the value `multi-dtb` selects a combined multi-device-tree image instead of one default. This file sets it to `qrb2210-arduino-imola`. Defined by the `meta-qcom` layer's classes, outside this repository. |
| `KERNEL_DEVICETREE ?=` | Device tree blobs the kernel recipe builds and deploys for this machine. This file sets it to `qcom/qrb2210-arduino-imola.dtb`. |
| `KERNEL_CMDLINE_EXTRA +=` | Additional kernel command-line arguments for this machine. They reach all three boot paths: the Android-style boot image, the U-Boot FIT script, and the unified kernel image. This file adds `deferred_probe_timeout=30`. Defined by the `meta-qcom` layer's classes, outside this repository. |
| `MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS +=` (2 assignments, accumulated) | Packages recommended into images built on `packagegroup-core-boot` for this machine. Recommendations are installed by default, though distro policy can filter them out. This file adds [`packagegroup-uno-q-firmware`](../../recipes-bsp/packagegroups/packagegroup-uno-q.md), [`packagegroup-uno-q-hexagon-dsp-binaries`](../../recipes-bsp/packagegroups/packagegroup-uno-q.md) and `qbootctl` (external). The file notes: "Helper util to tell the android bootloader to mark the boot as successfull. The boot firmware will switch to slot B and fail to boot otherwise." |
| `QCOM_CDT_FILE =` | Basename, without extension, of this machine's configuration data table binary. This file sets it to `cdt`. Defined by the `meta-qcom` layer's classes, outside this repository. |
| `QCOM_BOOT_FILES_SUBDIR =` | Names the subdirectory of the deploy directory the flash package is assembled from: boot firmware, configuration data table, and SPI NOR artifacts. This file sets it to `qrb2210-arduino-imola`. Defined by the `meta-qcom` layer's classes, outside this repository. |
| `QCOM_PARTITION_FILES_SUBDIR ?=` | Names the subdirectory holding this machine's GPT and rawprogram partition files, which are copied into the flash package. This file sets it to `partitions/qrb2210-unoq/emmc-16GB`. Defined by the `meta-qcom` layer's classes, outside this repository. |
| `QCOM_BOOT_FIRMWARE =` | Names the recipe providing the vendor boot-firmware bundle. When set, the flash-image build takes a `do_deploy` dependency on that recipe. This file sets it to [`firmware-qcom-boot-qrb2210-arduino-imola`](../../recipes-bsp/firmware-boot/firmware-qcom-boot-qrb2210-arduino-imola.md). Defined by the `meta-qcom` layer's classes, outside this repository. |

Names marked (external) are not provided by this layer.

## Compatibility

- `require conf/machine/include/qcom-qcm2290.inc`: this file is not in this layer; BitBake resolves it through `BBPATH` from another layer in the build.
- Layer dependencies: `LAYERDEPENDS_qcom-3rdparty = "core qcom"` in [conf/layer.conf](../layer.conf). This repository pins no revision of those layers; the build supplies them, and `LAYERSERIES_COMPAT_qcom-3rdparty = "wrynose"` records the Yocto release series they must match.
- [firmware-qcom-boot-qrb2210-arduino-imola recipe](../../recipes-bsp/firmware-boot/firmware-qcom-boot-qrb2210-arduino-imola.md) declares `COMPATIBLE_MACHINE` for this machine.
- [linux-arduino recipe](../../recipes-kernel/linux/linux-arduino.md) declares `COMPATIBLE_MACHINE` for this machine.
- [u-boot-arduino recipe](../../recipes-bsp/u-boot/u-boot-arduino.md) declares `COMPATIBLE_MACHINE` for this machine.
- [esp-qcom-image bbappend](../../recipes-kernel/images/esp-qcom-image-bbappend.md) carries changes scoped to this machine.
- [qcom-multimedia-image bbappend](../../dynamic-layers/qcom-distro/recipes-products/images/qcom-multimedia-image-bbappend.md) carries changes scoped to this machine.
- Build configuration [ci/uno-q.yml](../../ci/uno-q.yml) selects this machine; see the [ci/ folder index](../../ci/README.md).
