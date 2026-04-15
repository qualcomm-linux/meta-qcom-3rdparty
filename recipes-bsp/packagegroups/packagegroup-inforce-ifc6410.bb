SUMMARY = "Packages for the IFC6410 board"

inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', 'linux-firmware-qcom-adreno-a3xx', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath6k firmware-ath6kl', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-ar3k', '', d)} \
    linux-firmware-qcom-apq8064-dsps \
    linux-firmware-qcom-apq8064-gss \
    linux-firmware-qcom-apq8064-q6 \
    linux-firmware-qcom-apq8064-wifi \
"
