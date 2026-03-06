DESCRIPTION = "Linux ${PV} kernel for QCOM-based Arduino devices"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
SECTION = "kernel"

inherit kernel cml1

COMPATIBLE_MACHINE = "(uno-q)"

LINUX_VERSION ?= "6.16.7"

PV = "${LINUX_VERSION}"

SRCREV ?= "0dd6551ae96b78024086e72339fefbef6fcc604b"
SRCBRANCH ?= "nobranch=1"

SRC_URI = "\
    git://github.com/arduino/linux-qcom.git;${SRCBRANCH};protocol=https \
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
