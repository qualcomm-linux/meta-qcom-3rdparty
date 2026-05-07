# Specify location of the corresponding dspso.bin file by adding
# DSPSO_URI:pn-hexagon-dspso-lantronix-openq-835-hdk = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "Hexagon DSP binaries for Lantronix Open-Q 835 HDK (aka HDK835) board"

DSPSO_SOC = "apq8098"
DSPSO_VENDOR = "Lantronix"
DSPSO_DEVICE = "OpenQ835-HDK"
DSPSO_DEVICE_MODEL = "Lantronix Open-Q 835 HDK"

LICENSE = "CLOSED"
DEPENDS = "firmware-${DSP_PKG_NAME}"
S = "${UNPACKDIR}"

require recipes-bsp/hexagon-dspso/hexagon-dspso.inc

# Only ADSP
handle_dspso_image() {
    mkdir -p ${B}/dspso-adsp
    debugfs $1 -R "ls -p /" | \
        grep '^/[0-9]*/100' | cut -d/ -f6 | \
        while read name ; do echo "dump /$name ${B}/dspso-adsp/$name" ; done | \
        debugfs ${1}
}

PACKAGES = " \
    hexagon-dsp-binaries-${DSP_PKG_NAME}-adsp \
    hexagon-dsp-binaries-${DSP_PKG_NAME}-config \
"

SKIP_FILEDEPS:hexagon-dsp-binaries-${DSP_PKG_NAME}-adsp = "1"
