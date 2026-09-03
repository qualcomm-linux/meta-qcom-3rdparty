# Concepts

## Context

Qualcomm-maintained reference boards stay in `meta-qcom`. Third-party and community boards would fragment that layer, so they live here.

## Model

- **main** is the primary development branch, aimed at the newest Yocto release.
- **wrynose** is the LTS line used by Qualcomm Linux 2.x (Yocto Project 6.0).
- **scarthgap** and **kirkstone** cover earlier Qualcomm Linux 1.x lines.

Patches should land on `main` first and backport to `wrynose` when possible (`BACKPORTING.md`). The layer depends on `core` and `qcom`. When `qcom-distro` is in the build, `BBFILES_DYNAMIC` also loads `dynamic-layers/qcom-distro`.

## Tradeoffs

Splitting third-party machines keeps `meta-qcom` smaller and closer to Qualcomm-supported hardware. The cost is an extra layer and a backport workflow for stable branches.

## Further reading

- `README.md` (branches and dependencies)
- `BACKPORTING.md`
- `docs/contributing.md`

## Provenance

- `README.md`
- `conf/layer.conf`
- `docs/index.md`
