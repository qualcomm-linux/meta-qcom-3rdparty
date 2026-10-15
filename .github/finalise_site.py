# Copyright (c) 2026 DevDocs
# SPDX-License-Identifier: BSD-3-Clause
"""Retain upstream licence notices when distributing generated Sphinx assets.

Run after the HTML builder. Notices come from the installed, locked packages;
no upstream asset is attributed to this repository's author.
"""
from importlib.metadata import distribution
from pathlib import Path

site = Path("docs/site")
notices = {}
for package in ("Sphinx", "snowballstemmer", "alabaster", "Pygments"):
    dist = distribution(package)
    candidates = [f for f in dist.files or []
                  if Path(str(f)).name in ("LICENSE", "LICENSE.rst", "COPYING")]
    if len(candidates) != 1:
        raise RuntimeError(f"Expected one upstream licence for {package}, found {len(candidates)}")
    notices[package] = dist.locate_file(candidates[0]).read_text()

combined = []
for package, notice in notices.items():
    combined.append(f"{package} {distribution(package).version}\n\n{notice.rstrip()}\n")
(site / "THIRD_PARTY_LICENSES.txt").write_text("\n".join(combined))

for asset in sorted((site / "_static").glob("*.js")):
    package = "snowballstemmer" if asset.name in {"base-stemmer.js", "english-stemmer.js"} else "Sphinx"
    header = "/*\n" + notices[package].rstrip() + "\n*/\n"
    original = asset.read_text()
    if not original.startswith(header):
        asset.write_text(header + original)
