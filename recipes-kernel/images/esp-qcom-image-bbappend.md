<!-- Generated from recipes-kernel/images/esp-qcom-image.bbappend at wrynose @ c811139d (2026-07-09) by docsgen on 2026-08-25. Manual edits will be overwritten on regeneration; change the source, not this page. -->

# esp-qcom-image

Modifies the `esp-qcom-image` recipe. That recipe is not defined in this layer; another layer in the build provides it, and this repository pins no revision of it. This layer declares `LAYERDEPENDS_qcom-3rdparty = "core qcom"` in [conf/layer.conf](../../conf/layer.conf).

Bbappend file: [recipes-kernel/images/esp-qcom-image.bbappend](esp-qcom-image.bbappend).

## Configuration variables

| Variable | Effect |
| --- | --- |
| `KERNEL_DEVICETREE:uno-q =` | Device tree blobs the kernel recipe builds and deploys for this machine. This file sets it to `${QCOM_DTB_DEFAULT}.dtb`. The file notes: "Arduino: Dtb as part of UKI until the proper edk2 firmware is available". |

Override scopes:

- `KERNEL_DEVICETREE:uno-q`: machine scope [`uno-q`](../../conf/machine/uno-q.md)

## Compatibility

- Machine scopes: [`uno-q`](../../conf/machine/uno-q.md).
