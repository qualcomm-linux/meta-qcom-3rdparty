# Linux kernel

Appends to [meta-qcom](https://github.com/qualcomm-linux/meta-qcom)'s `linux-qcom-next` kernel recipe. Configuration fragments
live in a folder per machine, which the append adds to the recipe's file search
path; those folders hold only fragments, so they take no README.

## Folders

- [radxa-dragon-q6a/](radxa-dragon-q6a/) — Holds the Radxa Dragon Q6A kernel configuration fragment, `realtek-eth-8169.cfg`, which enables its Realtek Ethernet.

## Files

- [README.md](README.md) — Describes the kernel appends.
- [linux-qcom-next_git.bbappend](linux-qcom-next_git.bbappend) — Adds the Radxa Dragon Q6A configuration fragment to `linux-qcom-next`.
