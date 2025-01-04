package com.ifechukwu.deviceguard

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.os.Binder
import android.os.HardwarePropertiesManager
import android.os.UserManager
import android.provider.Settings
import android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
import timber.log.Timber
import java.io.File


/**
 * @Author: ifechukwu.udorji
 * @Date: 12/30/2024
 */
class DeviceController(
    private val devicePolicyManager: DevicePolicyManager,
    private val componentName: ComponentName,
    private val context: Context
) {
    private fun isDeviceOwner(): Boolean {
        return devicePolicyManager.isDeviceOwnerApp(context.packageName)
    }

    fun lockDevice() {
        if (isDeviceOwner()) {
            devicePolicyManager.lockNow()
        }
    }

    fun wipeDevice() {
        if (isDeviceOwner()) {
            devicePolicyManager.wipeData(0)
        }
    }

    fun disableAdmin() {
        if (isDeviceOwner()) {
            devicePolicyManager.removeActiveAdmin(componentName)
        }
    }

    fun rebootDevice() {
        if (isDeviceOwner()) {
            devicePolicyManager.reboot(componentName)
        }
    }

    /**
     * Reset password restrictions for device admins now apply to profile owners.
     * Device admins can no longer use DevicePolicyManager.resetPassword() to clear
     * passwords or change ones that are already set.
     * Device admins can still set a password, but only when the device has no password, PIN, or pattern.
     */
    fun resetPassword(newPassword: String) {
        if (isDeviceOwner()) {
            try {
                devicePolicyManager.resetPassword(newPassword, 0)
            } catch (e: SecurityException) {
                Timber.e(e, "Failed to reset password")
            }
        }
    }

    /**
     * Enables kiosk mode (lock task mode)
     * @param allowedPackages List of packages allowed to run in kiosk mode
     */
    fun enableKioskMode(allowedPackages: Array<String>) {
        if (!isDeviceOwner()) {
            throw SecurityException("Device owner privileges required")
        }


        // Set allowed packages for lock task mode
        devicePolicyManager.setLockTaskPackages(componentName, allowedPackages)

        setupKioskPolicies(true)

        // Start lock task mode
        if (context is Activity) {
            context.startLockTask()
        }
    }

    private fun setupKioskPolicies(active: Boolean) {
        // Configure kiosk mode policies
        setUserRestriction(UserManager.DISALLOW_SAFE_BOOT, active)
        setUserRestriction(UserManager.DISALLOW_FACTORY_RESET, active)
        setUserRestriction(UserManager.DISALLOW_ADD_USER, active)

        // disable keyguard and status bar
        devicePolicyManager.setKeyguardDisabled(componentName, active);
        devicePolicyManager.setStatusBarDisabled(componentName, active);

        // enable STAY_ON_WHILE_PLUGGED_IN
        enableStayOnWhilePluggedIn(active);
    }

    /**
     * Disables kiosk mode
     */
    fun disableKioskMode() {
        if (context is Activity) {
            context.stopLockTask()
        }

        setupKioskPolicies(false)
    }

    /**
     * Configures screen capture settings
     * @param disabled Whether screen capture should be allowed
     */
    fun disableScreenCapture(disabled: Boolean) {
        if (!isDeviceOwner()) {
            throw SecurityException("Device owner privileges required")
        }

        devicePolicyManager.setScreenCaptureDisabled(componentName, disabled)
    }

    fun setDeviceOwnerLockScreenInfo(message: String?) {
        if (!isDeviceOwner()) {
            throw SecurityException("Device owner privileges required")
        }

        devicePolicyManager.setDeviceOwnerLockScreenInfo(componentName, message)
    }

    fun setShortSupportMessage(message: String?) {
        if (!isDeviceOwner()) {
            throw SecurityException("Device owner privileges required")
        }

        devicePolicyManager.setShortSupportMessage(componentName, message)
    }

    fun setLongSupportMessage(message: String?) {
        if (!isDeviceOwner()) {
            throw SecurityException("Device owner privileges required")
        }

        devicePolicyManager.setLongSupportMessage(componentName, message)
    }

    fun lockDeviceWallpaper(lockWallpaper: Boolean) {
        if (!isDeviceOwner()) {
            throw SecurityException("Device owner privileges required")
        }

        setUserRestriction(UserManager.DISALLOW_SET_WALLPAPER, lockWallpaper)
    }

    fun monitorDeviceHealth() {
        if (!isDeviceOwner()) {
            throw SecurityException("Device owner privileges required")
        }

        val hardwarePropertiesManager =
            context.getSystemService(Context.HARDWARE_PROPERTIES_SERVICE) as HardwarePropertiesManager
    }

    fun suspendApps(packageNames: Array<String>, suspend: Boolean) {
        if (!isDeviceOwner()) {
            throw SecurityException("Device owner privileges required")
        }

        devicePolicyManager.setPackagesSuspended(componentName, packageNames, suspend)
    }

    fun blockUnknownSources(block: Boolean) {
        if (!isDeviceOwner()) {
            throw SecurityException("Device owner privileges required")
        }

        setUserRestriction(UserManager.DISALLOW_INSTALL_UNKNOWN_SOURCES, block)
    }

    fun onEnabled() {
        val userManager = context.getSystemService(Context.USER_SERVICE) as UserManager
        val serialNumber = userManager.getSerialNumberForUser(Binder.getCallingUserHandle())
        Timber.i("Device admin enabled in user with serial number: $serialNumber")
    }

    private fun enableStayOnWhilePluggedIn(enabled: Boolean) {
        if (enabled) {
            devicePolicyManager.setGlobalSetting(
                componentName,
                Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
                (BatteryManager.BATTERY_PLUGGED_AC
                        or BatteryManager.BATTERY_PLUGGED_USB
                        or BatteryManager.BATTERY_PLUGGED_WIRELESS).toString()
            )

            if (context is Activity) {
                context.window.addFlags(FLAG_KEEP_SCREEN_ON)
            }
        } else {
            devicePolicyManager.setGlobalSetting(
                componentName,
                Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
                "0"
            )

            if (context is Activity) {
                context.window.clearFlags(FLAG_KEEP_SCREEN_ON)
            }
        }
    }

    private fun setUserRestriction(restriction: String, disallow: Boolean) {
        if (disallow) {
            devicePolicyManager.addUserRestriction(componentName, restriction)
        } else {
            devicePolicyManager.clearUserRestriction(componentName, restriction)
        }
    }
}