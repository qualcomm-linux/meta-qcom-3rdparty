# Handle dspso.bin unpacking in a generic way
# If DSPSO_URI is defined, the image will be dissected automatically

DSPSO_URI ??= ""

DSPSO_SOC ?= "unknown"
DSPSO_VENDOR ?= "Qualcomm"
DSPSO_DEVICE ?= "Unknown"

DSPSO_PACKAGES ?= " \
    hexagon-dsp-binaries-${DSP_PKG_NAME}-adsp \
    hexagon-dsp-binaries-${DSP_PKG_NAME}-cdsp \
    hexagon-dsp-binaries-${DSP_PKG_NAME}-sdsp \
"

# Conditionally populate SRC_URI. We have to do it here rather than in python
# script to let base.bbclass to pick up dependencies
SRC_URI += "${DSPSO_URI}"

# Extract package name part
DSP_PKG_NAME ?= "${@d.getVar("PN").split("hexagon-dspso-")[-1]}"

DSP_QCOM_BASE_PATH = "${datadir}/qcom"
DSP_QCOM_CONF_D_PATH = "${DSP_QCOM_BASE_PATH}/conf.d"
DSP_QCOM_SUBPATH = "${DSPSO_SOC}/${DSPSO_VENDOR}/${DSPSO_DEVICE}"
DSP_QCOM_PATH = "${DSP_QCOM_BASE_PATH}/${DSP_QCOM_SUBPATH}"

DSPSO_CONFIG ?= "hexagon-dsp-binaries-${DSP_PKG_NAME}-config"

DEPENDS += "firmware-${DSP_PKG_NAME} e2fsprogs-native"

S = "${UNPACKDIR}"

inherit allarch

PACKAGES = " \
    hexagon-dsp-binaries-${DSP_PKG_NAME}-config \
    hexagon-dsp-binaries-${DSP_PKG_NAME}-license \
    ${DSPSO_PACKAGES} \
"

LICENSE:append = " AND MIT"
LICENSE:hexagon-dsp-binaries-${DSP_PKG_NAME}-config = "MIT"

RDEPENDS:hexagon-dsp-binaries-${DSP_PKG_NAME}-adsp = "${DSPSO_CONFIG} hexagon-dsp-binaries-${DSP_PKG_NAME}-license"
RDEPENDS:hexagon-dsp-binaries-${DSP_PKG_NAME}-cdsp = "${DSPSO_CONFIG} hexagon-dsp-binaries-${DSP_PKG_NAME}-license"
RDEPENDS:hexagon-dsp-binaries-${DSP_PKG_NAME}-sdsp = "${DSPSO_CONFIG} hexagon-dsp-binaries-${DSP_PKG_NAME}-license"

FILES:hexagon-dsp-binaries-${DSP_PKG_NAME}-config = "${DSP_QCOM_CONF_D_PATH}"
FILES:hexagon-dsp-binaries-${DSP_PKG_NAME}-license = "${datadir}/${BPN}"

FILES:hexagon-dsp-binaries-${DSP_PKG_NAME}-adsp = "${DSP_QCOM_PATH}/dsp/adsp"
FILES:hexagon-dsp-binaries-${DSP_PKG_NAME}-cdsp = "${DSP_QCOM_PATH}/dsp/cdsp"
FILES:hexagon-dsp-binaries-${DSP_PKG_NAME}-sdsp = "${DSP_QCOM_PATH}/dsp/sdsp"

INSANE_SKIP:hexagon-dsp-binaries-${DSP_PKG_NAME}-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:hexagon-dsp-binaries-${DSP_PKG_NAME}-cdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:hexagon-dsp-binaries-${DSP_PKG_NAME}-sdsp = "arch libdir file-rdeps textrel"

# Don't pick up dependencies like Hexagon libc.so or libgcc.so
SKIP_FILEDEPS:hexagon-dsp-binaries-${DSP_PKG_NAME}-adsp = "1"
SKIP_FILEDEPS:hexagon-dsp-binaries-${DSP_PKG_NAME}-cdsp = "1"
SKIP_FILEDEPS:hexagon-dsp-binaries-${DSP_PKG_NAME}-sdsp = "1"

# If the URL is the file:// URI, the whole local path will be duplicated in the UNPACKDIR.
# Otherwise we just need the last (filename) part of the path.
def get_dspso_path(path):
    from urllib.parse import urlparse
    if path == "":
        return ""
    url = urlparse(path)
    if url.scheme == "file":
        return url.path
    return url.path.rsplit('/', 1)[1]

handle_dspso_image() {
    # Old images, only ADSP
    if debugfs $1 -R "ls -l" | grep -q fastrpc_ ; then
        mkdir -p ${B}/dspso-adsp
        debugfs $1 -R "ls -p /" | \
            grep '^/[0-9]*/100' | cut -d/ -f6 | \
            while read name ; do echo "dump /$name ${B}/dspso-adsp/$name" ; done | \
            debugfs ${1}
    else
        for path in adsp cdsp sdsp ; do
            case "${PACKAGES}" in
                *${path}*)
                    mkdir -p ${B}/dspso-$path
                    debugfs $1 -R "ls -p /$path" | \
                        grep '^/[0-9]*/100' | cut -d/ -f6 | \
                        while read name ; do echo "dump /$path/$name ${B}/dspso-$path/$name" ; done | \
                        debugfs ${1}
                ;;
            esac
        done
    fi
}

generate_config() {
    if [ -n "${DSPSO_DEVICE_MODEL}" ] ; then
        cat > ${B}/${BPN}.yaml << EOF
machines:
  ${DSPSO_DEVICE_MODEL}:
    DSP_LIBRARY_PATH: ${DSP_QCOM_SUBPATH}
EOF
    fi
}

do_compile() {
    if [ -z "${DSPSO_URI}" ] ; then
        bbwarn "${PN}: not packaging DSPSO firmware. Please provide DSPSO_URI"
    else
        handle_dspso_image ${UNPACKDIR}/${@get_dspso_path(d.getVar("DSPSO_URI"))}
    fi
    generate_config
}

do_install() {
    if [ -n "${DSPSO_URI}" ] ; then
        install -d ${D}${datadir}/${BPN}
        install_license
    fi
    if [ -f "${B}/${BPN}.yaml" ] ; then
        install -d ${D}${DSP_QCOM_CONF_D_PATH}
        install -m 0644 ${B}/${BPN}.yaml ${D}${DSP_QCOM_CONF_D_PATH}
    fi
    if ls dspso-adsp/* ; then mkdir -p ${D}${DSP_QCOM_PATH}/dsp/adsp && install -m 0644 ${B}/dspso-adsp/* ${D}${DSP_QCOM_PATH}/dsp/adsp ; fi
    if ls dspso-cdsp/* ; then mkdir -p ${D}${DSP_QCOM_PATH}/dsp/cdsp && install -m 0644 ${B}/dspso-cdsp/* ${D}${DSP_QCOM_PATH}/dsp/cdsp ; fi
    if ls dspso-sdsp/* ; then mkdir -p ${D}${DSP_QCOM_PATH}/dsp/sdsp && install -m 0644 ${B}/dspso-sdsp/* ${D}${DSP_QCOM_PATH}/dsp/sdsp ; fi
}

# Make sure that there is nothing in the meta-package, all firmware must go to
# a particular package.
FILES:${PN} = ""
