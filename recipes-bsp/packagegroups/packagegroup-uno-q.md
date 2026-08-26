<!-- Generated from recipes-bsp/packagegroups/packagegroup-uno-q.bb at wrynose @ c811139d (2026-07-09) by docsgen on 2026-08-25. Manual edits will be overwritten on regeneration; change the source, not this page. -->

# packagegroup-uno-q

Packages for the Arduino UNO-Q platform.

| Field | Detail |
| --- | --- |
| Recipe | [recipes-bsp/packagegroups/packagegroup-uno-q.bb](packagegroup-uno-q.bb) |

## Packages

This recipe produces `packagegroup-uno-q-firmware` and `packagegroup-uno-q-hexagon-dsp-binaries` (`${PN}` expands to `packagegroup-uno-q`).

| Variable | Effect |
| --- | --- |
| `PACKAGES =` | The binary packages this recipe produces. This file sets it to `${PN}-firmware` and `${PN}-hexagon-dsp-binaries`. |
| `RRECOMMENDS:${PN}-firmware =` | Runtime recommendations for the named package: installed by default, and removable without breaking the dependency graph. Sets the recommended packages to `${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', 'linux-firmware-qcom-adreno-a702 linux-firmware-qcom-qcm2290-adreno', '', d)}`, `${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'linux-firmware-ath10k-wcn3990 linux-firmware-qcom-qcm2290-wifi ', '', d)}`, `${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', 'linux-firmware-qca-wcn3988', '', d)}`, `linux-firmware-qcom-qcm2290-audio`, `linux-firmware-qcom-qcm2290-modem` and `linux-firmware-qcom-venus-6.0`. |
| `RDEPENDS:${PN}-hexagon-dsp-binaries =` | Runtime dependencies of the named package: they are installed alongside it. This file sets it to `hexagon-dsp-binaries-thundercomm-rb1-adsp`. |

## Compatibility

- `COMPATIBLE_MACHINE` is not set, so this recipe is not restricted to a machine by this file.
- Inherited classes: `packagegroup`, provided outside this layer.
