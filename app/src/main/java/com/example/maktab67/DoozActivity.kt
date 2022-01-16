package com.example.maktab67

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.maktab67.databinding.ActivityDoozBinding

class DoozActivity : AppCompatActivity() {
    companion object {
        const val RED = "#FF0000"
        const val BLUE = "#0039FF"
        const val RED_TURN = "Red Turn"
        const val BLUE_TURN = "Blue Turn"
        const val DRAW = "Draw"
    }

    var remainPlaces = 9
    var redPoints = -1
    var bluePoints = -1
    var isPlaying = true
    var isBlueTurn = true
    var currentIsBlue = true
    var icons = arrayOf(0, 0, 0,
                        0, 0, 0,
                        0, 0, 0)  // 1-> blue, -1 -> red, 0 -> null
    lateinit var views: Array<ImageView>
    private var turning = HashSet<ImageView>(5)
    lateinit var turningThread: Thread
    lateinit var turnView: TextView
    lateinit var binding: ActivityDoozBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoozBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        load()
        init()
    }

    private fun log(msg: String, tag: String = "tag") {
        Log.e(tag, msg)
    }

    override fun onStop() {
        super.onStop()
        save()
    }

    private fun save() {
        log("save method")
        val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
        sharedPreferences.edit()
            .putBoolean("isBlueTurn", isBlueTurn)
            .putBoolean("currentIsBlue", currentIsBlue)
            .putInt("redPoints", redPoints)
            .putInt("bluePoints", bluePoints)
            .putString("icons", icons.joinToString(", "))
            .putInt("remainPlaces", remainPlaces)
            .putBoolean("isPlaying", isPlaying)
            .apply()
    }

    @SuppressLint("WrongConstant")
    private fun load() {
        val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
        with(sharedPreferences) {
            redPoints = getInt("redPoints", 0)
            bluePoints = getInt("bluePoints", 0)
            remainPlaces = getInt("remainPlaces", 0)
            isBlueTurn = getBoolean("isBlueTurn", true)
            currentIsBlue = getBoolean("currentIsBlue", true)
            val string = getString("icons", null)
            string?.let {
                icons = it.split(", ").map { s -> s.toInt() }.toTypedArray()
            }
            isPlaying = getBoolean("isPlaying", true)
        }
    }

    private fun init() {
        turnView = binding.turnViewer
        setTurn()
        with(binding) {
            views = arrayOf(dooz1, dooz2, dooz3, dooz4, dooz5, dooz6, dooz7, dooz8, dooz9)
            for (i in views.indices) {
                views[i].apply {
                    tag = i
                    if (icons[i] == 1) {
                        setImageResource(R.drawable.blue_o_icon)
                    } else if (icons[i] == -1) {
                        setImageResource(R.drawable.red_x_icon)
                    }
                }
            }
            if (!isPlaying || remainPlaces == 0) {
                check(false)
            }
            redCounter.text = redPoints.toString()
            blueCounter.text = bluePoints.toString()
        }
    }

    private fun setTurn() {
        turnView.apply {
            text = if (currentIsBlue) {
                setTextColor(Color.parseColor(BLUE))
                BLUE_TURN
            } else {
                setTextColor(Color.parseColor(RED))
                RED_TURN
            }
        }
    }

    fun onImageClick(view: View) {
        val image = view as ImageView
        if (!isPlaying || view.drawable != null) return

        remainPlaces--
        turnView.alpha = 0f
        view.translationY = -2000f

        image.setImageResource(getImage(view.tag as Int))
        currentIsBlue = !currentIsBlue
        setTurn()

        view.animate().translationY(0f).duration = 400
        turnView.animate().alpha(1f).duration = 800

        check()
    }

    private fun draw() {
        turnView.apply {
            text = DRAW
            setTextColor(Color.BLACK)
        }
        binding.doozRestartBtn.visibility = View.VISIBLE
        isPlaying = false
    }

    private fun check(addPoint: Boolean = true) {
        var value = 0

        fun equals(a: Int, b: Int, c: Int) {
            if (icons[a] != 0 && icons[a] == icons[b] && icons[b] == icons[c]) {
                turning.add(views[a])
                turning.add(views[b])
                turning.add(views[c])
                value = icons[b]
            }
        }

        equals(0, 1, 2)
        equals(3, 4, 5)
        equals(6, 7, 8)
        equals(0, 3, 6)
        equals(1, 4, 7)
        equals(2, 5, 8)
        equals(0, 4, 8)
        equals(2, 4, 6)
        if (value != 0) {
            win(addPoint)
        } else if (remainPlaces == 0) {
            draw()
        }
    }

    private fun win(addPoint: Boolean = true) {
        if (addPoint) {
            if (!currentIsBlue) {
                binding.blueCounter.text = (++bluePoints).toString()
            } else {
                binding.redCounter.text = (++redPoints).toString()
            }
        }
        binding.doozRestartBtn.visibility = View.VISIBLE
        isPlaying = false
        turningThread = Thread {
            Thread.sleep(400)
            while (!isPlaying) {
                for (v in turning) {
                    v.animate().rotationYBy(360f).duration = 1000
                }
                Thread.sleep(2000)
            }
        }
        turningThread.start()
    }

    fun restart(view: View?) {
        if (isPlaying) return
        isPlaying = true
        icons = Array(9) {0}
        for (v in views) {
            v.setImageDrawable(null)
        }
        turning.clear()
        remainPlaces = 9
        isBlueTurn = !isBlueTurn
        currentIsBlue = isBlueTurn
        setTurn()
        binding.doozRestartBtn.visibility = View.INVISIBLE
    }

    private fun getImage(tag: Int): Int {
       return if (currentIsBlue) {
           icons[tag] = 1
           R.drawable.blue_o_icon
        } else {
           icons[tag] = -1
           R.drawable.red_x_icon
       }
    }

    fun reset() {

    }
}
