# Contributing to meta-qcom-3rdparty

This document describes how to contribute to the **Qualcomm Linux “meta-qcom-3rdparty”** layer and what standards are expected from contributors and vendors.
It follows the same conventions used by the Yocto Project and OpenEmbedded upstream layers to ensure interoperability and quality.

---

## 1  Purpose of this Repository

The `meta-qcom-3rdparty` layer provides a **common OpenEmbedded / Yocto BSP** foundation for third-party hardware platforms based on Qualcomm SoCs.

**Goals**

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
- Do not introduce SoC-generic behavior under a machine-specific path. Those SoC-generic behavior must be sent/upstreamed to `meta-qcom` instead.

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

## 6  Machine Example – Arduino Uno-Q

The [Arduino UNO Q](https://www.arduino.cc/product-uno-q) (QRB2210 / QCM2290 SoC) is the reference example for how a new board is integrated in this layer.
The following subsections walk through each required component.

### 6.1  Machine Configuration

File: `conf/machine/uno-q.conf`

Every machine must have a configuration file under `conf/machine/`.
Key elements to include:

- **SoC include** — pull in the common SoC baseline from `meta-qcom`:
  ```bitbake
  require conf/machine/include/qcom-qcm2290.inc
  ```
- **Vendor override** — prepend a vendor-scoped override so that vendor-specific
  appends can use it without affecting other machines:
  ```bitbake
  MACHINEOVERRIDES =. "arduino:"
  ```
- **Kernel provider** — point to the board-specific kernel recipe:
  ```bitbake
  PREFERRED_PROVIDER_virtual/kernel ?= "linux-arduino"
  ```
- **Device tree** — declare the DTB name(s) used at build and boot time:
  ```bitbake
  QCOM_DTB_DEFAULT ?= "qrb2210-arduino-imola"
  KERNEL_DEVICETREE ?= "qcom/qrb2210-arduino-imola.dtb"
  ```
- **Machine features** — list hardware capabilities:
  ```bitbake
  MACHINE_FEATURES = "efi usbhost usbgadget alsa wifi bluetooth"
  ```
- **Packagegroups** — pull in board firmware and DSP binaries at image level:
  ```bitbake
  MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += " \
      packagegroup-uno-q-firmware \
      packagegroup-uno-q-hexagon-dsp-binaries \
  "
  ```
- **Boot and partition paths** — align with the layout expected by `qcom-common`
  image helpers:
  ```bitbake
  QCOM_BOOT_FILES_SUBDIR = "qrb2210-arduino-imola"
  QCOM_PARTITION_FILES_SUBDIR ?= "partitions/qrb2210-unoq/emmc-16GB"
  QCOM_BOOT_FIRMWARE = "firmware-qcom-boot-qrb2210-arduino-imola"
  ```

### 6.2  Packagegroup

File: `recipes-bsp/packagegroups/packagegroup-uno-q.bb`

Create a machine-specific packagegroup that groups firmware and Hexagon DSP
binaries into separate sub-packages.
Conditional inclusion based on `DISTRO_FEATURES` avoids pulling in unnecessary
blobs:

```bitbake
PACKAGES = "${PN}-firmware ${PN}-hexagon-dsp-binaries"

RRECOMMENDS:${PN}-firmware = " \
    ${@bb.utils.contains_any('DISTRO_FEATURES', 'opencl opengl vulkan', \
        'linux-firmware-qcom-adreno-a702', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', \
        'linux-firmware-ath10k-wcn3990', '', d)} \
    linux-firmware-qcom-qcm2290-audio \
"
RDEPENDS:${PN}-hexagon-dsp-binaries = "hexagon-dsp-binaries-thundercomm-rb1-adsp"
```

### 6.3  Kernel Recipe

File: `recipes-kernel/linux/linux-arduino_6.16.bb`

When the board requires a kernel tree or revision different from `linux-qcom-next`,
provide a dedicated recipe.
Always restrict its applicability with `COMPATIBLE_MACHINE`:

```bitbake
COMPATIBLE_MACHINE = "(uno-q)"
SRC_URI = "git://github.com/arduino/linux-qcom.git;..."
```

Include a `configs/<board>.cfg` kernel fragment for any board-specific
`Kconfig` options that must be enabled on top of the upstream `defconfig`.

If the board can share the `linux-qcom-next` tree (e.g. for a secondary build
variant), use a `.bbappend` with machine overrides instead of a new recipe:

```bitbake
# recipes-kernel/linux/linux-qcom-next_git.bbappend
LINUX_VERSION:uno-q = "6.19+7.0-rc2"
SRCREV:uno-q = "a656209cfb5a49f301c377aa8455a10f83a4a719"
```

### 6.4  Boot Firmware Recipe

File: `recipes-bsp/firmware-boot/firmware-qcom-boot-qrb2210-arduino-imola_<version>.bb`

Closed-source boot binaries must be hosted on a **public, no-login mirror**
managed by the vendor (arduino.cc in this case) and fetched via `SRC_URI`.
Never commit binaries to the repository:

```bitbake
COMPATIBLE_MACHINE = "(uno-q)"
SRC_URI = "https://downloads.arduino.cc/debian-im/unoq-bootloader-emmc-linux-${PV}.zip"
include recipes-bsp/firmware-boot/firmware-qcom-boot-common.inc
```

### 6.5  CI Integration

File: `ci/uno-q.yml`

Add a [kas](https://kas.readthedocs.io/en/latest/userguide.html) machine
fragment that extends `ci/base.yml`:

```yaml
header:
  version: 14
  includes:
  - ci/base.yml

machine: uno-q
```

Then register the machine in the build matrix inside
`.github/workflows/build-yocto.yml` so that every pull request triggers a
`nodistro` build (and optionally a `qcom-distro` build) for the new board:

```yaml
matrix:
  machine:
    - uno-q
```

---

### 6.6  Summary Checklist

When adding a new board, ensure the following files are present:

| File | Purpose |
|---|---|
| `conf/machine/<machine>.conf` | Machine definition |
| `recipes-bsp/packagegroups/packagegroup-<machine>.bb` | Firmware + DSP packagegroup |
| `recipes-bsp/firmware-boot/firmware-qcom-boot-<soc>-<board>_<ver>.bb` | Boot firmware recipe |
| `recipes-kernel/linux/linux-<vendor>_<ver>.bb` (or `.bbappend`) | Kernel recipe or revision override |
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
