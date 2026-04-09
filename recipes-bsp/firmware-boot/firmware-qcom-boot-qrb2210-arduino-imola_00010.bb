SUMMARY = "Prebuilt bootloader images for Arduino UNO Q"
LICENSE = "LICENSE.qcom-2"

LIC_FILES_CHKSUM = "file://${UNPACKDIR}/LICENSE.${BPN};md5=6e1bae7ef13289c332a27b917fb49764"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/nexus/generic/product/chip/tech-package/QRB2210_bootbinaries.1.0/qrb2210_bootbinaries.1.0-test-device-public"
BOOTBINARIES = "Agatti_bootbinaries"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/${PV}/${BOOTBINARIES}.zip;downloadfilename=${BOOTBINARIES}_r1.0_${PV}.zip;name=bootbinaries \
    https://${FW_ARTIFACTORY}/${PV}/LICENSE.txt;downloadfilename=LICENSE.${BPN};name=license \
    http://downloads.arduino.cc/debian-im/qrb2210-arduino-imola-unoq-cdt.zip;downloadfilename=cdt-uno-q_${PV}.zip;name=cdt-uno-q \
    "
SRC_URI[bootbinaries.sha256sum] = "70168afbb254b901d17111bcfcd627af2bfab326cf5e0a007feafafa6bc63b72"
SRC_URI[license.sha256sum] = "3ad8f1fd82f2918c858cec2d0887b7df6f71a06416beecfdb3efe7d62874d863"
SRC_URI[cdt-uno-q.sha256sum] = "c606e95d0107f8c58d0dd9494e00624d1db7c4361cca20513bc78ef02ca28dd1"

# edk2-based firmware has an "older" version id
PE = "1"

QCOM_BOOT_IMG_SUBDIR = "qrb2210-arduino-imola"

COMPATIBLE_MACHINE = "(uno-q)"

include recipes-bsp/firmware-boot/firmware-qcom-boot-common.inc
