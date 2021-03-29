package com.spencer_studios.autoracerretro

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import kotlin.math.ceil

class GameView(context: Context, attr: AttributeSet?) :
    androidx.appcompat.widget.AppCompatImageView(context, attr) {

    private val playerPaint = playerPaint()
    private val enemyPaint = enemyPaint()
    private val textPaint = textPaint(context)
    private val hiScoreTextPaint = hiScorePaint(context)

    private val enemies = ArrayList<Enemy>()
    private val velocities = ArrayList<Float>()

    private val h = Handler()

    var hiScore = PrefUtils(context).getHiScore()

    val updateFrequency = 5L
    val verts = 6

    var screenWidth = 0
    var screenHeight = 0
    var updateCounter = 0
    var touchXPos = 0f
    var score = 0
    var gravity = 0f
    var minYVelocity = 0
    var maxYVelocity = 0
    var hasCalculatedYVelocity = false
    var isGameOver = false
    var userTerminated = false
    var tickArr = launchFrequency()

    private val carRect = Rect(0, 0, 0, screenHeight)

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!userTerminated) {

            val boxDims = screenWidth / verts

            if (!hasCalculatedYVelocity) {
                minYVelocity = ceil((0.07 * screenHeight) / 100f).toInt()
                maxYVelocity = ceil((0.68 * screenHeight) / 100f).toInt()
                gravity = (0.0272f * screenHeight) / 100f
                touchXPos = (screenWidth / 2).toFloat()
                textPaint.textSize = boxDims.toFloat() / 2
                hiScoreTextPaint.textSize = boxDims.toFloat() / 5
                hasCalculatedYVelocity = true
            }

            if (updateCounter < tickArr[tickArr.size - 1]) {
                updateCounter++
                tickArr.forEach {
                    if (updateCounter == it) {
                        addEnemy(boxDims)
                    }
                }
            }

            for (i in enemies.indices) {
                velocities[i] += gravity
                enemies[i].yPos += velocities[i]
                if (enemies[i].yPos > screenHeight) {
                    score++
                    if (score > hiScore) hiScore = score
                    velocities[i] = (minYVelocity..maxYVelocity).random().toFloat()
                    enemies[i].yPos = -boxDims.toFloat()
                    enemies[i].xPos = (0 until verts).random().toFloat() * boxDims
                }
                canvas.drawRect(
                    enemies[i].xPos,
                    enemies[i].yPos,
                    (enemies[i].xPos + boxDims),
                    (enemies[i].yPos + boxDims),
                    enemyPaint
                )
            }
            carRect.set(
                (touchXPos - (boxDims / 4)).toInt(),
                ((screenHeight / 4) * 3) - (boxDims / 2),
                (touchXPos + (boxDims / 4)).toInt(),
                (screenHeight / 4) * 3
            )

            for (i in enemies.indices) {
                if (carRect.intersect(
                        Rect(
                            enemies[i].xPos.toInt(),
                            enemies[i].yPos.toInt(),
                            (enemies[i].xPos + boxDims).toInt(),
                            (enemies[i].yPos + boxDims).toInt()
                        )
                    )
                ) {
                    isGameOver = true
                    break
                }
            }

            canvas.drawText(
                "HI $hiScore",
                (boxDims / 2).toFloat(),
                (boxDims).toFloat() / 2,
                hiScoreTextPaint
            )
            canvas.drawText("$score", (boxDims / 2).toFloat(), (boxDims).toFloat() + 10, textPaint)

            canvas.drawRect(carRect, playerPaint)

            if (!isGameOver) {
                h.postDelayed(runner, updateFrequency)
            } else {
                //textPaint.textSize = (boxDims / 4).toFloat()
                canvas.drawText("you scored $score", 10f, (screenHeight / 2).toFloat(), hiScoreTextPaint)
                canvas.drawText(
                    "tap to play again",
                    10f,
                    (screenHeight / 2).toFloat() + (boxDims / 2),
                    hiScoreTextPaint
                )
                val prefs = PrefUtils(context)
                val hi = prefs.getHiScore()
                if (score > hi) {
                    prefs.setHiScore(score)
                }
            }
        } else {
            h.removeCallbacks(runner)
        }
    }

    fun terminate() {
        h.removeCallbacks(runner)
        userTerminated = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        this.screenWidth = w
        this.screenHeight = h
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun addEnemy(i: Int) {
        enemies.add(Enemy(((0 until verts).random() * i).toFloat(), -(i * 2).toFloat()))
        velocities.add((1..3).random().toFloat())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_MOVE) {
            if (!isGameOver) {
                touchXPos = event.x
            }
        }
        if (event?.action == MotionEvent.ACTION_DOWN) {
            if (isGameOver) {
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
        hasCalculatedYVelocity = false
        h.postDelayed(runner, updateFrequency)
    }
}
