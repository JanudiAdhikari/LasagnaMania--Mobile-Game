package com.example.lasagnamania

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import android.media.MediaPlayer

class GarfieldView(context: Context) : View(context) {
    private val garfield = arrayOfNulls<Bitmap>(2)
    private var garfieldX = 10
    private var garfieldY = 0
    private var garfieldSpeed = 0

    private var canvasWidth = 0
    private var canvasHeight = 0

    private lateinit var lasagnaBitmap: Bitmap
    private lateinit var pizzaBitmap: Bitmap
    private lateinit var vegBitmap: Bitmap

    private var lasagnaX = 0
    private var lasagnaY = 0
    private var lasagnaSpeed = 20
    private val lasagnaPaint = Paint()

    private var pizzaX = 0
    private var pizzaY = 0
    private var pizzaSpeed = 15
    private val pizzaPaint = Paint()

    private var vegX = 0
    private var vegY = 0
    private var vegSpeed = 25
    private val vegPaint = Paint()

    private var score = 0

    private var lifeCounterOfGarfield = 0

    private var touch = false

    private val backgroundImage: Bitmap
    private val scorePaint = Paint()
    private val life = arrayOfNulls<Bitmap>(2)

    // Declare MediaPlayer instances
    private lateinit var mediaPlayer1: MediaPlayer
    private lateinit var mediaPlayer2: MediaPlayer

    init {
        // Load original bitmaps
        val originalGarfield = BitmapFactory.decodeResource(resources, R.drawable.dash1)
        val originalGarfieldFail = BitmapFactory.decodeResource(resources, R.drawable.dash1)
        val originalHeartFill = BitmapFactory.decodeResource(resources, R.drawable.heartfill)
        val originalHeartEmpty = BitmapFactory.decodeResource(resources, R.drawable.heartempty)

        val originalLasagna = BitmapFactory.decodeResource(resources, R.drawable.lasagna)
        val originalPizza = BitmapFactory.decodeResource(resources, R.drawable.pizza)
        val originalVeg = BitmapFactory.decodeResource(resources, R.drawable.brocoli)


        // Set desired width and height for scaled bitmaps
        val desiredWidthHeart = 100
        val desiredHeightHeart = 100
        val desiredWidthGarfield = 290
        val desiredHeightGarfield = 300

        val desiredWidthLasagna = 100
        val desiredHeightLasagna = 100
        val desiredWidthPizza = 100
        val desiredHeightPizza = 100
        val desiredWidthVeg = 100
        val desiredHeightVeg = 100

        lasagnaBitmap = Bitmap.createScaledBitmap(originalLasagna, desiredWidthLasagna, desiredHeightLasagna, false)
        pizzaBitmap = Bitmap.createScaledBitmap(originalPizza, desiredWidthPizza, desiredHeightPizza, false)
        vegBitmap = Bitmap.createScaledBitmap(originalVeg, desiredWidthVeg, desiredHeightVeg, false)


        // Scale bitmaps
        garfield[0] = Bitmap.createScaledBitmap(originalGarfield, desiredWidthGarfield, desiredHeightGarfield, false)
        garfield[1] = Bitmap.createScaledBitmap(originalGarfieldFail, desiredWidthGarfield, desiredHeightGarfield, false)
        life[0] = Bitmap.createScaledBitmap(originalHeartFill, desiredWidthHeart, desiredHeightHeart, false)
        life[1] = Bitmap.createScaledBitmap(originalHeartEmpty, desiredWidthHeart, desiredHeightHeart, false)

        backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.gameback)

        lasagnaPaint.color = Color.YELLOW
        lasagnaPaint.isAntiAlias = false
        pizzaPaint.color = Color.BLUE
        pizzaPaint.isAntiAlias = false

        vegPaint.color = Color.RED
        vegPaint.isAntiAlias = false

        scorePaint.color = Color.WHITE
        scorePaint.textSize = 70f
        scorePaint.typeface = Typeface.DEFAULT_BOLD
        scorePaint.isAntiAlias = true

        garfieldY = 550
        score = 0
        lifeCounterOfGarfield = 3

