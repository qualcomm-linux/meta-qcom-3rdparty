# Backporting to meta-qcom-3rdparty (wrynose LTS)

`wrynose` is the **LTS Stable branch**. It is not the primary development
branch: `main` is. Every change is expected to land on `main` first and then
be **backported** here, unless it is specific to `wrynose` (e.g. it does not
apply to `main`, or `main` has diverged in a way that makes the change
meaningless there).

1. Land the change on **main** first, following the main contribution
   workflow (fork, topic branch, rebase on latest upstream `main`, open a
   PR). Label the pull request `backport wrynose` and the backport PR is
   opened automatically once it is merged; the steps below are for when that
   fails (conflicts) or the change is wrynose-only.
2. Once it is merged on `main`, backport the merged commit(s) to `wrynose`
   using **`git cherry-pick -x`** so the backport records the original commit
   hash in the message (`(cherry picked from commit <sha>)`):

   ```sh
   git fetch origin
   git checkout -b backport/<PR-or-topic>-to-wrynose origin/wrynose
   git cherry-pick -x <sha> [<sha> ...]      # -x records the source commit
   # resolve conflicts if any, keeping the change minimal and equivalent
   ```

3. Run the CI-equivalent checks (see below).
4. Open a GitHub pull request targeting **wrynose**, following the
   `[Backport wrynose]` subject convention (see [Commit messages for
   backports](#commit-messages-for-backports)).
5. Use PR discussion for review iteration.

## Before opening/updating a backport PR

Run the CI-equivalent checks in this order:

```sh
ci/kas-container-shell-helper.sh ci/yocto-patchreview.sh
ci/kas-container-shell-helper.sh ci/yocto-check-layer.sh
```

## Commit messages for backports

For backports, preserve the original main commit message and metadata, and
keep the `cherry-pick -x` trailer intact. The `[Backport wrynose]` prefix
belongs to the **pull request subject only**, following the practice
established in meta-qcom:

- `[Backport wrynose] firmware-qcom-boot-rubikpi3: use QLI2.0 boot assets (#63)`
- `[Backport wrynose] ci: add rubikpi3 kas fragment (#41)`

Never add the prefix to a commit subject: the commits are normal patches
whose only backport marker is the cherry-pick line. The backported body
should retain the original explanation and end with the trailer that
`git cherry-pick -x` adds:

```text
(cherry picked from commit <sha>)
```

`git cherry-pick` preserves the original author's `Signed-off-by`; add your
own with `git cherry-pick -s` if your workflow requires it.
