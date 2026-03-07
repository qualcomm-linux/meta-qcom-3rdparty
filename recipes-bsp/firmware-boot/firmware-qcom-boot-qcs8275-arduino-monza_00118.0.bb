DESCRIPTION = "QCOM NHLOS Firmware for Arduino VENTUNO Q"
LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/LICENSE.${BPN};md5=6e1bae7ef13289c332a27b917fb49764"

FW_ARTIFACTORY = "softwarecenter.qualcomm.com/nexus/generic/product/chip/software-product-family/Qualcomm_Linux.SPF.1.0/qualcomm_linux.spf.1.0-test-device-public"
FW_BUILD_ID = "r1.0_${PV}/QCS8300.LE.1.0"
FW_BIN_PATH = "common/build/ufs/bin"
BOOTBINARIES = "QCS8300_bootbinaries"

SRC_URI = " \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/${FW_BIN_PATH}/${BOOTBINARIES}.zip;downloadfilename=${BOOTBINARIES}_r1.0_${PV}.zip;name=bootbinaries \
    https://${FW_ARTIFACTORY}/${FW_BUILD_ID}/LICENSE.txt;downloadfilename=LICENSE.${BPN};name=license \
    https://artifacts.codelinaro.org/artifactory/codelinaro-le/Qualcomm_Linux/QCS8300/cdt/qcs8275-Monza_v1.zip;downloadfilename=cdt-qcs8275-monza_${PV}.zip;name=cdt-qcs8275-monza \
    "
SRC_URI[bootbinaries.sha256sum] = "e07ed2b58c639373e22c289873f1059a4ba48cfe41bd9ae2f9651884177b60c9"
SRC_URI[cdt-qcs8275-monza.sha256sum] = "8484beb2ac1ff74a129c586f6d5331536765975dbabfb0600509f11010ec1c41"
SRC_URI[license.sha256sum] = "3ad8f1fd82f2918c858cec2d0887b7df6f71a06416beecfdb3efe7d62874d863"

QCOM_BOOT_IMG_SUBDIR = "qcs8275-arduino-monza"

include recipes-bsp/firmware-boot/firmware-qcom-boot-common.inc

do_deploy:append() {
    if [ -d "${UNPACKDIR}/${BOOTBINARIES}/sail_nor" ]; then
        SAIL_FILES="gpt_backup0.bin gpt_main0.bin prog_firehose_ddr.elf patch0.xml rawprogram0.xml sailhyp.elf sailsw1.elf"

        install -d ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/sail_nor
        for sail_file in ${SAIL_FILES}; do
            install -m 0644 ${UNPACKDIR}/${BOOTBINARIES}/sail_nor/${sail_file} ${DEPLOYDIR}/${QCOM_BOOT_IMG_SUBDIR}/sail_nor
        done
    fi
}
