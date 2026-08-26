<!-- Generated from recipes-bsp/packagegroups/ at wrynose @ c811139d (2026-07-09) by docsgen on 2026-08-25. Manual edits will be overwritten on regeneration; change the source, not this page. -->

# recipes-bsp/packagegroups/

One packagegroup recipe per board, each pulling that board's firmware and Hexagon DSP binary packages into an image under a single installable name.

## Contents

| File | Description | Machine | Reference |
| --- | --- | --- | --- |
| [recipes-bsp/packagegroups/packagegroup-radxa-dragon-q6a.bb](packagegroup-radxa-dragon-q6a.bb) | Packages for the Radxa Dragon Q6A platform. | Not restricted by this file | [packagegroup-radxa-dragon-q6a recipe](packagegroup-radxa-dragon-q6a.md) |
| [recipes-bsp/packagegroups/packagegroup-uno-q.bb](packagegroup-uno-q.bb) | Packages for the Arduino UNO-Q platform. | Not restricted by this file | [packagegroup-uno-q recipe](packagegroup-uno-q.md) |

## Description

A packagegroup is a dependency-only metapackage, built by `inherit packagegroup`, whose whole content is a curated list of runtime packages. Each recipe here defines two sub-packages, `${PN}-firmware` and `${PN}-hexagon-dsp-binaries`, matching the convention the sibling `meta-qcom` layer's own packagegroup recipes follow.

A `-firmware` sub-package splits into a conditional half and an unconditional one. GPU, Wi-Fi and Bluetooth firmware is gated on `DISTRO_FEATURES` through `bb.utils.contains` and `bb.utils.contains_any` expressions, on `opencl`/`opengl`/`vulkan`, `wifi` and `bluetooth` respectively, so it is installed in builds whose distro carries the matching feature. Everything else the sub-package names is recommended unconditionally: audio, modem, camera, display-bridge, compute, QUPv3 and video-codec firmware, according to the board. The `-hexagon-dsp-binaries` sub-packages are unconditional throughout, depending on the board's ADSP binaries, and its CDSP binaries where the recipe names them.

Machine restriction happens at the point of consumption rather than inside these recipes: each board's machine configuration pulls its matching packagegroup in by name through `MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS`, which is how the metapackage reaches a built image. The packagegroup for [`radxa-dragon-q6a`](../../conf/machine/radxa-dragon-q6a.md) covers runtime firmware and Hexagon DSP binaries; that board's boot chain is vendor-managed and provisioned outside the build.
