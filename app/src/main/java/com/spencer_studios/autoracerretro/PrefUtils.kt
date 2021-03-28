package com.spencer_studios.autoracerretro

import android.content.Context
import androidx.preference.PreferenceManager

class PrefUtils(private val ctx : Context) {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(ctx)

    fun setHiScore(score: Int){
        prefs.edit().putInt("hi", score).apply()
    }

    fun getHiScore() : Int{
        return prefs.getInt("hi", 0)
    }
}