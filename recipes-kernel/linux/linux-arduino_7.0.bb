DESCRIPTION = "Linux ${PV} kernel for QCOM-based Arduino devices"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
SECTION = "kernel"

inherit kernel cml1

COMPATIBLE_MACHINE = "(uno-q)"

LINUX_VERSION ?= "7.0"

PV = "${LINUX_VERSION}"

SRCREV ?= "122c2c22d838ca826e7f4e7360df96fb4e8f7ad2"
SRCBRANCH ?= "nobranch=1"

SRC_URI = "\
    git://github.com/arduino/linux-qcom.git;${SRCBRANCH};protocol=https \
    file://0001-arm64-dts-qcom-Add-qrb2210-arduino-imola-usbhost-DTB.patch \
    file://configs/arduino.cfg \
"

S = "${UNPACKDIR}/${BP}"

KBUILD_DEFCONFIG ?= "defconfig"

do_configure:prepend() {
    # Use a copy of the 'defconfig' from the actual repo to merge fragments
    cp ${S}/arch/${ARCH}/configs/${KBUILD_DEFCONFIG} ${B}/.config

    # Merge fragment for QCOM value add features
    ${S}/scripts/kconfig/merge_config.sh -m -O ${B} ${B}/.config ${@" ".join(find_cfgs(d))}
}
