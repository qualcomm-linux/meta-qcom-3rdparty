# Reference

## Overview

This page lists values taken from the layer and the Arduino UNO Q machine file. It is a lookup page, not a procedure.

## Interface

| Item | Value |
| --- | --- |
| Collection | `qcom-3rdparty` |
| Layer priority | `5` |
| Layer depends | `core qcom` |
| Compatible series | `wrynose` |
| Recipe glob | `${LAYERDIR}/recipes-*/*/*.bb` and `.bbappend` |
| Dynamic layer | `qcom-distro:${LAYERDIR}/dynamic-layers/qcom-distro/*/*/*.bb{,append}` |

## Parameters

From `conf/machine/uno-q.conf` (Arduino UNO Q):

| Variable | Value in source |
| --- | --- |
| `MACHINEOVERRIDES` | prepends `arduino:` |
| `PREFERRED_PROVIDER_virtual/kernel` | `linux-arduino` |
| `PREFERRED_PROVIDER_virtual/bootloader` | `u-boot-arduino` |
| `UBOOT_CONFIG` | `qrb2210-arduino-imola` |
| `QCOM_DTB_DEFAULT` | `qrb2210-arduino-imola` |
| `KERNEL_DEVICETREE` | `qcom/qrb2210-arduino-imola.dtb` |
| `MACHINE_FEATURES` | `efi usbhost usbgadget alsa wifi bluetooth` |
| `QCOM_BOOT_FIRMWARE` | `firmware-qcom-boot-qrb2210-arduino-imola` |
| `QCOM_BOOT_FILES_SUBDIR` | `qrb2210-arduino-imola` |
| `QCOM_PARTITION_FILES_SUBDIR` | `partitions/qrb2210-unoq/emmc-16GB` |

Include: `conf/machine/include/qcom-qcm2290.inc`.

## Errors

The `uno-q` machine adds `qbootctl` so Android bootloader marks the boot successful. The comment in source says the firmware switches to slot B and fails to boot otherwise.

## Examples

Kas one-liner from `docs/index.md`:

```bash
kas build meta-qcom-3rdparty/ci/<machine.yml>
```

## Provenance

- `conf/layer.conf`
- `conf/machine/uno-q.conf`
- `docs/index.md`
