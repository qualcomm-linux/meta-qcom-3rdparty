# Specify location of the corresponding NON-HLOS.bin file by adding
# NHLOS_URI:pn-firmware-qcom-ifc6410 = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "QCOM Firmware for Inforce IFC6410 board"

NHLOS_LICENSE = "Inforce_Soft_are_Click_Through_License.txt"
NHLOS_LICENSE_MD5 = "cbddec68783c9e6e0995fb21c958cf11"

# ifc6410 firmware is unsigned, so install into generic location
FW_QCOM_NAME = "apq8064"

FW_QCOM_LIST = "dsps.mbn gss.mbn q6.mbn wcnss.mbn"

S = "${UNPACKDIR}"

require recipes-bsp/firmware/firmware-qcom.inc
require recipes-bsp/firmware/firmware-qcom-nhlos.inc

# The txt file was generated from the PDF. Provide the original.
python() {
    if d.getVar("NHLOS_URI") or d.getVar("PROPRIETARY_URI"):
        d.appendVar('SRC_URI', ' file://Inforce_Soft_are_Click_Through_License.pdf')
}

do_install:append() {
    if [ -n "${NHLOS_URI}${PROPRIETARY_URI}" ] ; then
        install -d ${D}${datadir}/${BPN}
        install -m 0644 Inforce_Soft_are_Click_Through_License.pdf ${D}${datadir}/${BPN}
    fi
}

SPLIT_FIRMWARE_PACKAGES = " \
    linux-firmware-qcom-${FW_QCOM_NAME}-dsps \
    linux-firmware-qcom-${FW_QCOM_NAME}-gss \
    linux-firmware-qcom-${FW_QCOM_NAME}-q6 \
    linux-firmware-qcom-${FW_QCOM_NAME}-wifi \
"
