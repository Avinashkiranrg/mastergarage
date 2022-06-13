package com.vendor.mastergarage.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vendor.mastergarage.R
import com.vendor.mastergarage.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    lateinit var binding:ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_left)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.toolbar.setOnClickListener {
            onBackPressed()
        }



    }
}