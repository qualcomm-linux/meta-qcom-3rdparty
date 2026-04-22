# Security recommendation for Production Build:

## 1. Debugfs

By default debugfs is enabled to make it user friendly for debugging. Commercial products are not advised to have this enabled. We recommend deployers to disable debugfs as part of production deployment customization.

Disable via kernel configuration:

DebugFS is controlled by the kernel configuration option `CONFIG_DEBUG_FS`.

For commercial/production builds, it should be disabled at build time by setting:

```text
CONFIG_DEBUG_FS=n
````

This can be achieved by updating the kernel defconfig or by applying a kernel configuration fragment in the Yocto build. Disabling this option prevents debugfs from being built and mounted, reducing exposure of internal kernel debug interfaces in production images.

**Additional build‑level safeguard:**

In addition to the kernel configuration, production builds should ensure that debug‑related build options are disabled.

For example, in meta-qcom CI configurations, setting:

```text
DEBUG_BUILD = "0"
```

disables DebugFS along with other debug‑related features as part of a non‑debug build profile. This provides an additional safeguard to ensure that debug functionality is not inadvertently enabled in production images.

## 2. Kernel hardening flags

Kernel security restrictions and self-protection (“kernel hardening”) can be enabled via distribution-specific configuration.

For commercial/production builds, enabling available kernel hardening features is recommended to enhance system security and reduce the attack surface.

Depending on the Yocto setup, this may be achieved by enabling a `hardened` distribution feature (if supported) via the appropriate configuration mechanism, such as a distro configuration, layer override, or `local.conf`.

For example:

```text
DISTRO_FEATURES:append = " hardened"
```

## 3. Logging and kernel information exposure restrictions

For commercial/production builds, it is recommended to restrict kernel logging and kernel information exposure to reduce the attack surface and prevent leakage of sensitive kernel details.

### 3.1 Kernel log rate limiting (printk)

Rate limiting for kernel log output (for example via `/dev/kmsg` or the serial console) can be configured using kernel command-line parameters.

For production builds, enabling printk rate limiting is recommended to avoid excessive kernel log exposure. This is typically configured via the kernel command line set by the bootloader or Yocto kernel command line configuration.

For example:

```text
printk.devkmsg=ratelimit
```

### 3.2 Kernel pointer restriction (kptr\_restrict)

Kernel pointer exposure can be restricted using the `kptr_restrict` sysctl.

For commercial/production builds, it is recommended to set this to restrict kernel pointer visibility to privileged users only.

This should be enabled by default using persistent sysctl configuration (as part of the image), rather than manual runtime commands.

For example:

```text
kernel.kptr_restrict = 2
```

### 3.3 Kernel log access restriction (dmesg\_restrict)

Access to kernel logs via `dmesg` can be restricted so that only privileged users are allowed to read kernel messages.

For production builds, it is recommended to enable this restriction by default using persistent sysctl configuration.

For example:

```text
kernel.dmesg_restrict = 1
```

## 4. Signed module loading enforcement

Loading of unsigned kernel modules can weaken kernel integrity and bypass intended security controls.

For commercial/production builds, it is recommended to enable kernel module signature verification and enforce rejection of unsigned modules.

This is implemented via kernel configuration, by enabling module signature support and enforcing signature verification at module load time.

For example, this typically involves enabling kernel options such as:

```text
CONFIG_MODULE_SIG=Y
CONFIG_MODULE_SIG_FORCE=Y
```

The exact configuration and key management mechanism is platform- and distribution-specific and should be integrated as part of the production kernel build.

## 5. Userspace hardening

OpenEmbedded provides a standard set of userspace security hardening compiler and linker flags via **security\_flags.inc**
(<https://git.openembedded.org/openembedded-core/tree/meta/conf/distro/include/security_flags.inc>).

These flags are already part of the build and must remain enabled for production images to ensure adequate userspace exploit mitigations. Any deviation from these defaults should be avoided or explicitly justified.

## 6. ADB security recommendations for production

### ADB / adbd restrictions

By default `adbd` runs as root on yocto, which means `adb shell` provides root access.
Commercial/production products should not allow root access via ADB because it weakens overall device security.

### Recommendation

In production builds, `adbd` must not run as root.

Limit root ADB access to development/debug use only.

**Deployers are expected to disable root ADB access as part of production deployment customization before commercialization.**
