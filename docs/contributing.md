# Contributing to meta-qcom-3rdparty

This document describes how to contribute to the **Qualcomm Linux “meta-qcom-3rdparty”** layer and what standards are expected from contributors and vendors.
It follows the same conventions used by the Yocto Project and OpenEmbedded upstream layers to ensure interoperability and quality.

---

## 1  Purpose of this Repository

The `meta-qcom-3rdparty` layer provides a **common OpenEmbedded / Yocto BSP** foundation for third-party hardware platforms based on Qualcomm SoCs.

### Goals

- **Common layer for non-Qualcomm EVKs:** consolidate enablement for boards not officially maintained by Qualcomm.
- **Clean BSP implementation:** a shared source of truth that vendors can reuse without divergence.
- **Extend the Qualcomm Linux ecosystem:** encourage community participation and long-term maintainability aligned with `meta-qcom`.

References:

- [Yocto Project Overview](https://docs.yoctoproject.org/overview-manual/yp-intro.html)
- [OpenEmbedded Layer Index](https://layers.openembedded.org/layerindex/)

---

## 2  General Contribution Guidelines

Our process mirrors the official Yocto Project contribution flow — see
[Preparing Changes for Submission](https://docs.yoctoproject.org/dev/contributor-guide/submit-changes.html#preparing-changes-for-submission).

### 2.1  Pull-Request Workflow

- **Fork and propose changes** via GitHub Pull Requests.
  Use **draft mode** for work-in-progress patches.
- **Create clean commits:** one logical change per commit.
  Follow [Yocto commit style](https://docs.yoctoproject.org/dev/contributor-guide/submit-changes.html#writing-good-commit-messages).
- **Explain _why_** the change is needed in the commit message.
- **Add a Signed-off-by line** to certify compliance with the [Developer’s Certificate of Origin](https://developercertificate.org/).
- **Validate locally** before submission: build with `bitbake`, flash, and verify runtime.
- **Address review feedback** and re-push to update your PR.
  Use `git rebase -i` to squash or reorder commits as needed.

### 2.2  Machine-Specific Isolation

Because this layer expects to host multiple vendor platforms:

- Use **machine overrides** (`:machine` or `:append:machine`) to confine board-specific logic.
- Avoid cross-contamination between machines or with upstream `meta-qcom`.
- Do not introduce SoC-generic behavior under a machine-specific path. Such SoC-generic behavior must be sent/upstreamed to `meta-qcom` instead.

Reference: [BitBake Overrides](https://docs.yoctoproject.org/ref-manual/variables.html#var-OVERRIDES)

### 2.3  Repository Organization

All vendor boards live together in a single layer:

- **No branch or folder segregation per vendor.**
- Maintain quality equivalent to `meta-qcom`.

### 2.4  No Recipe Forks

- Forks of recipes from `meta-qcom`, `meta-qcom-hwe`, or base OE / Yocto layers are **not accepted**.
- Use `.bbappend` files for vendor-specific patching.
- Keep upstream recipes authoritative.

Reference: [Understanding bbappends](https://docs.yoctoproject.org/ref-manual/terms.html#term-Append-Files)

### 2.5  Scope of Changes

- Limit changes to **BSP-specific content** (kernel, firmware, device tree, drivers, partition configs).
- Avoid distribution-specific logic — vendors may ship separate distro layers.

Preferred test distros:

- `nodistro` (systemd-compatible)
- [`meta-qcom-distro`](https://github.com/qualcomm-linux/meta-qcom-distro)

### 2.6  Maintainer Expectations

- Each contributor acts as the **maintainer** of their changes, upstream and downstream.
- Vendors must appoint a **point-of-contact (PoC)** to review and triage vendor-specific PRs and issues promptly, which will be incorporated as part of the repository CODEOWNERS file.

---

## 3  Upstream Baseline

**Goal:** upstream-aligned BSP enablement serving as the base for future Qualcomm Linux releases.
**Target branch:** `main`

### 3.1  Expected Contribution Types

- **Machine configuration files** (`conf/machine/*.conf`)
- **Closed-source component recipes**
  - Do **not** commit binaries.
  - Host them on a **public, no-login mirror** managed by the vendor.
  - Provide a clear `DESCRIPTION` and `LICENSE` field.
- **Machine-specific packagegroups:**
  See [`meta-qcom/recipes-bsp/packagegroups`](https://github.com/qualcomm-linux/meta-qcom/tree/master/recipes-bsp/packagegroups)
- **Partition definitions:**
  Use [`qcom-ptool`](https://github.com/qualcomm-linux/qcom-ptool) to manage partition layouts.
- **Kernel enablement:**
  - Align with `linux-yocto-dev` and `linux-qcom-next`.
  - Patches should be **submitted upstream to the Linux kernel** first.
  - Temporary backports or in-flight patches are acceptable if tracked.
  References:
  - [Yocto kernel workflow](https://docs.yoctoproject.org/kernel-dev/index.html)
  - [Submitting Linux patches](https://www.kernel.org/doc/html/latest/process/submitting-patches.html)
- **Firmware:**
  Custom firmware must be contributed to [`linux-firmware`](https://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git/) whenever possible.
- **External layer recipes:**
  Changes targeting `oe-core` or `meta-openembedded` should be sent directly upstream.

---

## 4  Downstream Baseline – Qualcomm Linux 1.x

**Goal:** extend existing Qualcomm Linux 1.x releases with third-party hardware support equivalent to official EVKs.
**Target branch:** `scarthgap` (≥ 1.4)

### 4.1  Expected Contribution Types

- Machine configuration files
- Recipes for closed-source components
  (same binary-hosting rules as upstream)
- Machine-specific packagegroups:
  [`meta-qcom-hwe/recipes-firmware/packagegroups`](https://github.com/qualcomm-linux/meta-qcom-hwe/tree/scarthgap/recipes-firmware/packagegroups)
- Partition definitions:
  [`meta-qcom-hwe/recipes-devtools/partition-utils`](https://github.com/qualcomm-linux/meta-qcom-hwe/tree/scarthgap/recipes-devtools/partition-utils)
- Kernel customization:
  Follow [`linux-qcom-base`](https://github.com/qualcomm-linux/meta-qcom-hwe/tree/scarthgap/recipes-kernel/linux)
  or `linux-qcom-custom` guidelines.
  - Machine-specific patches via `.bbappend`.
  - Custom kernel recipe forks only with maintainer consent.
- Firmware additions:
  Extend [`linux-firmware` recipes](https://github.com/qualcomm-linux/meta-qcom-hwe/tree/scarthgap/recipes-firmware/firmware) as needed.
- Hardware-specific enablement should **live entirely within this layer**, not in external “extras” layers or manifests.
- Vendor-specific distro features / demo content belong in a **separate distro layer** maintained by the vendor.

---

## 5  Integration Expectations

- The repository should maintain **CI / CD** pipelines (GitHub Actions with kas) to validate new submissions.
  Reference: [kas Documentation](https://kas.readthedocs.io/en/latest/userguide.html)
- Vendors may optionally integrate with **LAVA** for runtime testing (get in contact with the project maintainers or raise an issue).
  Reference: [LAVA Framework](https://docs.lavasoftware.org/lava/index.html)

---

## 6  Machine Example – Thundercomm RUBIK Pi 3

The [Thundercomm RUBIK Pi 3](https://www.thundercomm.com/product/rubik-pi/) (QCS6490 SoC) is the reference example for how a new board is integrated in this layer.
The following subsections walk through each required component.

### 6.1  Machine Configuration

File: `conf/machine/rubikpi3.conf`

Every machine must have a configuration file under `conf/machine/`.
Key elements to include:

- **Kernel provider** — set it _before_ the SoC include. `qcom-base.inc`
  applies a weak default and the first `?=` wins, so a later assignment would
  be silently ignored:

  ```bitbake
  PREFERRED_PROVIDER_virtual/kernel ?= "linux-qcom-next"
  ```

- **SoC include** — pull in the common SoC baseline from `meta-qcom`:

  ```bitbake
  require conf/machine/include/qcom-qcs6490.inc
  ```

- **Machine features** — extend the SoC defaults with the board's hardware
  capabilities:

  ```bitbake
  MACHINE_FEATURES += "efi pci"
  ```

- **Device tree** — declare the DTB name(s) used at build and boot time:

  ```bitbake
  KERNEL_DEVICETREE = "qcom/qcs6490-thundercomm-rubikpi3.dtb"
  QCOM_DTB_DEFAULT ?= "qcs6490-thundercomm-rubikpi3"
  ```

- **Boot firmware, partitions and CDT** — align with the layout expected by
  the `qcom-common` image helpers. The partition layout itself comes from
  `qcom-ptool` via `qcom-partition-conf`, not from this layer:

  ```bitbake
  QCOM_BOOT_FIRMWARE = "firmware-qcom-boot-rubikpi3"
  QCOM_BOOT_FILES_SUBDIR = "rubikpi3"
  QCOM_PARTITION_FILES_SUBDIR ?= "partitions/qcs6490-thundercomm-rubikpi3/ufs"
  QCOM_CDT_FILE = "RubikPi3_CDT"
  ```

- **Packagegroups** — pull in the SoC essentials and the board firmware at
  image level:

  ```bitbake
  MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += " \
      packagegroup-qcom-boot-essential \
      packagegroup-machine-essential-qcom-qcs6490-soc \
      packagegroup-rubikpi3-firmware \
  "
  ```

### 6.2  Packagegroup

File: `recipes-bsp/packagegroups/packagegroup-rubikpi3.bb`

Create a machine-specific packagegroup for the board firmware.
Conditional inclusion based on `DISTRO_FEATURES` avoids pulling in unnecessary
blobs:

```bitbake
PACKAGES = "${PN}-firmware"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', \
        'linux-firmware-qcom-adreno-a660 linux-firmware-qcom-qcm6490-adreno', '', d)} \
    linux-firmware-qcom-qcm6490-qupv3fw \
    linux-firmware-qcom-vpu \
    linux-firmware-qcom-qcs6490-thundercomm-rubikpi3-audio \
    linux-firmware-qcom-qcs6490-compute \
"
```

Boards that ship Hexagon DSP binaries add a separate `${PN}-hexagon-dsp-binaries`
sub-package, as `packagegroup-radxa-dragon-q6a.bb` does, so images can pick
the firmware without the DSP blobs.

### 6.3  Kernel

Prefer the `linux-qcom-next` kernel from `meta-qcom`; `rubikpi3` uses it
unchanged. Board-specific `Kconfig` fragments or revision pins go into a
`.bbappend` with machine overrides, never into a copied recipe:

```bitbake
# recipes-kernel/linux/linux-qcom-next_git.bbappend
FILESEXTRAPATHS:prepend:radxa-dragon-q6a := "${THISDIR}/radxa-dragon-q6a:"

SRC_URI:append:radxa-dragon-q6a = " file://realtek-eth-8169.cfg"
```

Only when the board needs a different kernel tree is a dedicated
`recipes-kernel/linux/linux-<vendor>_<ver>.bb` recipe acceptable. Always
restrict its applicability with `COMPATIBLE_MACHINE = "(<machine>)"` and keep
the board-specific options in a `configs/<board>.cfg` fragment merged on top
of the upstream `defconfig`.

### 6.4  Boot Firmware Recipe

Reuse the SoC boot firmware recipe from `meta-qcom` whenever the board is
covered by it. Only add a recipe here when the board needs binaries `meta-qcom`
does not provide, such as a vendor-signed firmware set or the board-specific CDT.

File: `recipes-bsp/firmware-boot/firmware-qcom-boot-rubikpi3_20260621.bb`

Closed-source boot binaries must be hosted on a **public, no-login mirror**
managed by the vendor (the `rubikpi-ai/boot-assets` git repository in this
case) and fetched via `SRC_URI`. Never commit binaries to the repository:

```bitbake
LICENSE = "LicenseRef-LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=165287851294f2fb8ac8cbc5e24b02b0"

SRC_URI = "git://github.com/rubikpi-ai/boot-assets;protocol=https;branch=main;destsuffix=${BP}"
SRCREV = "10b868574aa4d06fb3836399d10eb5c792765504"

inherit allarch deploy

QCOM_BOOT_IMG_SUBDIR = "rubikpi3"

COMPATIBLE_MACHINE = "(rubikpi3)"
```

`do_deploy` installs the boot binaries under `${QCOM_BOOT_IMG_SUBDIR}`,
including the CDT named by `QCOM_CDT_FILE`, where the `qcomflash` class picks
them up.

Vendor firmware under a license the distro lists as incompatible needs an
exception for the images that ship it, confined to the machine and kept under
`dynamic-layers/qcom-distro/` so it only applies with `meta-qcom-distro`:

```bitbake
# dynamic-layers/qcom-distro/recipes-products/images/qcom-multimedia-image.bbappend
INCOMPATIBLE_LICENSE_EXCEPTIONS:append:rubikpi3 = " firmware-qcom-boot-rubikpi3:LicenseRef-LICENSE.qcom-2"
```

### 6.5  CI Integration

File: `ci/rubikpi3.yml`

Add a [kas](https://kas.readthedocs.io/en/latest/userguide.html) machine
fragment that extends `ci/base.yml`:

```yaml
header:
  version: 14
  includes:
  - ci/base.yml

machine: rubikpi3
```

Then register the machine in the build matrix inside
`.github/workflows/build-yocto.yml` so that every pull request triggers a
`nodistro` and a `qcom-distro` build for the new board:

```yaml
matrix:
  machine:
    - rubikpi3
```

---

### 6.6  Summary Checklist

When adding a new board, ensure the following files are present:

| File | Purpose |
| --- | --- |
| `conf/machine/<machine>.conf` | Machine definition |
| `recipes-bsp/packagegroups/packagegroup-<machine>.bb` | Firmware (and DSP) packagegroup |
| `recipes-bsp/firmware-boot/firmware-qcom-boot-<machine>_<ver>.bb` | Board firmware recipe, when not already covered by `meta-qcom` |
| `recipes-kernel/linux/linux-qcom-next_git.bbappend` (or `linux-<vendor>_<ver>.bb`) | Kernel fragments / revision override, or dedicated kernel recipe |
| `ci/<machine>.yml` | KAS machine fragment |
| Entry in `.github/workflows/build-yocto.yml` matrix | CI build registration |

---

## 7  Additional References

- [Yocto Project Contributor Guide](https://docs.yoctoproject.org/dev/contributor-guide/index.html)
- [OpenEmbedded Core Contributor Guide](https://docs.yoctoproject.org/dev/contributor-guide/submit-changes.html)
- [Yocto Layer Model and Compatibility](https://docs.yoctoproject.org/dev-manual/layers.html)
- [BitBake User Manual](https://docs.yoctoproject.org/bitbake/)

---

**SPDX-License-Identifier:** MIT
