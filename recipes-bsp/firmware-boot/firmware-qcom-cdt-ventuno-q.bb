DESCRIPTION = "CDT (Configuration Data Table) Firmware for Arduino VENTUNO Q platform"

SRC_URI = " \
    https://${CDT_ARTIFACTORY}/QCS8300/cdt/qcs8275-Monza_v1.zip;downloadfilename=cdt-qcs8275-monza_${PV}.zip;name=cdt-qcs8275-monza \
    "
SRC_URI[cdt-qcs8275-monza.sha256sum] = "8484beb2ac1ff74a129c586f6d5331536765975dbabfb0600509f11010ec1c41"

QCOM_CDT_SUBDIR = "8300"

include recipes-bsp/firmware-boot/firmware-qcom-cdt-common.inc
