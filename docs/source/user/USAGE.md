# Build an image for a supported board

This tutorial builds `core-image-base` for the Thundercomm RUBIK Pi 3 (`rubikpi3`)
with [kas](https://github.com/siemens/kas) and finds the flashable package it
produces. The same steps build the other machines in
[`conf/machine`](../../../conf/machine/) with their fragments in
[`ci`](../../../ci/).

## Prerequisites

- A Linux host with Docker, tested with Docker 29.7
  ([installation guide](https://docs.docker.com/engine/install/)), or Podman.
  `kas-container` runs the whole build inside the kas container image.
- `kas-container` from kas 5.5
  ([installation guide](https://kas.readthedocs.io/en/latest/userguide/getting-started.html)):

  ```sh
  curl -sSfL -o ~/.local/bin/kas-container \
    https://raw.githubusercontent.com/siemens/kas/refs/tags/5.5/kas-container
  chmod +x ~/.local/bin/kas-container
  ```

- Git, network access, and the free disk space in the Yocto Project's
  [system requirements](https://docs.yoctoproject.org/ref-manual/system-requirements.html).

## 1. Clone the layer

```sh
git clone https://github.com/qualcomm-linux/meta-qcom-3rdparty.git
cd meta-qcom-3rdparty
```

The clone checks out the default branch. To build another branch, choose it from
the README's [branch table](../../../README.md#branches) and `git switch` to it.

## 2. Choose the work directories

`kas-container` clones the other layers and builds in `KAS_WORK_DIR`. Keep it, and
the download and shared-state caches, outside the checkout. Copy
[`.env.example`](../../../.env.example), adjust its paths if needed, and load it:

```sh
cp .env.example .env
set -a; . ./.env; set +a
mkdir -p "$KAS_WORK_DIR"
```

## 3. Build the image

```sh
kas-container build ci/rubikpi3.yml
```

[`ci/rubikpi3.yml`](../../../ci/rubikpi3.yml) selects the `rubikpi3` machine and
includes [`ci/base.yml`](../../../ci/base.yml), which adds
[meta-qcom](https://github.com/qualcomm-linux/meta-qcom) and its default build: the
`nodistro` distribution and the `core-image-base` image.

In the project's CI, which keeps its downloads and shared-state cache between runs, the
`rubikpi3/nodistro` job builds `world` and then this image; it took between 8 and
128 minutes across 63 successful runs, for example
[9 minutes](https://github.com/qualcomm-linux/meta-qcom-3rdparty/actions/runs/36785803193/job/110130170699)
and [128 minutes](https://github.com/qualcomm-linux/meta-qcom-3rdparty/actions/runs/36506511249/job/109209256651).

## 4. Find the result

The command exits with status 0, and
`$KAS_WORK_DIR/build/tmp/deploy/images/rubikpi3/` contains:

- `core-image-base-rubikpi3.rootfs.qcomflash.tar.gz`, the flashable package: the
  boot firmware, partition tables, and root filesystem, in a
  `core-image-base-rubikpi3/` folder;
- `core-image-base-rubikpi3.rootfs.qcomflash/`, the same package unpacked;
- `core-image-base-rubikpi3.rootfs.ext4`, the root filesystem image.

The `radxa-dragon-q6a` build also writes `.wic.gz` and `.wic.bmap` disk images.
meta-qcom's [flashing guide](https://github.com/qualcomm-linux/meta-qcom/blob/master/docs/flashing.md)
explains the [QDL](https://github.com/linux-msm/qdl) tool that writes a flashable package to a board.
