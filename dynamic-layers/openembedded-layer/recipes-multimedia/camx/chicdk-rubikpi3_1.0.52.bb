SUMMARY = "Additional RUBIK Pi 3 camera modules for CHI 1.0.52"
DESCRIPTION = "IMX219 and IMX477 sensor libraries, connector-specific module \
configuration and tuning for RUBIK Pi 3 camera modules."

LICENSE = "LicenseRef-LICENSE.qcom-2"
LIC_FILES_CHKSUM = " \
    file://usr/share/doc/chicdk-kodiak/LICENSE.QCOM-2.txt;md5=165287851294f2fb8ac8cbc5e24b02b0 \
    file://usr/share/doc/chicdk-kodiak/NOTICE;md5=9c80f7af9f8aeb34ef0beff70a22e3d3 \
    file://usr/share/doc/chicdk-kodiak/SENSOR-NOTICES.txt;md5=b45aa1c04de1b0187423fdda7adeb793 \
    file://usr/share/doc/chicdk-kodiak/TUNING-NOTICES.txt;md5=0571e35492d99faf4b9c27037fdbdab7 \
"
NO_GENERIC_LICENSE[LicenseRef-LICENSE.qcom-2] = "usr/share/doc/chicdk-kodiak/LICENSE.QCOM-2.txt"

SRC_URI = "https://raw.githubusercontent.com/rubikpi-ai/prebuilt/15e85de40c94a508db8e320f1d144d4e93f31c8a/chicdk-kodiak_1.0.52_armv8-2a.tar.xz"
SRC_URI[sha256sum] = "c3584696228ec7629c4bd7c32c396b167ecedbb77bf37975a39c8148fe8cf5a2"

S = "${UNPACKDIR}"

COMPATIBLE_HOST = "aarch64.*-linux"

DEPENDS = "glib-2.0"
RDEPENDS:${PN} = "chicdk-kodiak"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${libdir}/camx/kodiak/camera

    # Add board assets alongside the official CHI runtime and sensor map.
    for sensor in imx219 imx477; do
        install -m 0755 \
            ${S}/usr/lib/camx/kodiak/camera/com.qti.sensor.$sensor.so.0.1.0 \
            ${D}${libdir}/camx/kodiak/camera/
        ln -s com.qti.sensor.$sensor.so.0.1.0 \
            ${D}${libdir}/camx/kodiak/camera/com.qti.sensor.$sensor.so.0

        install -m 0644 \
            ${S}/usr/lib/camx/kodiak/camera/com.qti.tuned.$sensor.bin \
            ${D}${libdir}/camx/kodiak/camera/
    done

    for module in cam1_imx219 cam1_imx477 cam2_imx219 cam2_imx477; do
        install -m 0644 \
            ${S}/usr/lib/camx/kodiak/camera/com.qti.sensormodule.$module.bin \
            ${D}${libdir}/camx/kodiak/camera/
    done

    install -d ${D}${datadir}/doc/${BPN}
    install -m 0644 \
        ${S}/usr/share/doc/chicdk-kodiak/LICENSE.QCOM-2.txt \
        ${S}/usr/share/doc/chicdk-kodiak/NOTICE \
        ${S}/usr/share/doc/chicdk-kodiak/SENSOR-NOTICES.txt \
        ${S}/usr/share/doc/chicdk-kodiak/TUNING-NOTICES.txt \
        ${D}${datadir}/doc/${BPN}/
}

FILES:${PN} = " \
    ${libdir}/camx/kodiak/camera/com.qti.sensor.imx219.so.0 \
    ${libdir}/camx/kodiak/camera/com.qti.sensor.imx219.so.0.1.0 \
    ${libdir}/camx/kodiak/camera/com.qti.sensor.imx477.so.0 \
    ${libdir}/camx/kodiak/camera/com.qti.sensor.imx477.so.0.1.0 \
    ${libdir}/camx/kodiak/camera/com.qti.tuned.imx219.bin \
    ${libdir}/camx/kodiak/camera/com.qti.tuned.imx477.bin \
    ${libdir}/camx/kodiak/camera/com.qti.sensormodule.cam1_imx219.bin \
    ${libdir}/camx/kodiak/camera/com.qti.sensormodule.cam1_imx477.bin \
    ${libdir}/camx/kodiak/camera/com.qti.sensormodule.cam2_imx219.bin \
    ${libdir}/camx/kodiak/camera/com.qti.sensormodule.cam2_imx477.bin \
    ${datadir}/doc/${BPN}/LICENSE.QCOM-2.txt \
    ${datadir}/doc/${BPN}/NOTICE \
    ${datadir}/doc/${BPN}/SENSOR-NOTICES.txt \
    ${datadir}/doc/${BPN}/TUNING-NOTICES.txt \
"

# Keep the mandatory vendor terms and notices with the camera assets.
FILES:${PN}-doc = ""

INSANE_SKIP:${PN} += "already-stripped"
