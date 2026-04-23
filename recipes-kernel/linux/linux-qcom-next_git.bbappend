# tag: qcom-next-7.0-rc2-20260306
LINUX_VERSION:uno-q = "6.19+7.0-rc2"
SRCREV:uno-q = "a656209cfb5a49f301c377aa8455a10f83a4a719"

FILESEXTRAPATHS:prepend:radxa-dragon-q6a := "${THISDIR}/radxa-dragon-q6a:"

SRC_URI:append:radxa-dragon-q6a = " \
			file://realtek-eth-8169.cfg \
"
