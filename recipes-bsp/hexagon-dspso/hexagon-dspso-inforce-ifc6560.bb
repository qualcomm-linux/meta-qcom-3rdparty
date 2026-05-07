# Specify location of the corresponding dspso.bin file by adding
# DSPSO_URI:pn-hexagon-dspso-inforce-ifc6560 = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "Hexagon DSP binaries for Inforce IFC6560 board"

DSPSO_SOC = "sda660"
DSPSO_VENDOR = "Inforce"
DSPSO_DEVICE = "IFC6560"
DSPSO_DEVICE_MODEL = "Inforce 6560 Single Board Computer"

LICENSE = "CLOSED"
DEPENDS = "firmware-${DSP_PKG_NAME}"
S = "${UNPACKDIR}"

require recipes-bsp/hexagon-dspso/hexagon-dspso.inc

SKIP_FILEDEPS:hexagon-dsp-binaries-${DSP_PKG_NAME}-adsp = "1"
