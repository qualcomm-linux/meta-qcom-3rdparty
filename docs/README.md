# meta-qcom-3rdparty Documentation

Welcome to the documentation for **meta-qcom-3rdparty**, an OpenEmbedded / Yocto layer that extends the Qualcomm Linux ecosystem by supporting community and vendor hardware platforms not officially maintained by Qualcomm.

This documentation is intended for developers, vendors, and contributors working with Qualcomm-based SoCs using the Yocto Project build system.

---

## Overview

The `meta-qcom-3rdparty` layer provides:

- Common BSP enablement for **third-party and community boards**
- **Upstream-aligned** machine support based on [`meta-qcom`](https://github.com/qualcomm-linux/meta-qcom)
- Clean, maintainable structure to avoid fragmentation across vendors
- Integration hooks for both **Qualcomm Linux 1.x** (downstream) and **Qualcomm Linux 2.x** (upstream) releases

---

## Documentation Index

- [Contribution Guidelines](source/contributing/CONTRIBUTING.md) — how to contribute patches, follow Yocto conventions, and structure vendor-specific code.
- [Usage Guide](source/user/USAGE.md) — how to build an image for a supported board and find the result.
- [Configuration Reference](source/user/CONFIGURATION.md) — what each of the layer's settings does, with safe example values.
- [Supported Machines](../conf/machine/README.md) — list of currently supported platforms and their machine configurations.
- [Developer Notes](source/contributing/DEVELOPMENT.md) — development environment, documentation build, and local checks.

---

## Quick Start

To build a reference image using [kas](https://github.com/siemens/kas) ([documentation](https://kas.readthedocs.io/)):

```bash
kas build meta-qcom-3rdparty/ci/<machine.yml>
```

Otherwise add this layer to your existing Yocto environment:

```bash
git clone https://github.com/qualcomm-linux/meta-qcom-3rdparty.git
bitbake-layers add-layer ../meta-qcom-3rdparty
```

---

## Related Layers and References

- [meta-qcom](https://github.com/qualcomm-linux/meta-qcom)
- [meta-qcom-hwe](https://github.com/qualcomm-linux/meta-qcom-hwe)
- [meta-qcom-distro](https://github.com/qualcomm-linux/meta-qcom-distro)
- [OpenEmbedded Layer Index](https://layers.openembedded.org/layerindex/)
- [Yocto Project Documentation](https://docs.yoctoproject.org/)

## Build this documentation

After installing the [prerequisites](source/contributing/DEVELOPMENT.md#prerequisites),
build the site from the repository root, then open `docs/site/index.html` directly
in a browser; no HTTP server is needed:

```sh
make -f docs/source/Makefile setup html
```

The site is generated and not committed. It is built from the repository's README
files and the guides in [source](source/README.md). The build writes
`docs/site/THIRD_PARTY_LICENSES.txt`, which keeps the licences of the bundled
documentation assets. The build attaches the upstream notices to redistributed
JavaScript without changing its behaviour.

## Folders

- [source/](source/README.md) — Contains the authoritative guides and the documentation build configuration.

## Files

- [README.md](README.md) — Introduces the layer's documentation and explains how to build the site.

---

**SPDX-License-Identifier:** MIT
