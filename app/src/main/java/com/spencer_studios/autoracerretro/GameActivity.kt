package com.spencer_studios.autoracerretro

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class GameActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        toolbar.visibility = View.GONE
    }

    override fun onBackPressed() {
        val gv: GameView = findViewById(R.id.gameView)
        gv.terminate()
        startActivity(Intent(this,TitleActivity::class.java))
        finish()
    }
}