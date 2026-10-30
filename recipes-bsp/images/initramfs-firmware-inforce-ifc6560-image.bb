DESCRIPTION = "Tiny ramdisk image with Inforce IFC6560 devices firmware files"

PACKAGE_INSTALL += " \
    packagegroup-inforce-ifc6560-firmware \
"

require recipes-bsp/images/initramfs-firmware-image.inc
