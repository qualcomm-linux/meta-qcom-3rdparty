SUMMARY = "Packages for the IFC6560 board"

inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
    ${PN}-hexagon-dsp-binaries \
"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', 'linux-firmware-qcom-adreno-a530 linux-firmware-qcom-sda660-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-wcn399x', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath10k-wcn3990 linux-firmware-qcom-sda660-wifi', '', d)} \
    linux-firmware-qcom-sda660-audio \
    linux-firmware-qcom-sda660-compute \
    linux-firmware-qcom-sda660-modem \
    linux-firmware-qcom-sda660-venus \
"

RDEPENDS:${PN}-hexagon-dsp-binaries = " \
    hexagon-dsp-binaries-inforce-ifc6560-adsp \
    hexagon-dsp-binaries-inforce-ifc6560-cdsp \
"
