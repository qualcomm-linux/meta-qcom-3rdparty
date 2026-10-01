# Build an image for a supported board

This tutorial builds `core-image-base` for the Thundercomm RUBIK Pi 3
(`rubikpi3`) with kas-container, as the layer's CI does, and finds the
flashable package it produces.

## Prerequisites

- A Linux host that meets the Yocto Project
  [system requirements](https://docs.yoctoproject.org/ref-manual/system-requirements.html),
  including their free disk space.
- Docker or Podman, and [kas-container](https://kas.readthedocs.io/en/latest/userguide/kas-container.html)
  on your `PATH`. These steps were checked with kas 4.8.2.
- Network access: kas clones the layers, and BitBake downloads the sources.

## 1. Clone the layer

```sh
git clone https://github.com/qualcomm-linux/meta-qcom-3rdparty.git
cd meta-qcom-3rdparty
```

## 2. Choose the work and cache directories

Set `KAS_WORK_DIR`, `DL_DIR`, and `SSTATE_DIR` to directories outside the
checkout, as the [agent guide's recommended environment](../contributing/AGENTS.md#2-recommended-environment)
shows. [.env.example](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/.env.example)
describes each variable. Without them, kas-container builds in the checkout.

## 3. Build the image

```sh
kas-container build ci/rubikpi3.yml
```

`ci/rubikpi3.yml` sets the machine and includes `ci/base.yml`, which adds
[meta-qcom](https://github.com/qualcomm-linux/meta-qcom) and selects
`nodistro` and the `core-image-base` target. To build the Qualcomm Linux
distribution images instead, add the distribution fragment:
`kas-container build ci/rubikpi3.yml:ci/qcom-distro.yml`. The
[configuration reference](CONFIGURATION.md#kas-configuration) describes every
fragment.

The first build compiles a complete toolchain and image, which takes hours;
later builds reuse the shared-state cache.

## Expected result

kas-container exits with status 0, and
`$KAS_WORK_DIR/build/tmp/deploy/images/rubikpi3/` contains:

- `core-image-base-rubikpi3.rootfs.ext4`, the root filesystem image.
- `core-image-base-rubikpi3.rootfs.qcomflash.tar.gz`, the flashable package
  with the boot firmware, partition tables, and root filesystem.

For `ci/radxa-dragon-q6a.yml`, the folder is `radxa-dragon-q6a/`. It also holds
`core-image-base-radxa-dragon-q6a.rootfs.wic.gz` and its `.wic.bmap`, a disk
image for SD, UFS, or NVMe storage; the board's boot firmware stays in its SPI
NOR flash.
