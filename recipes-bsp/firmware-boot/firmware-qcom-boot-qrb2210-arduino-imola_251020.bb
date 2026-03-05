SUMMARY = "Prebuilt bootloader images for Arduino UNO Q"

LICENSE = "LICENSE.qcom"
LIC_FILES_CHKSUM = "file://LICENSE;md5=cbbe399f2c983ad51768f4561587f000"

SRC_URI = "https://downloads.arduino.cc/debian-im/unoq-bootloader-emmc-linux-${PV}.zip"
SRC_URI[sha256sum] = "c606e95d0107f8c58d0dd9494e00624d1db7c4361cca20513bc78ef02ca28dd1"

BOOTBINARIES = "unoq-bootloader-emmc-linux-251020"

QCOM_BOOT_IMG_SUBDIR = "qrb2210-arduino-imola"

COMPATIBLE_MACHINE = "(uno-q)"

include recipes-bsp/firmware-boot/firmware-qcom-boot-common.inc
