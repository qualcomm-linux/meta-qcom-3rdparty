<!-- Generated from recipes-kernel/images/ at wrynose @ c811139d (2026-07-09) by docsgen on 2026-08-25. Manual edits will be overwritten on regeneration; change the source, not this page. -->

# recipes-kernel/images/

A bbappend that packages the `uno-q` device tree into the EFI System Partition image, so the board's unified kernel image carries its own device tree.

## Contents

| File | Description | Machine | Reference |
| --- | --- | --- | --- |
| [recipes-kernel/images/esp-qcom-image.bbappend](esp-qcom-image.bbappend) | Modifies the `esp-qcom-image` recipe, which is provided outside this layer. | [`uno-q`](../../conf/machine/uno-q.md) | [esp-qcom-image bbappend](esp-qcom-image-bbappend.md) |

## Description

The bbappend machine-scopes `KERNEL_DEVICETREE` for `uno-q` onto the `esp-qcom-image` recipe. That recipe is not defined in this layer; another layer in the build provides it, along with the image classes it inherits. It builds a VFAT-formatted EFI System Partition image holding a systemd-boot unified kernel image, the boot artifact EFI firmware loads directly, which is a different path from the Android-style boot image and U-Boot FIT flows used on Qualcomm boards that boot without EFI.

The ESP image recipe is selected per machine. The Qualcomm image machinery defaults `QCOM_ESP_IMAGE` to it when `MACHINE_FEATURES` contains `efi`, and copies its output into the flashable bundle beside the rootfs, the device tree image and the boot firmware. Within this two-machine layer exactly one board exercises it, [`uno-q`](../../conf/machine/uno-q.md); [`radxa-dragon-q6a`](../../conf/machine/radxa-dragon-q6a.md) sets `QCOM_ESP_IMAGE` empty and builds its ESP inline through its wic kickstart file instead.

The bbappend's single change overrides the base recipe's empty `KERNEL_DEVICETREE` so that `uno-q` gets its device tree packaged into the unified kernel image. The in-file comment records the arrangement as an interim one, pending mature EDK2 firmware for the board.
