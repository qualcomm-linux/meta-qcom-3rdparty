<!-- Generated from recipes-kernel/linux/ at wrynose @ c811139d (2026-07-09) by docsgen on 2026-08-25. Manual edits will be overwritten on regeneration; change the source, not this page. -->

# recipes-kernel/linux/

The layer's kernel recipes and kernel bbappends, with the `.cfg` configuration fragments they merge into each board's kernel configuration.

## Contents

| File | Description | Machine | Reference |
| --- | --- | --- | --- |
| [recipes-kernel/linux/linux-arduino/configs/arduino.cfg](linux-arduino/configs/arduino.cfg) | Kernel configuration fragment: 47 options. Merged into `linux-arduino`. | [`uno-q`](../../conf/machine/uno-q.md) | [linux-arduino recipe](linux-arduino.md) |
| [recipes-kernel/linux/linux-arduino_7.0.bb](linux-arduino_7.0.bb) | Linux ${PV} kernel for QCOM-based Arduino devices. | [`uno-q`](../../conf/machine/uno-q.md) | [linux-arduino recipe](linux-arduino.md) |
| [recipes-kernel/linux/linux-qcom-next_git.bbappend](linux-qcom-next_git.bbappend) | Modifies the `linux-qcom-next` recipe, which is provided outside this layer. | [`radxa-dragon-q6a`](../../conf/machine/radxa-dragon-q6a.md) | [linux-qcom-next bbappend](linux-qcom-next-bbappend.md) |
| [recipes-kernel/linux/radxa-dragon-q6a/realtek-eth-8169.cfg](radxa-dragon-q6a/realtek-eth-8169.cfg) | Kernel configuration fragment: 10 options. Merged into `linux-qcom-next`. | [`radxa-dragon-q6a`](../../conf/machine/radxa-dragon-q6a.md) | [linux-qcom-next bbappend](linux-qcom-next-bbappend.md) |

## Description

Two patterns coexist here. [`linux-arduino`](linux-arduino.md) is a standalone recipe providing `virtual/kernel` for [`uno-q`](../../conf/machine/uno-q.md), fetching an Arduino-maintained kernel fork independent of the base layer's kernel tree. The [`linux-qcom-next` bbappend](linux-qcom-next-bbappend.md) extends the `linux-qcom-next` recipe that [`radxa-dragon-q6a`](../../conf/machine/radxa-dragon-q6a.md) selects through `PREFERRED_PROVIDER_virtual/kernel`; that recipe is not defined in this layer, another layer in the build provides it, and the bbappend contributes machine-scoped `SRC_URI` content while leaving the recipe itself to that layer.

Kernel configuration here uses the `cml1` fragment-merge mechanism, in place of the `KERNEL_FEATURES` and `.scc` machinery that `linux-yocto`-based recipes use. These recipes inherit `kernel` and `cml1`, so `.cfg` fragments arrive as plain `file://` entries in `SRC_URI`, and a prepend to the configure step copies the in-tree `KBUILD_DEFCONFIG` into `.config` and merges every discovered fragment on top with `scripts/kconfig/merge_config.sh`, the generic OpenEmbedded fragment-merge pattern.

Each fragment carries exactly the delta its board needs. `arduino.cfg` enables display, audio, Bluetooth and networking options for the UNO Q carrier, and `realtek-eth-8169.cfg` adds the Realtek RTL8169 Ethernet stack the Radxa Dragon Q6A needs on top of the upstream defconfig. Device tree selection for `uno-q` is made one folder over: `KERNEL_DEVICETREE:uno-q` is set in the [`esp-qcom-image` bbappend](../images/esp-qcom-image-bbappend.md) under `recipes-kernel/images/`, a coupling worth knowing when tracing device tree wiring.
