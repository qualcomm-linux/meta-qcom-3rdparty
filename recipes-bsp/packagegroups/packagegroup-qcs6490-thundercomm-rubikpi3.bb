SUMMARY = "Packages for the Thundercomm RUBIK Pi 3 platform"

inherit packagegroup

PACKAGES = " \
    ${PN}-firmware \
"

# Firmware pulled from upstream linux-firmware. Board-specific signed
# audio/compute DSP firmware is not yet packaged for this board; the system
# boots without it. Wifi (brcmfmac43456) and BT (BCM4345C5) ship in
# linux-firmware upstream.
RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', 'linux-firmware-qcom-adreno-a660 linux-firmware-qcom-qcm6490-adreno', '', d)} \
    linux-firmware-qcom-qcm6490-qupv3fw \
    linux-firmware-qcom-vpu \
    linux-firmware-qcom-qcs6490-thundercomm-rubikpi3-audio \
    linux-firmware-qcom-qcs6490-compute \
"
