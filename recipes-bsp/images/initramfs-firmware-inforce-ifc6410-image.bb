DESCRIPTION = "Tiny ramdisk image with Inforce IFC6410 firmware files"

PACKAGE_INSTALL += " \
    packagegroup-inforce-ifc6410-firmware \
"

require recipes-bsp/images/initramfs-firmware-image.inc
