# Documentation source

Start with the [usage tutorial](user/USAGE.md), or follow the
[development setup](contributing/DEVELOPMENT.md) to build this site and run its
checks.

## Folders

- [.templates/](.templates/) — Supplies the generated site's entry-point redirect.
- [contributing/](contributing/README.md) — Holds the contribution, agent, and development guides and the function reference.
- [user/](user/README.md) — Holds the usage tutorial and the configuration reference.

## Files

- [Makefile](Makefile) — Provides the shared local and CI setup, build, and check commands.
- [README.md](README.md) — Introduces the documentation source.
- [conf.py](conf.py) — Configures Markdown rendering, the site's links to repository files, and search.
- [requirements.lock](requirements.lock) — Locks direct and transitive documentation dependencies for reproducible builds.
- [requirements.txt](requirements.txt) — Pins the documentation packages.
