# Specify location of the corresponding NON-HLOS.bin file by adding
# NHLOS_URI:pn-firmware-qcom-openq-835-hdk = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "QCOM Firmware for Lantronix Open-Q 835 HDK"

LICENSE = "CLOSED"

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

do_install:append() {
    find "${D}${FW_QCOM_PATH}" -name "a540_gpmu.fw2*" -exec mv {} "${D}${FW_QCOM_BASE_PATH}/" \;
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
