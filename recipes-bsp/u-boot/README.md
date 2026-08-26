<!-- Generated from recipes-bsp/u-boot/ at wrynose @ c811139d (2026-07-09) by docsgen on 2026-08-25. Manual edits will be overwritten on regeneration; change the source, not this page. -->

# recipes-bsp/u-boot/

The U-Boot recipe for the Arduino UNO Q, built from source and repackaged as an Android-style boot image the Qualcomm image machinery can flash.

## Contents

| File | Description | Machine | Reference |
| --- | --- | --- | --- |
| [recipes-bsp/u-boot/u-boot-arduino_git.bb](u-boot-arduino_git.bb) | Recipe for `u-boot-arduino`. | [`uno-q`](../../conf/machine/uno-q.md) | [u-boot-arduino recipe](u-boot-arduino.md) |

## Description

The recipe fetches a pinned revision of a board-specific U-Boot fork over git and layers two `require` files on top, `u-boot-common.inc` and `u-boot.inc`, for the generic fetch, compile and deploy machinery. Neither is defined in this layer (external); another layer in the build provides them, resolved through `BBPATH`, consistent with the OpenEmbedded-Core U-Boot includes of the same names.

The file is a full recipe rather than a `.bbappend` over an existing one, and it declares `PROVIDES += "u-boot"`, so a machine's `PREFERRED_PROVIDER_virtual/bootloader` can select it as the bootloader implementation. That binding is made by name in [`uno-q`](../../conf/machine/uno-q.md).

Build-stage customization is confined to two function overrides. An append to the compile step post-processes the compiled binary into a gzip-wrapped, `mkbootimg`-packaged Android-style boot image through the `skales` tools, gated on the device tree blob having been deployed by the kernel recipe; an append to the deploy step, scoped to the `qcom` override, symlinks the packaged binary to a machine-named boot image so the Qualcomm image machinery picks it up. The sibling `meta-qcom` layer's own generic U-Boot bbappend performs a near-identical sequence, which makes chainloading U-Boot from an Android boot-image format a recognized idiom across this layer family.
