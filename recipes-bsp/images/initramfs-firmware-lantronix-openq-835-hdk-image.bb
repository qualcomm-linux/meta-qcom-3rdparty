DESCRIPTION = "Tiny ramdisk image with Lantronix Open-Q 835 HDK devices firmware files"

PACKAGE_INSTALL += " \
    packagegroup-lantronix-openq-835-hdk-firmware \
"

require recipes-bsp/images/initramfs-firmware-image.inc
