package com.spencer_studios.autoracerretro

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import kotlin.math.ceil

class GameView(context: Context, attr: AttributeSet?) :
    androidx.appcompat.widget.AppCompatImageView(context, attr) {

    private val playerPaint = playerPaint()
    private val enemyPaint = enemyPaint()
    private val scoreTextPaint = textPaint(context)
    private val hiScoreTextPaint = hiScorePaint(context)

    private val blocks = ArrayList<Block>()
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
    var blockDims = 0
    var hasCalculatedValues = false
    var isGameOver = false
    var userTerminated = false
    lateinit var tickArr: Array<Int>

    private val playerRect = Rect(0, 0, 0, screenHeight)

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!userTerminated) {
            if (!hasCalculatedValues) {
                tickArr = launchFrequency()
                minYVelocity = ceil((0.07 * screenHeight) / 100f).toInt()
                maxYVelocity = ceil((0.68 * screenHeight) / 100f).toInt()
                gravity = (0.0272f * screenHeight) / 100f
                blockDims = screenWidth / verts
                touchXPos = (screenWidth / 2).toFloat()
                scoreTextPaint.textSize = blockDims.toFloat() / 2
                hiScoreTextPaint.textSize = blockDims.toFloat() / 5
                hasCalculatedValues = true
            }

            if (updateCounter < tickArr[tickArr.size - 1]) {
                updateCounter++
                tickArr.forEach {
                    if (updateCounter == it) addBlock()
                }
            }

            for (i in blocks.indices) {
                velocities[i] += gravity
                blocks[i].yPos += velocities[i]
                if (blocks[i].yPos > screenHeight) {
                    score++
                    if (score > hiScore) hiScore = score
                    velocities[i] = rndV()
                    blocks[i].yPos = -blockDims.toFloat()
                    blocks[i].xPos = rndX()
                }
                canvas.drawRect(
                    blocks[i].xPos,
                    blocks[i].yPos,
                    blocks[i].xPos + blockDims,
                    blocks[i].yPos + blockDims,
                    enemyPaint
                )
            }
            playerRect.set(
                (touchXPos - (blockDims / 4)).toInt(),
                ((screenHeight / 4) * 3) - (blockDims / 2),
                (touchXPos + (blockDims / 4)).toInt(),
                (screenHeight / 4) * 3
            )

            for (i in blocks.indices) {
                if (playerRect.intersect(
                        Rect(
                            blocks[i].xPos.toInt(),
                            blocks[i].yPos.toInt(),
                            (blocks[i].xPos + blockDims).toInt(),
                            (blocks[i].yPos + blockDims).toInt()
                        )
                    )
                ) {
                    isGameOver = true
                    break
                }
            }

            canvas.drawText(
                "HI $hiScore",
                (blockDims / 2).toFloat(),
                (blockDims).toFloat() / 2,
                hiScoreTextPaint
            )
            canvas.drawText(
                "$score",
                (blockDims / 2).toFloat(),
                (blockDims).toFloat() + 10,
                scoreTextPaint
            )
            canvas.drawRect(playerRect, playerPaint)

            if (!isGameOver) h.postDelayed(runner, updateFrequency)
            else {
                canvas.drawText(
                    resources.getString(R.string.replay_game_message),
                    (blockDims/5).toFloat(),
                    (screenHeight / 2).toFloat(),
                    hiScoreTextPaint
                )
                val prefs = PrefUtils(context)
                val hi = prefs.getHiScore()
                if (score > hi) prefs.setHiScore(score)
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

    private fun addBlock() {
        blocks.add(Block(rndX(), -(blockDims * 2).toFloat()))
        velocities.add(rndV())
    }

    private fun rndX(): Float = ((0 until verts).random() * blockDims).toFloat()
    private fun rndV(): Float = (minYVelocity..maxYVelocity).random().toFloat()

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_MOVE && !isGameOver) touchXPos = event.x
        if (event?.action == MotionEvent.ACTION_DOWN && isGameOver) newGame()
        return true
    }

    private val runner = Runnable { invalidate() }

    private fun newGame() {
        updateCounter = 0
        score = 0
        velocities.clear()
        blocks.clear()
        isGameOver = false
        hasCalculatedValues = false
        h.postDelayed(runner, updateFrequency)
    }
}
