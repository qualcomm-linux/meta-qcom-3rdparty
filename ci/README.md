# CI configuration

[kas](https://github.com/siemens/kas) fragments that compose the layer's builds, and the scripts that CI runs inside
the kas container. The [configuration reference](../docs/source/user/CONFIGURATION.md#kas-fragments)
explains each fragment, and the [agent guide](../docs/source/contributing/AGENTS.md)
shows how to run the scripts.

## Files

- [README.md](README.md) — Describes the kas fragments and CI scripts.
- [base.yml](base.yml) — Adds [meta-qcom](https://github.com/qualcomm-linux/meta-qcom)'s base build and this layer; every machine fragment includes it.
- [ci.yml](ci.yml) — Adds meta-qcom's CI build settings.
- [kas-container-shell-helper.sh](kas-container-shell-helper.sh) — Runs a repository script inside a `kas-container` shell for `ci/base.yml`, passing it the repository and work directories.
- [linux-qcom-next.yml](linux-qcom-next.yml) — Selects the `linux-qcom-next` kernel in `local.conf`.
- [meta-qcom.yml](meta-qcom.yml) — Declares the meta-qcom repository for the fragments that include meta-qcom's.
- [mirror.yml](mirror.yml) — Adds meta-qcom's shared-state mirror setting.
- [qcom-distro.yml](qcom-distro.yml) — Adds meta-qcom's `qcom-distro` distribution, layers, and images.
- [radxa-dragon-q6a.yml](radxa-dragon-q6a.yml) — Builds for the Radxa Dragon Q6A.
- [rubikpi3.yml](rubikpi3.yml) — Builds for the Thundercomm RUBIK Pi 3.
- [world.yml](world.yml) — Builds every recipe from meta-qcom and this layer.
- [yocto-buildstats.sh](yocto-buildstats.sh) — Charts and summarises the latest build's task statistics.
- [yocto-check-layer.sh](yocto-check-layer.sh) — Runs `yocto-check-layer` on this layer against every machine it defines.
- [yocto-patchreview.sh](yocto-patchreview.sh) — Reviews the layer's patches and fails on a malformed sign-off or upstream status.
