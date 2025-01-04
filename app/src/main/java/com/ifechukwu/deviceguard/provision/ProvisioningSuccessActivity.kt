package com.ifechukwu.deviceguard.provision

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ifechukwu.deviceguard.R
import com.ifechukwu.deviceguard.databinding.ActivityProvisioningSuccessBinding

class ProvisioningSuccessActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProvisioningSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProvisioningSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}