FILESEXTRAPATHS:prepend:radxa-dragon-q6a := "${THISDIR}/radxa-dragon-q6a:"

SRC_URI:append:radxa-dragon-q6a = " \
			file://realtek-eth-8169.cfg \
			file://drm-simple-bridge.cfg \
			file://0001-driver-core-seed-deferred-probe-timeout-from-kconfig.patch \
			file://deferred-probe-timeout.cfg \
"

FILESEXTRAPATHS:prepend:rubikpi3 := "${THISDIR}/rubikpi3:"

SRC_URI:append:rubikpi3 = " \
    file://0002-arm64-dts-qcom-add-rubikpi3-camx-overlay.patch \
"
