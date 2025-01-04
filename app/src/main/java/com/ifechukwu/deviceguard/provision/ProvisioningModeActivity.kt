package com.ifechukwu.deviceguard.provision

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.Intent
import android.os.Bundle
import com.ifechukwu.deviceguard.databinding.ActivityProvisioningModeBinding

class ProvisioningModeActivity : Activity() {
    private lateinit var binding: ActivityProvisioningModeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProvisioningModeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.setOnClickListener {
            val intent = Intent()
            intent.putExtra(
                DevicePolicyManager.EXTRA_PROVISIONING_MODE,
                DevicePolicyManager.PROVISIONING_MODE_FULLY_MANAGED_DEVICE
            )
            finishWithIntent(intent)
        }
    }

    private fun finishWithIntent(intent: Intent) {
        setResult(RESULT_OK, intent)
        finish()
    }
}