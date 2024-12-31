package com.ifechukwu.deviceguard

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context

/**
 * @Author: ifechukwu.udorji
 * @Date: 12/30/2024
 */
class DeviceController(
    private val devicePolicyManager: DevicePolicyManager,
    private val componentName: ComponentName,
    private val context: Context
) {
    fun lockDevice() {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.lockNow()
        }
    }

    fun wipeDevice() {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.wipeData(0)
        }
    }

    fun disableAdmin() {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.removeActiveAdmin(componentName)
        }
    }

    fun rebootDevice() {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.reboot(componentName)
        }
    }

    fun resetPassword(newPassword: String) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.resetPassword(newPassword, 0)
        }
    }

    fun hideApplication(hidden: Boolean) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.setApplicationHidden(componentName, context.packageName, hidden)
        }
    }
}