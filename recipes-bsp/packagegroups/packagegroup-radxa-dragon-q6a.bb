SUMMARY = "Packages for the Radxa Dragon Q6A platform"

inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
    ${PN}-hexagon-dsp-binaries \
"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', 'linux-firmware-qcom-adreno-a660 linux-firmware-qcom-qcm6490-adreno', '', d)} \
    camxfirmware-kodiak \
	linux-firmware-lt9611uxc \
    linux-firmware-qcom-qcs6490-radxa-dragon-q6a-audio \
    linux-firmware-qcom-qcs6490-radxa-dragon-q6a-compute \
    linux-firmware-qcom-qcm6490-qupv3fw \
    linux-firmware-qcom-vpu \
"

RDEPENDS:${PN}-hexagon-dsp-binaries = " \
    hexagon-dsp-binaries-radxa-dragon-q6a-adsp \
    hexagon-dsp-binaries-radxa-dragon-q6a-cdsp \
"
