package com.spencer_studios.autoracerretro

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import androidx.core.content.res.ResourcesCompat

fun playerPaint(): Paint {
    return Paint().apply {
        color = Color.parseColor("#4caf50")
        isAntiAlias = true
        strokeWidth = 5f
        style = Paint.Style.FILL
    }
}

fun enemyPaint(): Paint {
    return Paint().apply {
        color = Color.parseColor("#f44336")
        isAntiAlias = true
        strokeWidth = 3f
        style = Paint.Style.FILL
    }
}

fun textPaint(ctx: Context): Paint {
    val tf = ResourcesCompat.getFont(ctx, R.font.press_start_2p)
    return Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        textSize = 25f
        typeface = tf
        style = Paint.Style.FILL_AND_STROKE
    }
}

fun hiScorePaint(ctx: Context): Paint {
    val tf = ResourcesCompat.getFont(ctx, R.font.press_start_2p)
    return Paint().apply {
        color = Color.LTGRAY
        isAntiAlias = true
        textSize = 25f
        typeface = tf
        style = Paint.Style.FILL_AND_STROKE
    }
}