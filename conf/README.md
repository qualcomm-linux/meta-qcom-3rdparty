<!-- Generated from conf/ at wrynose @ c811139d (2026-07-09) by docsgen on 2026-08-25. Manual edits will be overwritten on regeneration; change the source, not this page. -->

# conf/

This folder holds `layer.conf`, which registers the layer with BitBake and declares what it depends on, together with the [conf/machine/](machine/README.md) directory of machine configurations the layer ships.

## Contents

| File | Description | Reference |
| --- | --- | --- |
| [conf/layer.conf](layer.conf) | Collection name, file pattern, priority, layer dependencies, Yocto release compatibility and the dynamic-layer gate. | Configuration variables |
| `conf/machine/` | Machine configurations for the boards this layer supports. | [conf/machine/](machine/README.md) |

## Description

`layer.conf` establishes the layer's identity. It extends `BBPATH` with the layer root, adds the layer's `recipes-*/*/*.bb` and `.bbappend` globs to `BBFILES`, and declares the collection name that every other `BBFILE_*` and `LAYER*` variable in the file is keyed on.

`BBFILE_PATTERN_qcom-3rdparty` scopes that collection to the files under the layer directory, and `BBFILE_PRIORITY_qcom-3rdparty` sets its precedence for recipe selection and for bbappend ordering against the other layers in a build. `LAYERDEPENDS_qcom-3rdparty` declares the collections this layer requires, the OpenEmbedded-Core `core` collection and the `qcom` collection of the sibling `meta-qcom` BSP layer; BitBake parses the layer once both are present in `bblayers.conf`. `LAYERSERIES_COMPAT_qcom-3rdparty` declares the Yocto release series the layer is validated against, and BitBake warns when the active series falls outside it.

`BBFILES_DYNAMIC` gates a further set of recipe files on the presence of a named collection in the build. This is the standard Yocto pattern for a layer carrying content meant to combine with another optional layer: the guarded files are parsed when a layer providing that collection is also included, which lets this layer extend a distro-specific layer's recipes while keeping that layer optional. Here the gate covers the [`qcom-multimedia-image` bbappend](../dynamic-layers/qcom-distro/recipes-products/images/qcom-multimedia-image-bbappend.md).

BitBake reads every reachable `conf/layer.conf` at parse time, one per layer named in `BBLAYERS`, and aggregates these declarations into the collection graph that resolves dependency order, recipe precedence and which dynamic files enter the build.

## Configuration variables

| Variable | Effect |
| --- | --- |
| `BBPATH .=` | Extends BitBake's search path for `conf/` and `classes/` files with this layer's directory. The file notes: "We have a conf and classes directory, add to BBPATH". |
| `BBFILES +=` | Adds this layer's recipes and bbappends to the set of files BitBake parses. Adds the `${LAYERDIR}/recipes-*/*/*.bb` and `${LAYERDIR}/recipes-*/*/*.bbappend` recipe file patterns. The file notes: "We have a packages directory, add to BBFILES". |
| `BBFILE_COLLECTIONS +=` | Registers the layer's collection name: the identifier the other `BBFILE_*` and `LAYER*` variables are keyed on. This file adds `qcom-3rdparty`. |
| `BBFILE_PATTERN_qcom-3rdparty :=` | Regular expression matching the files that belong to this layer's collection. |
| `BBFILE_PRIORITY_qcom-3rdparty =` | Layer priority: decides which layer wins when more than one provides the same recipe, and the order in which bbappends are applied. This file sets it to `5`. |
| `LAYERDEPENDS_qcom-3rdparty =` | Layer collections that must be present in the build for this layer to be usable. This file sets it to `core` and `qcom`. |
| `LAYERSERIES_COMPAT_qcom-3rdparty =` | Yocto Project release series this layer declares compatibility with. BitBake warns when the build's series is not listed. This file sets it to `wrynose`. |
| `BBFILES_DYNAMIC +=` | Recipes and bbappends parsed only when the named layer collection is present in the build; each entry is `<collection>:<glob>`. Adds the `qcom-distro:${LAYERDIR}/dynamic-layers/qcom-distro/*/*/*.bb` and `qcom-distro:${LAYERDIR}/dynamic-layers/qcom-distro/*/*/*.bbappend` gated recipe file patterns. |
