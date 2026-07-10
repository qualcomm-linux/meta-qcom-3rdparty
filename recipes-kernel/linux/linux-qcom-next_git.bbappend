FILESEXTRAPATHS:prepend:radxa-dragon-q6a := "${THISDIR}/radxa-dragon-q6a:"

SRC_URI:append:radxa-dragon-q6a = " \
			file://realtek-eth-8169.cfg \
			file://drm-simple-bridge.cfg \
			file://0001-driver-core-seed-deferred-probe-timeout-from-kconfig.patch \
			file://deferred-probe-timeout.cfg \
"
