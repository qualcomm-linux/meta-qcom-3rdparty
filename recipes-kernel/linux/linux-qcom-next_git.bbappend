FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

# tag: qcom-next-7.0-rc2-20260306
LINUX_VERSION:uno-q = "6.19+7.0-rc2"
SRCREV:uno-q = "a656209cfb5a49f301c377aa8455a10f83a4a719"

LINUX_VERSION:ventuno-q = "6.19.5"
SRCREV:ventuno-q = "338ae0cf0cad091c55fc01d075e6a43ea92add33"
KBUILD_CONFIG_EXTRA:remove:ventuno-q = "${S}/arch/arm64/configs/prune.config ${S}/arch/arm64/configs/qcom.config"
SRCBRANCH:ventuno-q = "nobranch=1"
SRCBRANCH:class-devupstream:ventuno-q = "branch=early/hwe/monza"
SRC_URI:remove:ventuno-q = "git://github.com/qualcomm-linux/kernel.git;${SRCBRANCH};protocol=https"
SRC_URI:append:ventuno-q = " git://github.com/qualcomm-linux/kernel-topics.git;${SRCBRANCH};protocol=https file://configs/monza.cfg"
