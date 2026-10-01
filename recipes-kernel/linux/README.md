# Linux kernel appends

Board-specific changes to the [meta-qcom](https://github.com/qualcomm-linux/meta-qcom) kernel recipes.

## Folders

- [radxa-dragon-q6a/](radxa-dragon-q6a/) — Holds [realtek-eth-8169.cfg](radxa-dragon-q6a/realtek-eth-8169.cfg), the Radxa Dragon Q6A Ethernet configuration fragment. BitBake finds files in this folder by name, so it has no README.

## Files

- [README.md](README.md) — Indexes the kernel appends.
- [linux-qcom-next_git.bbappend](linux-qcom-next_git.bbappend) — Adds the Radxa Dragon Q6A Ethernet fragment to the linux-qcom-next kernel.
