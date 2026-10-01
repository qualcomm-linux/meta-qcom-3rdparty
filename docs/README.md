# Documentation

Build the site from the repository root, then open `docs/site/index.html`
directly in a browser; no HTTP server is needed:

```sh
make -f docs/source/Makefile setup html
```

The site is generated and not committed. Edit the [source](source/README.md) and
follow the contributor
[development environment and build walkthrough](source/contributing/DEVELOPMENT.md).

The build writes `site/THIRD_PARTY_LICENSES.txt`, which preserves the licences
supplied by the locked documentation packages. The build attaches the
upstream notices to redistributed JavaScript without changing its behaviour.

## Folders

- [source/](source/README.md) — Contains the authoritative guides and documentation build configuration.

## Files

- [README.md](README.md) — Explains how to build and open the site, and points to its source.
