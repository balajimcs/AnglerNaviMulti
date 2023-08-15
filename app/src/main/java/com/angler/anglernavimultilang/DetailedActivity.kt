package com.angler.anglernavimultilang

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.angler.anglernavimultilang.databinding.ActivityDetailedBinding

class DetailedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imageResId = intent.getIntExtra("imageResId", R.drawable.angler1)
        val headingText = intent.getStringExtra("heading")

        binding.detailedImage.setImageResource(imageResId)
        binding.detailedText.text = headingText
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
