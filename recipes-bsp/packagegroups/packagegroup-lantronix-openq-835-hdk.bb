SUMMARY = "Packages for the Open-Q 835 HDK board"

inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
    ${PN}-hexagon-dsp-binaries \
"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl', 'linux-firmware-qcom-adreno-a540 linux-firmware-qcom-apq8098-adreno', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-wcn399x', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath10k-wcn3990 linux-firmware-qcom-apq8098-wifi', '', d)} \
    linux-firmware-qcom-apq8098-audio \
    linux-firmware-qcom-apq8098-ipa \
    linux-firmware-qcom-apq8098-modem \
    linux-firmware-qcom-apq8098-sensors \
    linux-firmware-qcom-apq8098-venus \
"

RDEPENDS:${PN}-hexagon-dsp-binaries = " \
    hexagon-dsp-binaries-lantronix-openq-835-hdk-adsp \
"
