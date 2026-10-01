# Branches

| Branch | Why it exists | Maintenance and relationship to `main` |
| --- | --- | --- |
| `main` | Primary development, with focus on upstream support and the most recent Yocto Project release | Canonical branch. Changes land here first. |
| `wrynose` | LTS branch based on the Yocto Project 6.0 release, used by Qualcomm Linux 2.x | Receives backports of `main` changes, plus changes that apply only to `wrynose`, as the agent guide's [backporting section](docs/source/contributing/AGENTS.md#8-backporting-to-a-release-branch) describes. |
| `scarthgap` | Qualcomm Linux 1.4 and later, aligned with Yocto Project 5.0 (LTS) | Takes Qualcomm Linux 1.x contributions directly, as the contribution guide's [downstream baseline](docs/source/contributing/CONTRIBUTING.md#4--downstream-baseline--qualcomm-linux-1x) describes. Whether it merges with `main` is not documented. |
| `kirkstone` | Qualcomm Linux 1.3 and earlier, aligned with Yocto Project 4.0 (LTS) | Not documented. |
| `next` | Not documented | Not documented. |

The [README](README.md#branches) gives each branch's status, whether to build
from it, and where its contributions go.
