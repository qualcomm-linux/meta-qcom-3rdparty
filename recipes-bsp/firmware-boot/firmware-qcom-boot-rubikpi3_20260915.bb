SUMMARY = "Boot firmware for Thundercomm RUBIK Pi 3"
DESCRIPTION = "Qualcomm-signed SoC boot firmware and Rubik Pi 3-specific LUN 6 \
payloads from rubikpi-ai/boot-assets."

LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=165287851294f2fb8ac8cbc5e24b02b0"

SRC_URI = "git://github.com/rubikpi-ai/boot-assets;protocol=https;branch=qli2.0;destsuffix=${BP}"
SRCREV = "eaf0c648cb792c50606d23b97b83117bb536f051"

INHIBIT_DEFAULT_DEPS = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

inherit allarch deploy

QCOM_BOOT_IMG_SUBDIR = "rubikpi3"

COMPATIBLE_MACHINE = "(rubikpi3)"

do_deploy() {
    install -d ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}

    for ext in elf mbn fv img; do
        find "${S}" -maxdepth 1 -name "*.${ext}" \
            -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/ \;
    done

    # qcomflash consumes the CDT as cdt.bin via QCOM_CDT_FILE.
    install -m 0644 "${S}/RubikPi3_CDT.bin" ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/
}
addtask deploy before do_build after do_install
