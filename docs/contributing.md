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
- Do not introduce SoC-generic behavior under a machine-specific path.

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
- [`poky-altcfg`](https://git.yoctoproject.org/poky/tree/meta-poky/conf/distro/poky-altcfg.conf) (systemd-compatible)
- [`meta-qcom-distro`](https://github.com/qualcomm-linux/meta-qcom-distro)

### 2.6  Maintainer Expectations

- Each contributor acts as the **maintainer** of their changes, upstream and downstream.
- Vendors must appoint a **point-of-contact (PoC)** to review and triage vendor-specific PRs and issues promptly.

---

## 3  Upstream Baseline

**Goal:** upstream-aligned BSP enablement serving as the base for future Qualcomm Linux 2.0 releases.
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

*TODO*

The `Arduino Uno-Q` board serves as an example of a machine integration inside this layer.

Typical components include:
- `conf/machine/arduino-unoq.conf`
- Board-specific packagegroup under `recipes-bsp/packagegroups/`
- Firmware recipes referencing vendor-hosted binaries
- Partition configuration defined through `qcom-ptool`
- Kernel support via `linux-qcom-next` `.bbappend`
- CI job validating `core-image-minimal` build and boot on target

This structure should be mirrored by any new vendor machine added to the layer.

---

## 7  Additional References

- [Yocto Project Contributor Guide](https://docs.yoctoproject.org/dev/contributor-guide/index.html)
- [OpenEmbedded Core Contributor Guide](https://docs.yoctoproject.org/dev/contributor-guide/submit-changes.html)
- [Yocto Layer Model and Compatibility](https://docs.yoctoproject.org/dev-manual/layers.html)
- [BitBake User Manual](https://docs.yoctoproject.org/bitbake/)

---

**SPDX-License-Identifier:** MIT
