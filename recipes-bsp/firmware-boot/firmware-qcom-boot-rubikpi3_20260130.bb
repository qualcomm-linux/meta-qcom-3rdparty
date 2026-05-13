SUMMARY = "Boot firmware for Thundercomm RUBIK Pi 3"
DESCRIPTION = "\
Fetches the upstream Rubik Pi 3 boot assets (rubikpi-ai/boot-assets) which \
contain the Qualcomm-signed SoC firmware (XBL, UEFI, TZ, HYP, AOP, devcfg, \
qupv3fw, cpucp, etc.) and the Rubik Pi 3-specific LUN 6 payloads \
(rubikpi_config, devcfg_full, rubikpi_dtso). The matching rawprogram/patch/ \
gpt files come from qcom-partition-conf (which builds the layout via \
qcom-ptool); see platforms/qcs6490-thundercomm-rubikpi3/ufs/ there."

LICENSE = "CLOSED"

# Pinned to rubikpi-ai/boot-assets main branch (2026-01-30) — bump as needed.
SRC_URI = "git://github.com/rubikpi-ai/boot-assets;protocol=https;branch=main"
SRCREV = "83b8358548ecf1e4dc531746ee855a3054af2114"

INHIBIT_DEFAULT_DEPS = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

inherit allarch deploy

QCOM_BOOT_IMG_SUBDIR = "rubikpi3"

COMPATIBLE_MACHINE = "(qcs6490-thundercomm-rubikpi3)"

do_deploy() {
    install -d ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}

    # Signed Qualcomm SoC firmware + Rubik Pi 3-specific .img payloads.
    for ext in elf mbn fv img; do
        find "${S}" -maxdepth 1 -name "*.${ext}" \
            -exec install -m 0644 {} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/ \;
    done

    # Board CDT (boot-assets ships it; qcomflash class reads it as cdt.bin
    # via QCOM_CDT_FILE = "RubikPi3_CDT").
    install -m 0644 "${S}/RubikPi3_CDT.bin" ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/
}
addtask deploy before do_build after do_install
