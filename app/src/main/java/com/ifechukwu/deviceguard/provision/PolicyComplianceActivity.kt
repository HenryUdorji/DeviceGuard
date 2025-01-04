package com.ifechukwu.deviceguard.provision;

import android.app.Activity
import androidx.core.bundle.Bundle
import com.ifechukwu.deviceguard.databinding.ActivityPolicyComplianceBinding

class PolicyComplianceActivity: Activity() {
    private lateinit var binding: ActivityPolicyComplianceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPolicyComplianceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAccept.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }
}
