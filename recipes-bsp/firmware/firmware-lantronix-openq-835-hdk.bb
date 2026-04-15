# Specify location of the corresponding NON-HLOS.bin file by adding
# NHLOS_URI:pn-firmware-qcom-openq-835-hdk = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "QCOM Firmware for Lantronix Open-Q 835 HDK"

NHLOS_LICENSE = "Intrinsyc_license.txt"
NHLOS_LICENSE_MD5 = "79c13010406436e6ca03672f8727f0da"

# Open-Q 835 HDK isn't locked, so install firmware into generic location
FW_QCOM_NAME = "apq8098"

FW_QCOM_LIST = "\
    a540_gpmu.fw2 a540_zap.mbn \
    adsp.mbn adspr.jsn adspua.jsn \
    ipa_fws.elf \
    mba.mbn modem.mbn modemr.jsn modemuw.jsn \
    slpi_v2.mbn slpir.jsn slpius.jsn \
    venus.mbn \
    wlanmdsp.mbn \
"

S = "${UNPACKDIR}"

require recipes-bsp/firmware/firmware-qcom.inc
require recipes-bsp/firmware/firmware-qcom-nhlos.inc

# The txt file was generated from the RTF. Provide the original.
python() {
    if d.getVar("NHLOS_URI") or d.getVar("PROPRIETARY_URI"):
        d.appendVar('SRC_URI', ' file://Intrinsyc_license.rtf')
}

do_install:append() {
    if [ -n "${NHLOS_URI}${PROPRIETARY_URI}" ] ; then
        install -d ${D}${datadir}/${BPN}
        install -m 0644 Intrinsyc_license.rtf ${D}${datadir}/${BPN}
    fi

    if [ -n "${PROPRIETARY_URI}" ] ; then
        find "${D}${FW_QCOM_PATH}" -name "a540_gpmu.fw2*" -exec mv {} "${D}${FW_QCOM_BASE_PATH}/" \;
    fi
}

SPLIT_FIRMWARE_PACKAGES = "\
    linux-firmware-qcom-adreno-a540 \
    linux-firmware-qcom-${FW_QCOM_NAME}-adreno \
    linux-firmware-qcom-${FW_QCOM_NAME}-audio \
    linux-firmware-qcom-${FW_QCOM_NAME}-ipa \
    linux-firmware-qcom-${FW_QCOM_NAME}-modem \
    linux-firmware-qcom-${FW_QCOM_NAME}-sensors \
    linux-firmware-qcom-${FW_QCOM_NAME}-venus \
    linux-firmware-qcom-${FW_QCOM_NAME}-wifi \
"

FILES:linux-firmware-qcom-${FW_QCOM_NAME}-sensors += "${FW_QCOM_PATH}/slpi_v2.mbn*"

FILES:linux-firmware-qcom-adreno-a540 = "${FW_QCOM_BASE_PATH}/a540_gpmu.fw2*"
RDEPENDS:linux-firmware-qcom-adreno-a540 = "linux-firmware-qcom-adreno-a530"
