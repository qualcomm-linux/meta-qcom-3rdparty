FILESEXTRAPATHS:prepend:radxa-dragon-q6a := "${THISDIR}/radxa-dragon-q6a:"

SRC_URI:append:radxa-dragon-q6a = " \
			file://realtek-eth-8169.cfg \
"

FILESEXTRAPATHS:prepend:rubikpi3 := "${THISDIR}/rubikpi3:"

SRC_URI:append:rubikpi3 = " \
    file://0001-arm64-dts-qcom-qcs6490-rubikpi3-add-3.3-V-output-supply.patch \
"
