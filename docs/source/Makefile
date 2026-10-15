# Run from the repository root: make -f docs/source/Makefile <target>.
# setup installs only the pinned documentation toolchain in the local .venv:
# the locked Python packages, shdoc, which renders shell function comments,
# BitBake, whose statement parser finds the functions in the layer's metadata,
# Mermaid's standalone script, which the build runs in Chromium to draw diagrams
# as SVG, and Playwright's Chromium when no system Chromium is installed.
# html removes stale generated pages, builds the complete site strictly, and
# fails unless every function has a documented, rendered reference entry.
# check rebuilds the site and checks it offline, including that direct-file
# search avoids fetch(). Readers build the site with: setup html.
.PHONY: setup html check

# uv trusts the system certificate store, which holds any proxy's root certificate.
export UV_SYSTEM_CERTS = 1

# shdoc v1.4.
SHDOC_REVISION = b3436134f08428f8bbe2e54bc4dc20da85cd300b
SHDOC_SHA256 = 856bdc62db15e4970c59f011e9a779d6a23e86f4f123707e25f5390d14c9b191
# BitBake as meta-qcom's ci/base.lock.yml pins it at meta-qcom 3dc1ab3db77b; the
# commit hash identifies its content. Only its parser is used: nothing is evaluated or built.
BITBAKE_URL = https://github.com/openembedded/bitbake
BITBAKE_REVISION = 37b9c56ae3b2294ad170859a638c879910c82c59
# Mermaid 12.2.0's standalone build, from the npm package through jsDelivr. It runs
# only during the build; the site contains the SVG it draws, not the script.
MERMAID_URL = https://cdn.jsdelivr.net/npm/mermaid@12.2.0/dist/mermaid.min.js
MERMAID_SHA256 = a8a6a6f805b074a8261a2825c86f99c8054acb3b35f50bb7e24a4f9f2c3c3b9f

setup:
	uv venv --python 3.12 --clear .venv
	uv pip install --python .venv/bin/python -r docs/source/requirements.lock
	curl -sSfL -o .venv/bin/shdoc https://raw.githubusercontent.com/reconquest/shdoc/$(SHDOC_REVISION)/shdoc
	echo "$(SHDOC_SHA256)  .venv/bin/shdoc" | sha256sum -c --quiet -
	git init -q .venv/bitbake
	git -C .venv/bitbake fetch -q --depth 1 $(BITBAKE_URL) $(BITBAKE_REVISION)
	git -C .venv/bitbake -c advice.detachedHead=false checkout -q FETCH_HEAD
	mkdir -p .venv/mermaid
	curl -sSfL -o .venv/mermaid/mermaid.min.js $(MERMAID_URL)
	echo "$(MERMAID_SHA256)  .venv/mermaid/mermaid.min.js" | sha256sum -c --quiet -
	command -v chromium >/dev/null || .venv/bin/playwright install chromium

html:
	rm -rf docs/site docs/source/contributing/.generated
	.venv/bin/sphinx-build -W --keep-going -E -a -b html -c docs/source -d docs/.doctrees . docs/site
	.venv/bin/python .github/finalise_site.py
	.venv/bin/python .github/test_reference_coverage.py check

check: html
	.venv/bin/python .github/check_offline.py docs/site environment
	test -f docs/site/index.html
	grep -q 'SHOW_SEARCH_SUMMARY: false' docs/site/_static/documentation_options.js
