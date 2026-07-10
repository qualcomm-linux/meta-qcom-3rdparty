# Specify location of the corresponding NON-HLOS.bin file by adding
# NHLOS_URI:pn-firmware-qcom-ifc6560 = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "QCOM Firmware for Inforce IFC6560 board"

NHLOS_LICENSE = "Inforce_Soft_are_Click_Through_License.txt"
NHLOS_LICENSE_MD5 = "cbddec68783c9e6e0995fb21c958cf11"

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

SPLIT_FIRMWARE_PACKAGES = "\
    linux-firmware-qcom-${FW_QCOM_NAME}-adreno \
    linux-firmware-qcom-${FW_QCOM_NAME}-audio \
    linux-firmware-qcom-${FW_QCOM_NAME}-compute \
    linux-firmware-qcom-${FW_QCOM_NAME}-modem \
    linux-firmware-qcom-${FW_QCOM_NAME}-venus \
    linux-firmware-qcom-${FW_QCOM_NAME}-wifi \
"
