# Boot firmware recipes

These recipes deploy boot binaries that [meta-qcom](https://github.com/qualcomm-linux/meta-qcom) does not provide, for the
flashable image package.

## Files

- [README.md](README.md) — Indexes the boot firmware recipes.
- [firmware-qcom-boot-rubikpi3_20260915.bb](firmware-qcom-boot-rubikpi3_20260915.bb) — Fetches the RUBIK Pi 3 boot binaries and CDT from [rubikpi-ai/boot-assets](https://github.com/rubikpi-ai/boot-assets) and deploys them.
