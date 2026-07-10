# Specify location of the corresponding dspso.bin file by adding
# DSPSO_URI:pn-hexagon-dspso-lantronix-openq-835-hdk = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "Hexagon DSP binaries for Lantronix Open-Q 835 HDK (aka HDK835) board"

DSPSO_SOC = "apq8098"
DSPSO_VENDOR = "Lantronix"
DSPSO_DEVICE = "OpenQ835-HDK"
DSPSO_DEVICE_MODEL = "Lantronix Open-Q 835 HDK"

NHLOS_LICENSE = "Intrinsyc_license.txt"
NHLOS_LICENSE_MD5 = "79c13010406436e6ca03672f8727f0da"

DEPENDS = "firmware-${DSP_PKG_NAME}"
S = "${UNPACKDIR}"

DSPSO_PACKAGES = " \
    hexagon-dsp-binaries-${DSP_PKG_NAME}-adsp \
"

require recipes-bsp/hexagon-dspso/hexagon-dspso.inc

# The txt file was generated from the RTF. Provide the original.
python() {
    if d.getVar("NHLOS_URI") or d.getVar("PROPRIETARY_URI"):
        d.appendVar('SRC_URI', ' file://Intrinsyc_license.rtf')
}

do_install:append() {
    if [ -n "${NHLOS_URI}${PROPRIETARY_URI}" ] ; then
        install -d ${D}${datadir}/${BPN}
        install -m 0644 Intrinsyc_license.rtf ${D}${datadir}/${BPN}
    fi
}

# Only ADSP, there are no subdirs in the image
handle_dspso_image() {
    mkdir -p ${B}/dspso-adsp
    debugfs $1 -R "ls -p /" | \
        grep '^/[0-9]*/100' | cut -d/ -f6 | \
        while read name ; do echo "dump /$name ${B}/dspso-adsp/$name" ; done | \
        debugfs ${1}
}

SKIP_FILEDEPS:hexagon-dsp-binaries-${DSP_PKG_NAME}-adsp = "1"
