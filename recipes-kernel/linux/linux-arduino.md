<!-- Generated from recipes-kernel/linux/linux-arduino_7.0.bb at wrynose @ c811139d (2026-07-09) by docsgen on 2026-08-25. Manual edits will be overwritten on regeneration; change the source, not this page. -->

# linux-arduino

Linux ${PV} kernel for QCOM-based Arduino devices.

| Field | Detail |
| --- | --- |
| Recipe | [recipes-kernel/linux/linux-arduino_7.0.bb](linux-arduino_7.0.bb) |
| Version | `7.0`, from the filename; `PV` set to `${LINUX_VERSION}` |
| License | `GPL-2.0-only`; `LIC_FILES_CHKSUM` points at `COPYING`, `COPYING` is not present in this layer, so the license text is verified against the fetched source |

## Configuration variables

| Variable | Effect |
| --- | --- |
| `LIC_FILES_CHKSUM =` | License files and their checksums. The build fails if the license text in the fetched source changes. |
| `SECTION =` | Package category recorded in the metadata of the produced packages. This file sets it to `kernel`. |
| `COMPATIBLE_MACHINE =` | Regular expression limiting the machines this recipe will build for. This file sets the pattern `(uno-q)`, which matches [`uno-q`](../../conf/machine/uno-q.md) in this layer. |
| `LINUX_VERSION ?=` | Kernel version reported by the kernel build. This file sets it to `7.0`. |
| `PV =` | Package version. Left unset, the version is taken from the recipe filename. This file sets it to `${LINUX_VERSION}`. |
| `SRCREV ?=` | Source revision fetched for the SCM entry in `SRC_URI`. This file sets it to `122c2c22d838ca826e7f4e7360df96fb4e8f7ad2`. |
| `SRCBRANCH ?=` | Recipe-local variable substituted into the SCM parameters of `SRC_URI`. This file sets it to `nobranch=1`. |
| `SRC_URI =` | Source locations the recipe fetches and unpacks. |
| `S =` | Directory holding the unpacked sources that the recipe's tasks build in. |
| `KBUILD_DEFCONFIG ?=` | Names the in-tree kernel defconfig used as the base configuration before fragments are merged. This file sets it to `defconfig`. |

## Configuration fragments

[recipes-kernel/linux/linux-arduino/configs/arduino.cfg](linux-arduino/configs/arduino.cfg), merged through `SRC_URI`.

| Option | Value | Comment |
| --- | --- | --- |
| `CONFIG_DRM_PANEL_SITRONIX_ST7701` | `m` | |
| `CONFIG_DRM_I2C_ADV7511` | `m` | |
| `CONFIG_VIDEO_ADV7511` | `m` | |
| `CONFIG_SND_SOC_PM4125_SDW` | `m` | |
| `CONFIG_SM_LPASSCC_6115` | `y` | |
| `CONFIG_SND_USB_AUDIO` | `m` | |
| `CONFIG_TYPEC_TCPM` | `n` | Do not use TCPCI driver for ANX |
| `CONFIG_SND` | `y` | |
| `CONFIG_SND_USB_AUDIO` | `m` | |
| `CONFIG_UHID` | `y` | |
| `CONFIG_INPUT_UINPUT` | `y` | |
| `CONFIG_INPUT_JOYDEV` | `m` | |
| `CONFIG_BT_LE` | `m` | |
| `CONFIG_BT_RFCOMM` | `m` | |
| `CONFIG_BT_BNEP` | `m` | |
| `CONFIG_HIDRAW` | `y` | |
| `CONFIG_WATCHDOG_SYSFS` | `y` | |
| `CONFIG_DRM_DISPLAY_DP_AUX_CEC` | `y` | |
| `CONFIG_UDMABUF` | `y` | |
| `CONFIG_EXFAT_FS` | `y` | |
| `CONFIG_BT_RFCOMM` | `m` | |
| `CONFIG_ECRYPT_FS` | `y` | |
| `CONFIG_ECRYPT_FS_MESSAGING` | `y` | |
| `CONFIG_ZRAM` | `y` | |
| `CONFIG_ZRAM_BACKEND_LZ4` | `y` | |
| `CONFIG_ZSWAP` | `y` | |
| `CONFIG_DMABUF_HEAPS` | `y` | many GStreamer plugins in the Qualcomm IMSDK use DMABUF userspace heaps |
| `CONFIG_DMABUF_HEAPS_SYSTEM` | `y` | |
| `CONFIG_DMABUF_HEAPS_CMA` | `y` | |
| `CONFIG_CMA_SIZE_MBYTES` | `256` | |
| `CONFIG_GPIO_WAVESHARE_DSI_TOUCH` | `m` | panel |
| `CONFIG_REGULATOR_WAVESHARE_TOUCHSCREEN` | `m` | |
| `CONFIG_DRM_PANEL_JADARD_JD9365DA_H3` | `m` | |
| `CONFIG_DRM_PANEL_HIMAX_HX8394` | `m` | |
| `CONFIG_NF_NAT` | `m` | container |
| `CONFIG_NF_TABLES` | `m` | |
| `CONFIG_NF_TABLES_INET` | `y` | |
| `CONFIG_NF_TABLES_NETDEV` | `y` | |
| `CONFIG_NF_TABLES_IPV4` | `y` | |
| `CONFIG_NF_TABLES_IPV6` | `y` | |
| `CONFIG_NF_TABLES_BRIDGE` | `m` | |
| `CONFIG_NF_TABLES_ARP` | `y` | |
| `CONFIG_NETFILTER_XTABLES_LEGACY` | `y` | |
| `CONFIG_IP_NF_NAT` | `m` | |
| `CONFIG_IP_NF_RAW` | `m` | |
| `CONFIG_IP6_NF_NAT` | `m` | |
| `CONFIG_IP6_NF_RAW` | `m` | |

## Tasks

| Task | Scope | Comment |
| --- | --- | --- |
| `do_configure:prepend()` | `prepend` | |

## Compatibility

- Machines: [`uno-q`](../../conf/machine/uno-q.md), from `COMPATIBLE_MACHINE = "(uno-q)"`.
- Inherited classes: `kernel`, `cml1`, provided outside this layer.
