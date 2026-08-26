<!-- Generated from conf/machine/ at wrynose @ c811139d (2026-07-09) by docsgen on 2026-08-25. Manual edits will be overwritten on regeneration; change the source, not this page. -->

# conf/machine/

Machine configurations for the two boards this layer supports, [`uno-q`](uno-q.md) and [`radxa-dragon-q6a`](radxa-dragon-q6a.md). A build selects one by setting `MACHINE` to the file's basename.

## Contents

| File | Description | Reference |
| --- | --- | --- |
| [conf/machine/radxa-dragon-q6a.conf](radxa-dragon-q6a.conf) | Radxa Dragon Q6A: Machine configuration for Radxa Dragon Q6A, with QCS6490. | [radxa-dragon-q6a machine configuration](radxa-dragon-q6a.md) |
| [conf/machine/uno-q.conf](uno-q.conf) | Arduino UNO Q: Machine configuration for Arduino UNO Q. | [uno-q machine configuration](uno-q.md) |

## Description

A machine configuration in a BSP layer defines the hardware target BitBake builds for: kernel provider and device tree, bootloader and boot-firmware handling, machine features, image format, and board-specific package recommendations. BitBake locates `conf/machine/<MACHINE>.conf` by searching `BBPATH`, which every layer's `layer.conf` has extended with its own root, so a machine configuration is found wherever its owning layer sits, as long as that layer is in the build.

Both boards are third-party hardware built on Qualcomm SoCs the sibling `meta-qcom` layer already supports. `uno-q` targets the Arduino UNO Q (QRB2210, whose SoC-family include is named for QCM2290, the same silicon family under two commercial names; the board's own `UBOOT_CONFIG` and device-tree strings use `qrb2210-*`), and `radxa-dragon-q6a` targets the Radxa Dragon Q6A (QCS6490). Each one `require`s a SoC-family include, `conf/machine/include/qcom-qcm2290.inc` and `conf/machine/include/qcom-qcs6490.inc` respectively, that is not defined in this layer (external); `require` resolves the path through `BBPATH` at parse time, so the file parses once the layer providing that include is in `bblayers.conf` as well.

The two boards deliberately exercise different boot architectures, and the difference is visible directly in their variable settings. `uno-q` follows a conventional Yocto-managed U-Boot flow: it names a `PREFERRED_PROVIDER_virtual/bootloader`, defines a `UBOOT_CONFIG`, and points `QCOM_BOOT_FILES_SUBDIR` and `QCOM_PARTITION_FILES_SUBDIR` at board-specific boot and partition content the build packages and deploys. `radxa-dragon-q6a` targets a board whose early-boot firmware (PBL, XBL, UEFI/EDK2, TrustZone, hypervisor, device configuration, AOP and CDT) lives in vendor-managed SPI-NOR flash and is provisioned outside the Yocto build, so it leaves the bootloader provider to that firmware and blanks the Qualcomm boot and partition variables. The sibling layer's `image_types_qcom.bbclass` and its `do_image_qcomflash` task treat each blanked variable as a no-op, so the resulting image holds the OS disk alone: an EFI System Partition and a rootfs, assembled through the machine's wic template around a systemd-boot unified kernel image.
