# Specify location of the corresponding dspso.bin file by adding
# DSPSO_URI:pn-hexagon-dspso-inforce-ifc6560 = "..."  to local.conf. Use "file://"
# if the file is provided locally.

DESCRIPTION = "Hexagon DSP binaries for Inforce IFC6560 board"

DSPSO_SOC = "sda660"
DSPSO_VENDOR = "Inforce"
DSPSO_DEVICE = "IFC6560"
DSPSO_DEVICE_MODEL = "Inforce 6560 Single Board Computer"

LICENSE = "LicenseRef-NHLOS-inforce"
LIC_FILES_CHKSUM = "file://Inforce_Soft_are_Click_Through_License.txt;md5=cbddec68783c9e6e0995fb21c958cf11"
NO_GENERIC_LICENSE[NHLOS-inforce] = "Inforce_Soft_are_Click_Through_License.txt"

# The txt file was generated from the PDF. Provide the original.
SRC_URI = " \
    file://Inforce_Soft_are_Click_Through_License.txt \
    file://Inforce_Soft_are_Click_Through_License.pdf \
"

install_license() {
    install -m 0644 Inforce_Soft_are_Click_Through_License.txt ${D}${datadir}/${BPN}
    install -m 0644 Inforce_Soft_are_Click_Through_License.pdf ${D}${datadir}/${BPN}
}

require recipes-bsp/hexagon-dspso/hexagon-dspso.inc
