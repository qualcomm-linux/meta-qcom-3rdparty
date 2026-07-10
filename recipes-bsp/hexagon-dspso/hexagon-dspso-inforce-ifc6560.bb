# Specify location of the corresponding dspso.bin file by adding
# DSPSO_URI:pn-hexagon-dspso-inforce-ifc6560 = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "Hexagon DSP binaries for Inforce IFC6560 board"

DSPSO_SOC = "sda660"
DSPSO_VENDOR = "Inforce"
DSPSO_DEVICE = "IFC6560"
DSPSO_DEVICE_MODEL = "Inforce 6560 Single Board Computer"

NHLOS_LICENSE = "Inforce_Soft_are_Click_Through_License.txt"
NHLOS_LICENSE_MD5 = "cbddec68783c9e6e0995fb21c958cf11"

DEPENDS = "firmware-${DSP_PKG_NAME}"
S = "${UNPACKDIR}"

require recipes-bsp/hexagon-dspso/hexagon-dspso.inc

# The txt file was generated from the PDF. Provide the original.
python() {
    if d.getVar("DSPSO_URI"):
        d.appendVar('SRC_URI', ' file://Inforce_Soft_are_Click_Through_License.pdf')
}

do_install:append() {
    if [ -n "${DSPSO_URI}" ] ; then
        install -d ${D}${datadir}/${BPN}
        install -m 0644 Inforce_Soft_are_Click_Through_License.pdf ${D}${datadir}/${BPN}
    fi
}

SKIP_FILEDEPS:hexagon-dsp-binaries-${DSP_PKG_NAME}-adsp = "1"
