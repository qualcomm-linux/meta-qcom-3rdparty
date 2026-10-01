# Copyright (c) 2026 DevDocs
# SPDX-License-Identifier: BSD-3-Clause
"""Verify a copied Sphinx site through file:// with networking disabled.

Run with the generated site path and a search term known to occur in its content.
Requires Playwright and either system Chromium or Playwright's installed Chromium.
The script checks page links, anchors, resource failures, and a search-result click;
it never starts an HTTP server or interacts with the desktop browser.
"""
import json
from functools import partial
import shutil
import sys
import tempfile
from pathlib import Path
from urllib.parse import unquote, urlsplit
from playwright.sync_api import sync_playwright

source = Path(sys.argv[1]).resolve()
query = sys.argv[2]
# Repository maps belong only in the root README, including their source blocks.
for artifact in source.rglob("*"):
    if artifact.is_file() and artifact.suffix in {".html", ".md", ".txt"}:
        assert "REPOSITORY_MAP" not in artifact.name.upper(), artifact
        content = artifact.read_text()
        assert "<!-- repository-map:start -->" not in content, artifact
with tempfile.TemporaryDirectory(prefix='docs-offline-') as tmp:
    site = Path(tmp) / 'copied-site'
    shutil.copytree(source, site)
    errors, requests = [], []
    with sync_playwright() as pw:
        browser = pw.chromium.launch(executable_path=shutil.which("chromium"), headless=True)
        context = browser.new_context(offline=True)
        page = context.new_page()
        page.on('pageerror', partial(errors.append))
        page.on('requestfailed', partial(errors.append))
        page.on('request', partial(requests.append))
        page.goto((site/'index.html').as_uri())
        page.wait_for_url('**/README.html')
        assert page.locator('div.body h1').count() == 1
        visited = 0
        for html in sorted(site.rglob('*.html')):
            if '_downloads' in html.parts or html.name == 'index.html':
                continue
            page.goto(html.as_uri())
            for link in page.locator('[href], [src]').evaluate_all('(nodes) => nodes.map(n => n.getAttribute("href") || n.getAttribute("src"))'):
                parsed = urlsplit(link)
                if parsed.scheme or parsed.netloc or link.startswith('javascript:'):
                    continue
                target = (html.parent / unquote(parsed.path)).resolve() if parsed.path else html
                assert target.is_relative_to(site), (html, link, 'outside copied site')
                assert target.exists(), (html, link, 'missing')
                if parsed.fragment and target.suffix == '.html':
                    contents = target.read_text()
                    fragment = unquote(parsed.fragment)
                    assert f'id="{fragment}"' in contents or f'name="{fragment}"' in contents, (html, link, 'anchor')
            visited += 1
        page.goto((site/'search.html').as_uri()+'?q='+query)
        page.wait_for_function("document.querySelector('#search-results')?.textContent.includes('Search finished')")
        result = page.locator('#search-results li a').first
        assert result.count()
        result.click()
        page.wait_for_load_state()
        assert page.url.startswith(site.as_uri())
        assert not errors, errors
        network_requests = [request.url for request in requests if request.url.startswith(("http:", "https:"))]
        assert not network_requests, network_requests
        print(json.dumps({'site':str(source),'pages':visited,'search':query,'result':page.url.split('/copied-site/')[-1],'network_requests':network_requests,'browser_errors':errors}))
        browser.close()
