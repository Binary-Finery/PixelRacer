package com.spencer_studios.autoracerretro

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent

class AnimView(context: Context, attr: AttributeSet?) :
    androidx.appcompat.widget.AppCompatImageView(context, attr) {

    private val playerPaint = playerPaint()
    private val enemyPaint = enemyPaint(Color.RED)
    private val h = Handler()

    var screenWidth = 0
    var screenHeight = 0
    var updateCounter = 0
    var touchXPos = 0f
    var score = 0
    var gravity = 0.3f
    val updateFrequency = 10L
    var isGameOver = false
    var tickArr = arrayOf(1, 100, 300, 487, 688, 1000, 1800, 50000)

    private val carRect = Rect(0, 0, 0, screenHeight)

    private val enemies = ArrayList<Enemy>()
    private val velocities = ArrayList<Float>()
    private val textPaint = textPaint(context)

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val sqr = screenWidth / 5
        if (updateCounter < tickArr[tickArr.size - 1]) {
            updateCounter++
            tickArr.forEach { if (updateCounter == it) addEnemy(sqr) }
        }

        for (i in enemies.indices) {
            velocities[i] += gravity
            enemies[i].yPos += velocities[i]
            if (enemies[i].yPos > screenHeight) {
                score++
                velocities[i] = (1..10).random().toFloat()
                enemies[i].yPos = -sqr.toFloat()
                enemies[i].xPos = (0..5).random().toFloat() * sqr
            }
            canvas.drawRect(
                enemies[i].xPos,
                enemies[i].yPos,
                enemies[i].xPos + sqr,
                enemies[i].yPos + sqr,
                enemyPaint
            )
        }
        carRect.set(
            (touchXPos - (sqr / 4)).toInt(),
            ((screenHeight / 4) * 3) - (sqr / 2),
            (touchXPos + (sqr / 4)).toInt(),
            (screenHeight / 4) * 3
        )

        for (i in enemies.indices) {
            if (carRect.intersect(
                    Rect(
                        enemies[i].xPos.toInt(),
                        enemies[i].yPos.toInt(),
                        (enemies[i].xPos + sqr).toInt(),
                        (enemies[i].yPos + sqr).toInt()
                    )
                )
            ) {
                isGameOver = true
                break
            }
        }

        canvas.drawText("$score", 100f, 100f, textPaint)
        canvas.drawRect(carRect, playerPaint)
        if (!isGameOver) {
            h.postDelayed(runner, updateFrequency)
        } else {
            canvas.drawColor(Color.RED)
            canvas.drawText("score: $score", 10f, (screenHeight / 2).toFloat(), textPaint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        this.screenWidth = w
        this.screenHeight = h
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun addEnemy(i : Int) {
        val startX = (0..5).random() * i
        enemies.add(Enemy(startX.toFloat(), -100f))
        velocities.add((1..3).random().toFloat())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_MOVE) {
            if (!isGameOver) {
                touchXPos = event.x
            } else {
                newGame()
            }
        }
        return true
    }

    private val runner = Runnable { invalidate() }

    private fun newGame() {
        updateCounter = 0
        score = 0
        velocities.clear()
        enemies.clear()
        isGameOver = false
        h.postDelayed(runner, updateFrequency)
    }
}
