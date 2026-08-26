<!-- Generated from conf/machine/radxa-dragon-q6a.conf at wrynose @ c811139d (2026-07-09) by docsgen on 2026-08-25. Manual edits will be overwritten on regeneration; change the source, not this page. -->

# radxa-dragon-q6a

Machine configuration for Radxa Dragon Q6A, with QCS6490. Source: [conf/machine/radxa-dragon-q6a.conf](radxa-dragon-q6a.conf).

```text
Boot architecture:
  SPI NOR (Radxa-managed):  PBL -> XBL -> UEFI/EDK2 -> discovers ESP
  SD/UFS/NVMe (Yocto-built): ESP (systemd-boot + UKI) + rootfs

SPI NOR firmware is flashed separately via Radxa's edl-ng package.
This machine conf only produces the OS disk image (ESP + rootfs).
```

Bootloader: Radxa EDK2 in SPI NOR; no U-Boot needed

Do NOT set PREFERRED_PROVIDER_virtual/bootloader. The qcomflash class defaults to uefi.elf from QCOM_BOOT_FILES_SUBDIR, which is empty, so it's a no-op.

## Selection

```text
MACHINE = "radxa-dragon-q6a"
```

## Configuration variables

| Variable | Effect |
| --- | --- |
| `MACHINE_FEATURES +=` | Hardware capability flags for the board. Distro policy and recipes test them to decide which support packages and configuration to include. Adds the `efi` and `pci` machine features. |
| `KERNEL_DEVICETREE =` | Device tree blobs the kernel recipe builds and deploys for this machine. This file sets it to `qcom/qcs6490-radxa-dragon-q6a.dtb`. Grouped in the file under "Kernel / Device Tree". |
| `PREFERRED_PROVIDER_virtual/kernel ?=` | Selects the recipe that provides `virtual/kernel` for this machine. This file sets it to `linux-qcom-next` (external). |
| `QCOM_BOOT_FIRMWARE =` | Names the recipe providing the vendor boot-firmware bundle. When set, the flash-image build takes a `do_deploy` dependency on that recipe. This file sets it to the empty string. Defined by the `meta-qcom` layer's classes, outside this repository. Grouped in the file under "Boot firmware: NOT managed by Yocto". The file notes: "SPI NOR contents (XBL, UEFI, TZ, HYP, DEVCFG, AOP, CDT) are provided by Radxa and flashed independently. Blank these out so the qcomflash class doesn't try to package or depend on Qualcomm boot firmware." |
| `QCOM_BOOT_FILES_SUBDIR =` | Names the subdirectory of the deploy directory the flash package is assembled from: boot firmware, configuration data table, and SPI NOR artifacts. This file sets it to the empty string. Defined by the `meta-qcom` layer's classes, outside this repository. |
| `QCOM_PARTITION_FILES_SUBDIR =` | Names the subdirectory holding this machine's GPT and rawprogram partition files, which are copied into the flash package. This file sets it to the empty string. Defined by the `meta-qcom` layer's classes, outside this repository. |
| `QCOM_PARTITION_FILES_SUBDIR_SPINOR =` | The same, for the SPI NOR variant of the partition table. This file sets it to the empty string. Defined by the `meta-qcom` layer's classes, outside this repository. |
| `QCOM_PARTITION_CONF =` | Names the recipe providing the partition configuration for this machine. This file sets it to the empty string. Defined by the `meta-qcom` layer's classes, outside this repository. |
| `QCOM_CDT_FILE =` | Basename, without extension, of this machine's configuration data table binary. This file sets it to the empty string. Defined by the `meta-qcom` layer's classes, outside this repository. |
| `WKS_FILE =` | The wic kickstart file describing the partition layout of the generated disk image. This file sets it to `efi-uki-bootdisk.wks.in`. Grouped in the file under "EFI System Partition + UKI boot". The file notes: "Use the canned UEFI UKI WKS; it creates the ESP inline via the bootimg_efi plugin (systemd-boot + UKI) with correct GPT type GUID. No need for the standalone esp-qcom-image recipe." |
| `QCOM_ESP_IMAGE =` | Names the EFI system partition image recipe for this machine, selected when `MACHINE_FEATURES` contains `efi`. This file sets it to the empty string. Defined by the `meta-qcom` layer's classes, outside this repository. |
| `QCOM_VFAT_SECTOR_SIZE ?=` | Sector size passed to `mkfs.vfat -S` for this machine's device tree vfat image; it has to match the storage medium. This file sets it to `512`. Defined by the `meta-qcom` layer's classes, outside this repository. The file notes: "SD card uses 512-byte sectors; override to 4096 for UFS targets". |
| `QCOM_BOOTIMG_ROOTFS ?=` | The `root=` value baked into this machine's Android-style boot image by `mkbootimg`. Required: the build fails when it is unset. This file sets it to `PARTLABEL=root`. Defined by the `meta-qcom` layer's classes, outside this repository. The file notes: "Root device identifier for the UKI kernel cmdline". |
| `QCOM_DTB_DEFAULT ?=` | Names this machine's default device tree, without the `.dtb` suffix. It doubles as a flag: the value `multi-dtb` selects a combined multi-device-tree image instead of one default. This file sets it to `qcs6490-radxa-dragon-q6a`. Defined by the `meta-qcom` layer's classes, outside this repository. |
| `IMAGE_FSTYPES +=` | Image formats produced for this machine. This file adds `wic`, `wic.gz` and `wic.bmap`. Grouped in the file under "Image output". |
| `MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS +=` | Packages recommended into images built on `packagegroup-core-boot` for this machine. Recommendations are installed by default, though distro policy can filter them out. This file adds `packagegroup-qcom-boot-essential` (external), `packagegroup-machine-essential-qcom-qcs6490-soc` (external), [`packagegroup-radxa-dragon-q6a-firmware`](../../recipes-bsp/packagegroups/packagegroup-radxa-dragon-q6a.md), [`packagegroup-radxa-dragon-q6a-hexagon-dsp-binaries`](../../recipes-bsp/packagegroups/packagegroup-radxa-dragon-q6a.md) and `qairt-sdk-hexagon-v68` (external). Grouped in the file under "Firmware packages". |

Names marked (external) are not provided by this layer.

## Compatibility

- `require conf/machine/include/qcom-qcs6490.inc`: this file is not in this layer; BitBake resolves it through `BBPATH` from another layer in the build.
- Layer dependencies: `LAYERDEPENDS_qcom-3rdparty = "core qcom"` in [conf/layer.conf](../layer.conf). This repository pins no revision of those layers; the build supplies them, and `LAYERSERIES_COMPAT_qcom-3rdparty = "wrynose"` records the Yocto release series they must match.
- [linux-qcom-next bbappend](../../recipes-kernel/linux/linux-qcom-next-bbappend.md) carries changes scoped to this machine.
- Build configuration [ci/radxa-dragon-q6a.yml](../../ci/radxa-dragon-q6a.yml) selects this machine; see the [ci/ folder index](../../ci/README.md).
