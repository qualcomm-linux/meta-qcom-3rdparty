<!-- Generated from the repository tree at wrynose @ c811139d (2026-07-09) by docsgen on 2026-08-25. Manual edits will be overwritten on regeneration; change the source, not this page. -->

# meta-qcom-3rdparty

[![Build on push](https://img.shields.io/github/actions/workflow/status/qualcomm-linux/meta-qcom-3rdparty/push.yml?branch=wrynose&label=Build%20on%20push)](https://github.com/qualcomm-linux/meta-qcom-3rdparty/actions/workflows/push.yml?query=branch%3Awrynose)
[![Nightly Build](https://img.shields.io/github/actions/workflow/status/qualcomm-linux/meta-qcom-3rdparty/nightly-build.yml?branch=wrynose&label=Nightly%20Build)](https://github.com/qualcomm-linux/meta-qcom-3rdparty/actions/workflows/nightly-build.yml?query=branch%3Awrynose)

## Description

OpenEmbedded/Yocto Project BSP layer for Third-Party Maintained Qualcomm based platforms.

This layer provides additional recipes and machine configuration files for Third-Party Maintained Qualcomm platforms. Reference boards that are officially supported by Qualcomm are available via `meta-qcom` instead.

## Dependencies

This layer depends on:

```text
URI: https://github.com/openembedded/openembedded-core.git
layers: meta
branch: wrynose
revision: HEAD

URI: https://github.com/qualcomm-linux/meta-qcom.git
branch: wrynose
revision: HEAD
```

[conf/layer.conf](conf/layer.conf) declares `LAYERDEPENDS_qcom-3rdparty = "core qcom"`, so this layer builds only alongside openembedded-core and [meta-qcom](https://github.com/qualcomm-linux/meta-qcom). Neither is pinned here, and the files this layer requires from them are not copied into this repository.

## Branches

| Branch | Release |
| --- | --- |
| `main` | Primary development branch, with focus on upstream support and compatibility with the most recent Yocto Project release. |
| `wrynose` | LTS branch based on the Yocto Project 6.0 release, used by Qualcomm Linux 2.x. |
| `scarthgap` | Qualcomm Linux >= 1.4, aligned with Yocto Project 5.0 (LTS). |
| `kirkstone` | Qualcomm Linux <= 1.3, aligned with Yocto Project 4.0 (LTS). |

## Supported machines

| Machine | Board | Reference |
| --- | --- | --- |
| `radxa-dragon-q6a` | Radxa Dragon Q6A | [radxa-dragon-q6a machine configuration](conf/machine/radxa-dragon-q6a.md) |
| `uno-q` | Arduino UNO Q | [uno-q machine configuration](conf/machine/uno-q.md) |

## Layer contents

| Path | Contents |
| --- | --- |
| [.github/workflows/](.github/workflows/README.md) | build, lint, preflight and test-reporting workflows |
| [ci/](ci/README.md) | kas build fragments and CI scripts |
| [conf/](conf/README.md) | layer collection, priority, dependencies and the dynamic-layer gate |
| [conf/machine/](conf/machine/README.md) | machine configuration references for the supported boards |
| [dynamic-layers/qcom-distro/recipes-products/images/](dynamic-layers/qcom-distro/recipes-products/images/README.md) | conditional bbappends, parsed only under their gating layer collection |
| [recipes-bsp/firmware-boot/](recipes-bsp/firmware-boot/README.md) | prebuilt boot firmware recipe |
| [recipes-bsp/packagegroups/](recipes-bsp/packagegroups/README.md) | per-machine firmware and DSP binary bundles |
| [recipes-bsp/u-boot/](recipes-bsp/u-boot/README.md) | bootloader recipe built from source |
| [recipes-kernel/images/](recipes-kernel/images/README.md) | machine-scoped image recipe bbappends |
| [recipes-kernel/linux/](recipes-kernel/linux/README.md) | kernel recipes, bbappends and configuration fragments |
| [docs/index.md](docs/index.md) | hand-written documentation home and kas quick start |
| [docs/contributing.md](docs/contributing.md) | contribution rules for this layer |
| [BACKPORTING.md](BACKPORTING.md) | backport workflow for the stable branches |
| [AGENTS.md](AGENTS.md) | automated-agent instructions for this repository |

## Contributing

Read [docs/contributing.md](docs/contributing.md) before opening a pull request. Changes to a stable branch should be backports where possible; [BACKPORTING.md](BACKPORTING.md) has the backport workflow. For patch style and commit hygiene, follow the Yocto Project guide on [preparing changes for submission](https://docs.yoctoproject.org/dev/contributor-guide/submit-changes.html#preparing-changes-for-submission).

## Communication

- Defects and questions: [meta-qcom-3rdparty issues](https://github.com/qualcomm-linux/meta-qcom-3rdparty/issues).
- Proposed changes: [meta-qcom-3rdparty pull requests](https://github.com/qualcomm-linux/meta-qcom-3rdparty/pulls).
- Security vulnerabilities: report them through [SECURITY.md](SECURITY.md) rather than a public issue.
- Expected conduct in this project: [CODE-OF-CONDUCT.md](CODE-OF-CONDUCT.md).

## Maintainers

- Ricardo Salveti <ricardo.salveti@oss.qualcomm.com>
- Nicolas Dechesne <nicolas.dechesne@oss.qualcomm.com>

## License

This layer is licensed under the MIT license. See [COPYING.MIT](COPYING.MIT) for the full text.