        // Initialize MediaPlayer instances
        mediaPlayer1 = MediaPlayer.create(context, R.raw.eating)
        mediaPlayer2 = MediaPlayer.create(context, R.raw.crash)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmap(backgroundImage, 0f, 0f, null)

        canvasWidth = canvas.width
        canvasHeight = canvas.height

        val minGarfieldY = garfield[0]!!.height
        val maxGarfieldY = canvasHeight - garfield[0]!!.height * 1.5
        garfieldY += garfieldSpeed

        if (garfieldY < minGarfieldY) {
            garfieldY = minGarfieldY
        }
        if (garfieldY > maxGarfieldY) {
            garfieldY = maxGarfieldY.toInt()

        }
        garfieldSpeed += 2

        if (touch) {
            canvas.drawBitmap(garfield[1]!!, garfieldX.toFloat(), garfieldY.toFloat(), null)
            touch = false
        } else {
            canvas.drawBitmap(garfield[0]!!, garfieldX.toFloat(), garfieldY.toFloat(), null)
        }

        //Lasagna
        lasagnaX -= lasagnaSpeed

        if (hitLasagnaChecker(lasagnaX, lasagnaY)) {
            score += 10
            lasagnaX = -100
            mediaPlayer1.start()
        }

        if (lasagnaX < 0) {
            lasagnaX = canvasWidth + 21
            lasagnaY = (Math.floor(Math.random() * (maxGarfieldY - minGarfieldY)) + minGarfieldY).toInt()
        }

        canvas.drawBitmap(lasagnaBitmap, lasagnaX.toFloat(), lasagnaY.toFloat(), null)

        //Pizza
        pizzaX -= pizzaSpeed

        if (hitLasagnaChecker(pizzaX, pizzaY)) {
            score += 50
            pizzaX = -100
            mediaPlayer1.start()
        }

        if (pizzaX < 0) {
            pizzaX = canvasWidth + 21
            pizzaY = (Math.floor(Math.random() * (maxGarfieldY - minGarfieldY)) + minGarfieldY).toInt()
        }

        canvas.drawBitmap(pizzaBitmap, pizzaX.toFloat(), pizzaY.toFloat(), null)

        //Veg
        vegX -= vegSpeed

        if (hitLasagnaChecker(vegX, vegY)) {
            score -= 20
            vegX = -100
            lifeCounterOfGarfield--
            mediaPlayer2.start()

            if(lifeCounterOfGarfield == 0){
                Toast.makeText(context, "Game Over", Toast.LENGTH_SHORT).show()

                val gameOverIntent = Intent(context, GameOver::class.java)
                gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                gameOverIntent.putExtra("score", score.toString())
                context.startActivity(gameOverIntent)
            }
        }

        if (vegX < 0) {
            vegX = canvasWidth + 21
            vegY = (Math.floor(Math.random() * (maxGarfieldY - minGarfieldY)) + minGarfieldY).toInt()
        }

        canvas.drawBitmap(vegBitmap, vegX.toFloat(), vegY.toFloat(), null)

        canvas.drawText("Score : $score", 30f, 100f, scorePaint)

        repeat(3) { i ->
            val x = (1580 + life[0]!!.width * 1.5 * i).toInt()
            val y = 30

            if (i < lifeCounterOfGarfield) {
                canvas.drawBitmap(life[0]!!, x.toFloat(), y.toFloat(), null)
            } else {
                canvas.drawBitmap(life[1]!!, x.toFloat(), y.toFloat(), null)
            }
        }
    }

    private fun hitLasagnaChecker(x: Int, y: Int): Boolean {
        return garfieldX < x && x < garfieldX + garfield[0]!!.width && garfieldY < y && y < garfieldY + garfield[0]!!.height
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touch = true
                garfieldSpeed = -22
                garfieldY = event.y.toInt() - garfield[0]!!.height / 2
            }
            MotionEvent.ACTION_MOVE -> {
                garfieldY = event.y.toInt() - garfield[0]!!.height / 2
            }
        }
        // Ensure garfield stays within the canvas bounds
        garfieldY = garfieldY.coerceIn(0, canvasHeight - garfield[0]!!.height)
        return true
    }


}
