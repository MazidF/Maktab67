package com.example.maktab67

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.maktab67.databinding.ActivityDoozBinding

class DoozActivity : AppCompatActivity() {
    companion object {
        const val RED = "#FF0000"
        const val BLUE = "#0039FF"
        const val RED_TURN = "Red Turn"
        const val BLUE_TURN = "Blue Turn"
        const val DRAW = "Draw"
        val bestPlaces = arrayOf(4, 0, 2, 6, 8, 1, 3, 5, 7)
    }

    var hasBot = false
    lateinit var botTarget: HashSet<Int>
    lateinit var botThreat: HashSet<Int>
    var botValue = 2
        get() {
        if (field == 2) throw Exception("bot value has a problem!!")
        return field
    }
    var remainPlaces = 9
    var redPoints = 0
    var bluePoints = 0
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

        hasBot = intent.getBooleanExtra("hasBot", false)
        load()
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (!hasBot) {
            menu.add("Bot").setOnMenuItemClickListener {
                val intent = Intent(this, DoozActivity::class.java).apply {
                    putExtra("hasBot", true)
                }
                startActivity(intent)
                false
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun log(msg: String, tag: String = "myLog") {
        Log.e(tag, msg)
    }

    override fun onStop() {
        super.onStop()
        save()
    }

    private fun save() {
        if (hasBot) return
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
        if (hasBot) return
        val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
        with(sharedPreferences) {
            redPoints = getInt("redPoints", 0)
            bluePoints = getInt("bluePoints", 0)
            remainPlaces = getInt("remainPlaces", 9)
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
        if (hasBot) {
            botTarget = HashSet(9)
            botThreat = HashSet(9)
            val dialog = AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("Which one starts the game?")
                .setNeutralButton("Return") { _, _ ->
                    finish()
                }
                .setPositiveButton("Me") { _, _ ->
                    botValue = -1
                }.setNegativeButton("Bot") {_, _ ->
                    botValue = 1
                    decision()
                }.create()
            dialog.show()
        }
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
        onImageClick(view, false)
    }

    private fun onImageClick(view: View, fromBot: Boolean) {
        val image = view as ImageView
        if (!isPlaying || view.drawable != null) return
        if (hasBot && ((currentIsBlue && botValue == 1)  || (!currentIsBlue && botValue == -1))) {
            if (!fromBot) return
        }

        remainPlaces--
        turnView.alpha = 0f
        view.translationY = -2000f

        val index = view.tag as Int
        runOnUiThread{
            image.setImageResource(getImage(index))
            currentIsBlue = !currentIsBlue
            setTurn()
        }

        view.animate().translationY(0f).duration = 400
        turnView.animate().alpha(1f).duration = 800


        if (hasBot) {
            botCheck(index, fromBot)
            if (fromBot) {
                check()
            } else {
                if (!check()) {
                    Thread {
                        Thread.sleep(2000)
                        decision()
                    }.start()
                    log("continue after")
                }
            }
        } else {
            check()
        }

/*        if (fromBot) {
            check()
        } else {
            if (!check()) {
                Thread {
                    Thread.sleep(2000)
                    decision()
                }.start()
                log("continue after")
            }
        }*/
    }

    private fun draw() {
        turnView.apply {
            text = DRAW
            setTextColor(Color.BLACK)
        }
        binding.doozRestartBtn.visibility = View.VISIBLE
        isPlaying = false
    }

    private fun check(addPoint: Boolean = true): Boolean {
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
            runOnUiThread {
                win(addPoint)
            }
            return true
        } else if (remainPlaces == 0) {
            runOnUiThread {
                draw()
            }
            return true
        }
        return false
    }

    // this method invoked if game is not over !!!
    private fun botCheck(lastMove: Int, fromBot: Boolean) {
        if (!hasBot) return
        val value = if (fromBot) botValue else -botValue
        icons[lastMove] = value
        botTarget.remove(lastMove)
        botThreat.remove(lastMove)
        fun checker(a: Int, c: Int) {
            if (icons[a] == value && icons[c] == 0) {
                if (fromBot) {
                    botTarget.add(c)
                } else {
                    botThreat.add(c)
                }
                return
            }
            if (icons[c] == value && icons[a] == 0) {
                if (fromBot) {
                    botTarget.add(a)
                } else {
                    botThreat.add(a)
                }
            }
        }

        val reminder = (lastMove / 3) * 3
        checker((lastMove + 1) % 3 + reminder, (lastMove + 2) % 3 + reminder)
        checker((lastMove + 3) % 9, (lastMove + 6) % 9)
        when {
            lastMove % 8 == 0 -> {
                checker(4, if (0 == lastMove) 8 else 0)
            }
            lastMove % 4 == 2 -> {
                checker(4, if (2 == lastMove) 6 else 2)
            }
            lastMove == 4 -> {
                checker(0, 8)
                checker(2, 6)
            }
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
        if (hasBot) { // when bot should start the game
            botThreat.clear()
            botTarget.clear()
            if (isBlueTurn) {
                if (botValue == 1) decision()
            } else {
                if (botValue == -1) decision()
            }
        }
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

    private fun decision() {
        if (!hasBot) return
        var index: Int = -1
        if (botTarget.isNotEmpty()) {
            index = botTarget.stream().toArray()[0] as Int
            botTarget.remove(index)
        } else if (botThreat.isNotEmpty()) {
            index = botThreat.stream().toArray()[0] as Int
            botThreat.remove(index)
        } else {
            for (i in bestPlaces) {
                if (icons[i] == 0) {
                    index = i
                    break
                }
            }
        }
        if (index == -1) {
            throw Exception("either remaining palaces is 0 or something else!!")
        }
        val view = views[index]
        onImageClick(view, true)
    }
}
