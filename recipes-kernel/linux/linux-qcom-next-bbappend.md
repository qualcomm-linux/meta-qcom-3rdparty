<!-- Generated from recipes-kernel/linux/linux-qcom-next_git.bbappend at wrynose @ c811139d (2026-07-09) by docsgen on 2026-08-25. Manual edits will be overwritten on regeneration; change the source, not this page. -->

# linux-qcom-next

Modifies the `linux-qcom-next` recipe. That recipe is not defined in this layer; another layer in the build provides it, and this repository pins no revision of it. This layer declares `LAYERDEPENDS_qcom-3rdparty = "core qcom"` in [conf/layer.conf](../../conf/layer.conf).

Bbappend file: [recipes-kernel/linux/linux-qcom-next_git.bbappend](linux-qcom-next_git.bbappend).

## Configuration variables

| Variable | Effect |
| --- | --- |
| `FILESEXTRAPATHS:prepend:radxa-dragon-q6a :=` | Extends the search path used to resolve `file://` entries in `SRC_URI`. This file adds `${THISDIR}/radxa-dragon-q6a:`. Prepended to the value inherited from the target recipe. |
| `SRC_URI:append:radxa-dragon-q6a =` | Source locations the recipe fetches and unpacks. Appended to the value inherited from the target recipe. |

Override scopes:

- `FILESEXTRAPATHS:prepend:radxa-dragon-q6a`: `prepend`, machine scope [`radxa-dragon-q6a`](../../conf/machine/radxa-dragon-q6a.md)
- `SRC_URI:append:radxa-dragon-q6a`: `append`, machine scope [`radxa-dragon-q6a`](../../conf/machine/radxa-dragon-q6a.md)

## Configuration fragments

[recipes-kernel/linux/radxa-dragon-q6a/realtek-eth-8169.cfg](radxa-dragon-q6a/realtek-eth-8169.cfg), added through `SRC_URI`.

| Option | Value |
| --- | --- |
| `CONFIG_NET_SELFTESTS` | `y` |
| `CONFIG_R8169` | `y` |
| `CONFIG_MDIO_BUS` | `y` |
| `CONFIG_PHYLIB` | `y` |
| `CONFIG_FIXED_PHY` | `y` |
| `CONFIG_REALTEK_PHY` | `y` |
| `CONFIG_REALTEK_PHY_HWMON` | `not set` |
| `CONFIG_FWNODE_MDIO` | `y` |
| `CONFIG_OF_MDIO` | `y` |
| `CONFIG_ACPI_MDIO` | `y` |

## Compatibility

- Machine scopes: [`radxa-dragon-q6a`](../../conf/machine/radxa-dragon-q6a.md).
