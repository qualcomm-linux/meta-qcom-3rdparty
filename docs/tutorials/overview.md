# Overview

## Who this is for

Developers adding third-party Qualcomm boards to a Yocto / OpenEmbedded workspace. Official Qualcomm reference boards live in `meta-qcom`, not this layer.

## What this is

`meta-qcom-3rdparty` is an OpenEmbedded BSP layer for community and vendor hardware. The fork `devdocsorg/qli-meta-qcom-3rdparty` tracks Qualcomm Linux 2.x on branch `wrynose`.

The layer:

- Adds machine configs under `conf/machine` (for example `uno-q.conf` and `radxa-dragon-q6a.conf`)
- Declares collection `qcom-3rdparty` in `conf/layer.conf` with `LAYERDEPENDS` on `core` and `qcom`
- Sets `LAYERSERIES_COMPAT_qcom-3rdparty = "wrynose"`
- Optionally pulls `qcom-distro` dynamic-layer recipes when that distro is present

## What to read next

- Tutorial: add the layer and build a machine image
- Concept: why third-party boards are split from `meta-qcom`
- Reference: `conf/layer.conf` and machine variables

## Provenance

- `README.md`
- `conf/layer.conf`
- `docs/index.md`
