# Specify location of the corresponding dspso.bin file by adding
# DSPSO_URI:pn-hexagon-dspso-lantronix-openq-835-hdk = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "Hexagon DSP binaries for Lantronix Open-Q 835 HDK (aka HDK835) board"

DSPSO_SOC = "apq8098"
DSPSO_VENDOR = "Lantronix"
DSPSO_DEVICE = "OpenQ835-HDK"
DSPSO_DEVICE_MODEL = "Lantronix Open-Q 835 HDK"

DSPSO_PACKAGES = " \
    hexagon-dsp-binaries-${DSP_PKG_NAME}-adsp \
"

LICENSE = "LicenseRef-NHLOS-lantronix-openq-835-hdk"
LIC_FILES_CHKSUM = "file://Intrinsyc_license.txt;md5=79c13010406436e6ca03672f8727f0da"
NO_GENERIC_LICENSE[NHLOS-lantronix-openq-835-hdk] = "Intrinsyc_license.txt"

# The txt file was generated from the RTF. Provide the original.
SRC_URI = " \
    file://Intrinsyc_license.txt \
    file://Intrinsyc_license.rtf \
"

install_license() {
    install -m 0644 Intrinsyc_license.txt ${D}${datadir}/${BPN}
    install -m 0644 Intrinsyc_license.rtf ${D}${datadir}/${BPN}
}

require recipes-bsp/hexagon-dspso/hexagon-dspso.inc
