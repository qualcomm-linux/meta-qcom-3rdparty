# Specify location of the corresponding NON-HLOS.bin file by adding
# NHLOS_URI:pn-firmware-inforce-ifc6410 = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "QCOM Firmware for Inforce IFC6410 board"

# ifc6410 firmware is unsigned, so install into generic location
FW_QCOM_NAME = "apq8064"

FW_QCOM_LIST = "dsps.mbn gss.mbn q6.mbn wcnss.mbn"

LICENSE = "LicenseRef-NHLOS-inforce"
LIC_FILES_CHKSUM = "file://Inforce_Soft_are_Click_Through_License.txt;md5=cbddec68783c9e6e0995fb21c958cf11"
NO_GENERIC_LICENSE[NHLOS-inforce] = "Inforce_Soft_are_Click_Through_License.txt"

# The txt file was generated from the PDF. Provide the original.
SRC_URI = " \
    file://Inforce_Soft_are_Click_Through_License.txt \
    file://Inforce_Soft_are_Click_Through_License.pdf \
"

install_license() {
    install -m 0644 Inforce_Soft_are_Click_Through_License.txt ${D}${datadir}/${BPN}
    install -m 0644 Inforce_Soft_are_Click_Through_License.pdf ${D}${datadir}/${BPN}
}

SPLIT_FIRMWARE_PACKAGES = " \
    linux-firmware-qcom-${FW_QCOM_NAME}-dsps \
    linux-firmware-qcom-${FW_QCOM_NAME}-gss \
    linux-firmware-qcom-${FW_QCOM_NAME}-q6 \
    linux-firmware-qcom-${FW_QCOM_NAME}-wifi \
"

require recipes-bsp/firmware/firmware-qcom.inc
require recipes-bsp/firmware/firmware-qcom-nhlos.inc
