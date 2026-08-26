<!-- Generated from recipes-bsp/firmware-boot/ at wrynose @ c811139d (2026-07-09) by docsgen on 2026-08-25. Manual edits will be overwritten on regeneration; change the source, not this page. -->

# recipes-bsp/firmware-boot/

A recipe that installs prebuilt, vendor-supplied boot firmware for the Arduino UNO Q into the build's deploy directory, ready for the flash image to pick up.

## Contents

| File | Description | Machine | Reference |
| --- | --- | --- | --- |
| [recipes-bsp/firmware-boot/firmware-qcom-boot-qrb2210-arduino-imola_251020.bb](firmware-qcom-boot-qrb2210-arduino-imola_251020.bb) | Prebuilt bootloader images for Arduino UNO Q. | [`uno-q`](../../conf/machine/uno-q.md) | [firmware-qcom-boot-qrb2210-arduino-imola recipe](firmware-qcom-boot-qrb2210-arduino-imola.md) |

## Description

The recipe fetches a signed archive of NHLOS (Non-Hyp Loaded Operating System) boot binaries (bootloader stages, partition tables and related images) from a vendor download URL, verifies it against a pinned SHA-256, and copies the recognized artifact types into a machine-specific subdirectory of `DEPLOY_DIR_IMAGE`. `do_configure[noexec]` and `do_compile[noexec]` switch the build steps off: the recipe unpacks and deploys firmware compiled elsewhere.

The shared mechanics (`do_deploy`, the `deploy` and `allarch` inheritance, and the binary-copy logic) live in `firmware-qcom-boot-common.inc`, which each machine-specific recipe includes. That include is not defined in this layer; another layer in the build provides it, and BitBake resolves it through `BBPATH` at parse time. A recipe here sets the three things that vary per board: `SRC_URI` and its checksum, `BOOTBINARIES`, and `QCOM_BOOT_IMG_SUBDIR`.

The firmware reaches a built image through a machine configuration variable rather than a direct dependency. The consuming machine sets `QCOM_BOOT_FIRMWARE` to the recipe's name; the class that assembles the boot and flash image adds that recipe's `do_deploy` as an image task dependency and then reads the boot content back out of the `QCOM_BOOT_FILES_SUBDIR` tree under the deploy directory. `QCOM_BOOT_IMG_SUBDIR` is the write side of the same handover: it names the directory the recipe's own `do_deploy` installs into, and the machine's `QCOM_BOOT_FILES_SUBDIR` is what the image class then reads. Both sides of that wiring sit outside this folder. Of the two machines in this layer, [`uno-q`](../../conf/machine/uno-q.md) uses the mechanism, while [`radxa-dragon-q6a`](../../conf/machine/radxa-dragon-q6a.md) blanks `QCOM_BOOT_FIRMWARE` because its boot firmware is flashed independently of the Yocto build.
