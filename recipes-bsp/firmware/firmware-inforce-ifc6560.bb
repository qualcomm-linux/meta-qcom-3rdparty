# Specify location of the corresponding NON-HLOS.bin file by adding
# NHLOS_URI:pn-firmware-inforce-ifc6560 = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "QCOM Firmware for Inforce IFC6560 board"

# ifc6560 isn't fused, so install firmware into generic location
FW_QCOM_NAME = "sda660"

FW_QCOM_LIST = "\
    a508_zap.mbn a512_zap.mbn \
    adsp.mbn \
    cdsp.mbn \
    mba.mbn modem.mbn modemuw.jsn \
    venus.mbn \
    wlanmdsp.mbn \
"

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

SPLIT_FIRMWARE_PACKAGES = "\
    linux-firmware-qcom-${FW_QCOM_NAME}-adreno \
    linux-firmware-qcom-${FW_QCOM_NAME}-audio \
    linux-firmware-qcom-${FW_QCOM_NAME}-compute \
    linux-firmware-qcom-${FW_QCOM_NAME}-modem \
    linux-firmware-qcom-${FW_QCOM_NAME}-venus \
    linux-firmware-qcom-${FW_QCOM_NAME}-wifi \
"

require recipes-bsp/firmware/firmware-qcom.inc
require recipes-bsp/firmware/firmware-qcom-nhlos.inc
