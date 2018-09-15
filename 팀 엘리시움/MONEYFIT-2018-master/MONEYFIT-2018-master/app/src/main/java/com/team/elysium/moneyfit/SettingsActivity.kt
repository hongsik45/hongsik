package com.team.elysium.moneyfit

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton

/**
 * Created by sh on 2018-08-02.
 */
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_settings)


        initListener()
    }

    private fun initListener() {
        val backButton = findViewById<ImageButton>(R.id.settings_close_button)
        backButton.setOnClickListener { onBackPressed() }
    }
}