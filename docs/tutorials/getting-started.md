# Getting started

## Objectives

Build a `wrynose` image that includes this layer for a third-party Qualcomm board.

## Prerequisites

- A Yocto Project 6.0 (`wrynose`) workspace
- `openembedded-core` (`meta`) on `wrynose`
- `meta-qcom` on `wrynose`
- [kas](https://kas.readthedocs.io/) if you want the CI-style one-liner

## Concept map

The layer is a BitBake collection. `BBPATH` and `BBFILES` pick up `recipes-*/*/*.bb` and `.bbappend`. Machines in `conf/machine` select kernels, firmware, and device trees.

## Procedure

1. Add `meta-qcom-3rdparty` to `bblayers.conf` after `meta-qcom`.
2. Confirm `conf/layer.conf` lists `LAYERSERIES_COMPAT_qcom-3rdparty = "wrynose"`.
3. Choose a machine from `conf/machine`, for example `uno-q` (Arduino UNO Q).
4. Build with kas from this repo:

```bash
kas build meta-qcom-3rdparty/ci/<machine.yml>
```

Example CI files in this repo: `ci/uno-q.yml`, `ci/radxa-dragon-q6a.yml`.

## Practice

Open `conf/machine/uno-q.conf` and list `MACHINE_FEATURES` and `KERNEL_DEVICETREE`. Those values must match the board you flash.

## Check understanding

You can name the two required sibling layers (`openembedded-core` and `meta-qcom`) and one machine config path under `conf/machine`.

## Next steps

Read the concept page for branch policy, then the reference page for machine variables.

## Provenance

- `README.md`
- `docs/index.md`
- `conf/layer.conf`
- `conf/machine/uno-q.conf`
