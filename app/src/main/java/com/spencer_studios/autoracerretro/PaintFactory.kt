package com.spencer_studios.autoracerretro

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import androidx.core.content.res.ResourcesCompat

fun playerPaint(): Paint {
    return Paint().apply {
        color = Color.GREEN
        isAntiAlias = true
        strokeWidth = 5f
        style = Paint.Style.FILL
    }
}

fun enemyPaint(col : Int): Paint {
    return Paint().apply {
        color = col
        isAntiAlias = true
        strokeWidth = 3f
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.FILL_AND_STROKE
    }
}

fun textPaint(ctx: Context): Paint {
    val tf = ResourcesCompat.getFont(ctx, R.font.press_start_2p)
    return Paint().apply {
        color = Color.BLUE
        isAntiAlias = true
        textSize = 25f
        typeface = tf
        style = Paint.Style.FILL_AND_STROKE
    }
}