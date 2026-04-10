FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

LINUX_VERSION:uno-q = "6.19+7.0-rc7"
SRCREV:uno-q = "ce06e26c6ed01ba78e6f717354b8d66887cc6017"
KBUILD_CONFIG_EXTRA:remove:uno-q = "${S}/arch/arm64/configs/prune.config ${S}/arch/arm64/configs/qcom.config"
SRCBRANCH:uno-q = "nobranch=1"
SRCBRANCH:class-devupstream:uno-q = "branch=tech/hwe/unoq"
SRC_URI:remove:uno-q = "git://github.com/qualcomm-linux/kernel.git;${SRCBRANCH};protocol=https"
SRC_URI:append:uno-q = " git://github.com/qualcomm-linux/kernel-topics.git;${SRCBRANCH};protocol=https"
