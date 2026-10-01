# CI configuration and helper scripts

The [configuration reference](../docs/source/user/CONFIGURATION.md#kas-configuration)
explains each kas file, and the
[agent guide](../docs/source/contributing/AGENTS.md#4-run-routine-checks-via-ci-helper-scripts)
shows how to run the scripts.

## Files

- [README.md](README.md) — Indexes the kas configuration and CI scripts.
- [base.yml](base.yml) — Adds [meta-qcom](https://github.com/qualcomm-linux/meta-qcom) and this layer, and includes the [meta-qcom](https://github.com/qualcomm-linux/meta-qcom) base build configuration.
- [ci.yml](ci.yml) — Includes the [meta-qcom](https://github.com/qualcomm-linux/meta-qcom) CI build settings.
- [kas-container-shell-helper.sh](kas-container-shell-helper.sh) — Runs a CI script inside the kas container with this repository mounted.
- [linux-qcom-next.yml](linux-qcom-next.yml) — Selects the linux-qcom-next kernel.
- [meta-qcom.yml](meta-qcom.yml) — Declares the [meta-qcom](https://github.com/qualcomm-linux/meta-qcom) repository for the other kas files.
- [mirror.yml](mirror.yml) — Includes the [meta-qcom](https://github.com/qualcomm-linux/meta-qcom) shared-state mirror setting.
- [qcom-distro.yml](qcom-distro.yml) — Includes the [meta-qcom](https://github.com/qualcomm-linux/meta-qcom) Qualcomm Linux distribution configuration.
- [radxa-dragon-q6a.yml](radxa-dragon-q6a.yml) — Builds for the Radxa Dragon Q6A.
- [rubikpi3.yml](rubikpi3.yml) — Builds for the Thundercomm RUBIK Pi 3.
- [world.yml](world.yml) — Builds every recipe in [meta-qcom](https://github.com/qualcomm-linux/meta-qcom) and this layer.
- [yocto-buildstats.sh](yocto-buildstats.sh) — Charts and summarises the task statistics of the latest build.
- [yocto-check-layer.sh](yocto-check-layer.sh) — Runs yocto-check-layer on this layer for all its machines.
- [yocto-patchreview.sh](yocto-patchreview.sh) — Reviews the layer's patches and fails on malformed sign-off or upstream-status tags.
