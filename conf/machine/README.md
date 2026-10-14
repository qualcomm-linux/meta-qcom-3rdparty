# Machines

One [BitBake](https://github.com/openembedded/bitbake) machine configuration per supported board. Select a board with its
[kas](https://github.com/siemens/kas) fragment in [`ci`](../../ci/README.md), as the
[usage tutorial](../../docs/source/user/USAGE.md) shows.

## Files

- [README.md](README.md) — Lists the supported machines.
- [radxa-dragon-q6a.conf](radxa-dragon-q6a.conf) — Radxa Dragon Q6A (QCS6490), producing a disk image for SD, UFS, or NVMe storage.
- [rubikpi3.conf](rubikpi3.conf) — Thundercomm RUBIK Pi 3 (QCS6490), producing a flashable package with its boot firmware.
