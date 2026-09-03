DESCRIPTION = "CDT (Configuration Data Table) Firmware for Arduino UNO Q platform"

SRC_URI = " \
    https://downloads.arduino.cc/debian-im/qrb2210-arduino-imola-unoq-cdt.zip;downloadfilename=cdt-arduino-imola_${PV}.zip;name=cdt-arduino-imola \
    "
SRC_URI[cdt-arduino-imola.sha256sum] = "a99c66bb1e89a8d4b1e49015fbb51d2b0fb025d0751283399555113135083313"

QCOM_CDT_SUBDIR = "qrb2210"

COMPATIBLE_MACHINE = "(uno-q)"

include recipes-bsp/firmware-boot/firmware-qcom-cdt-common.inc
