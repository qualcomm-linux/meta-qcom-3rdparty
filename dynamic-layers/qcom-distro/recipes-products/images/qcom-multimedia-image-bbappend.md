<!-- Generated from dynamic-layers/qcom-distro/recipes-products/images/qcom-multimedia-image.bbappend at wrynose @ c811139d (2026-07-09) by docsgen on 2026-08-25. Manual edits will be overwritten on regeneration; change the source, not this page. -->

# qcom-multimedia-image

Modifies the `qcom-multimedia-image` recipe. That recipe is not defined in this layer; another layer in the build provides it, and this repository pins no revision of it. This layer declares `LAYERDEPENDS_qcom-3rdparty = "core qcom"` in [conf/layer.conf](../../../../conf/layer.conf).

Bbappend file: [dynamic-layers/qcom-distro/recipes-products/images/qcom-multimedia-image.bbappend](qcom-multimedia-image.bbappend).

Activation: this file is parsed only when the `qcom-distro` layer collection is present. [conf/layer.conf](../../../../conf/layer.conf) sets `BBFILES_DYNAMIC` with the entry `qcom-distro:${LAYERDIR}/dynamic-layers/qcom-distro/*/*/*.bbappend`.

## Configuration variables

| Variable | Effect |
| --- | --- |
| `INCOMPATIBLE_LICENSE_EXCEPTIONS:append:uno-q =` | Recipe and license pairs exempted from the distro's incompatible-license filter. This file adds [`firmware-qcom-boot-qrb2210-arduino-imola:LICENSE.qcom`](../../../../recipes-bsp/firmware-boot/firmware-qcom-boot-qrb2210-arduino-imola.md). Appended to the value inherited from the target recipe. |

Override scopes:

- `INCOMPATIBLE_LICENSE_EXCEPTIONS:append:uno-q`: `append`, machine scope [`uno-q`](../../../../conf/machine/uno-q.md)

## Compatibility

- Machine scopes: [`uno-q`](../../../../conf/machine/uno-q.md).
