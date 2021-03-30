package com.spencer_studios.autoracerretro

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_title.*
import kotlinx.android.synthetic.main.content_title.*

class TitleActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title)
        setSupportActionBar(findViewById(R.id.toolbar))

        toolbar.visibility = View.GONE

        val hi = PrefUtils(this).getHiScore()
        tvHiScore.text = "HI $hi"

        btnPlay.setOnClickListener {
            startActivity(Intent(it.context, GameActivity::class.java))
            finish()
        }
    }
}