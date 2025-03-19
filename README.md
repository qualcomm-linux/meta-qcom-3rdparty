# meta-qcom-3rdparty

## Introduction

OpenEmbedded/Yocto Project BSP layer for Third-Party Maintained Qualcomm based
platforms.

This layers provides additional recipes and machine configuration files for
Third-Party Maintained Qualcomm platforms. Reference boards that are officially
supported by Qualcomm are available via ``meta-qcom`` / ``meta-qcom-hwe`` instead.

This layer depends on:

```
URI: https://github.com/openembedded/openembedded-core.git
layers: meta
branch: kirkstone
revision: HEAD

URI: https://github.com/qualcomm-linux/meta-qcom.git
branch: kirkstone
revision: HEAD

URI: https://github.com/qualcomm-linux/meta-qcom-hwe.git
branch: kirkstone
revision: HEAD

```

## Branches

- **main:** Primary development branch, with focus on upstream support and
  compatibility with the most recent Yocto Project release.
- **kirkstone:** Qualcomm Linux <= 1.3, aligned with Yocto Project 4.0 (LTS).

## Machine Support

See `conf/machine` for the complete list of supported devices.

## Contributing

Please submit any patches against the `meta-qcom-3rdparty` layer by using
the GitHub pull-request feature. Fork the repo, create a branch,
do the work, rebase from upstream, and create the pull request.

For some useful guidelines when submitting patches, please refer to:
[Preparing Changes for Submission](https://docs.yoctoproject.org/dev/contributor-guide/submit-changes.html#preparing-changes-for-submission)

Pull requests will be discussed within the GitHub pull-request infrastructure.

## Communication

- **GitHub Issues:** [meta-qcom-3rdparty issues](https://github.com/qualcomm-linux/meta-qcom-3rdparty/issues)
- **Pull Requests:** [meta-qcom-3rdparty pull requests](https://github.com/qualcomm-linux/meta-qcom-3rdparty/pulls)

## Maintainer(s)

* Ricardo Salveti <ricardo.salveti@oss.qualcomm.com>
* Nicolas Dechesne <nicolas.dechesne@oss.qualcomm.com>

## License

This layer is licensed under the MIT license. Check out [COPYING.MIT](COPYING.MIT)
for more detais.
