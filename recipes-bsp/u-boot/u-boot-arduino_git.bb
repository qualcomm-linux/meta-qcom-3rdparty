require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

DEPENDS += "bc-native dtc-native gnutls-native python3-pyelftools-native skales-native xxd-native"

SRC_URI = "git://github.com/arduino/u-boot.git;branch=${SRCBRANCH};protocol=https"
SRCBRANCH = "qcom-mainline"
SRCREV = "8008ca96a4dc53ddb3e51b96ea7e86d881ab7969"

PV = "2025.10+2026.01-rc3+git"

PROVIDES += "u-boot"

COMPATIBLE_MACHINE = "(uno-q)"

uboot_compile_config:append() {
    cd ${B}/${builddir}
    touch empty-file
    rm -f u-boot-nodtb.bin.gz
    gzip -k u-boot-nodtb.bin
    cat u-boot-nodtb.bin.gz ${DEPLOY_DIR_IMAGE}/${type}.dtb > u-boot-nodtb.bin.gz-${type}
    ${STAGING_BINDIR_NATIVE}/skales/mkbootimg --base 0x80000000 --pagesize 4096 --kernel u-boot-nodtb.bin.gz-${type} --cmdline "root=/dev/notreal" --ramdisk empty-file --output u-boot-${type}.bin
}
do_compile[depends] += "virtual/kernel:do_deploy"

# Symlink the 'main' u-boot.bin to boot.img so the qcom image bbclass pick it up
uboot_deploy_config:append:qcom() {
    cd ${DEPLOYDIR} && ln -sf u-boot-${type}-${PV}-${PR}.bin boot-${MACHINE}.img
}
