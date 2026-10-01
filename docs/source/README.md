# meta-qcom-3rdparty Documentation

Welcome to the documentation for **meta-qcom-3rdparty**, an OpenEmbedded / Yocto layer that extends the Qualcomm Linux ecosystem by supporting community and vendor hardware platforms not officially maintained by Qualcomm.

This documentation is intended for developers, vendors, and contributors working with Qualcomm-based SoCs using the Yocto Project build system.

---

## Overview

The `meta-qcom-3rdparty` layer provides:

- Common BSP enablement for **third-party and community boards**
- **Upstream-aligned** machine support based on [`meta-qcom`](https://github.com/qualcomm-linux/meta-qcom)
- Clean, maintainable structure to avoid fragmentation across vendors
- Integration hooks for both **Qualcomm Linux 1.x** (downstream) and future **Qualcomm Linux 2.x** (upstream) releases

---

## Documentation Index

- [Contribution Guidelines](contributing/CONTRIBUTING.md) — how to contribute patches, follow Yocto conventions, and structure vendor-specific code.
- [Usage Guide](user/USAGE.md) — how to include and build the layer, add it to your workspace, and validate target builds (TODO).
- [Supported Machines](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/conf/machine/README.md) — list of currently supported platforms, vendors, and hardware status (TODO).
- [Developer Notes](contributing/DEVELOPMENT.md) — additional details for maintainers, CI integration, and testing recommendations (TODO).
- [Configuration Reference](user/CONFIGURATION.md) — the layer, machine, kas, and CI settings and their values.

```{toctree}
:hidden:

user/README
contributing/README
```

---

## Quick Start

To build a reference image using [kas](https://kas.readthedocs.io/):

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

## Folders

- [user/](user/README.md) — Holds the usage tutorial and the configuration reference.
- [contributing/](contributing/README.md) — Holds the contribution guidelines, development setup, agent guide, and function reference.
- [.templates/](https://github.com/devdocsorg/meta-qcom-3rdparty/tree/docs/layer-documentation/docs/source/.templates) — Supplies the generated site's entry-point redirect.

## Files

- [README.md](README.md) — Introduces the documentation and supplies the site's homepage.
- [Makefile](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/docs/source/Makefile) — Provides the shared local and CI setup, build, and check commands.
- [conf.py](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/docs/source/conf.py) — Configures Markdown rendering, the function reference, local navigation, and search.
- [requirements.txt](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/docs/source/requirements.txt) — Pins the documentation packages.
- [requirements.lock](https://github.com/devdocsorg/meta-qcom-3rdparty/blob/docs/layer-documentation/docs/source/requirements.lock) — Locks direct and transitive documentation dependencies for reproducible builds.

---

**SPDX-License-Identifier:** MIT
