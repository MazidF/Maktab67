package com.example.maktab67

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.example.maktab67.databinding.ActivityDiceBinding
import kotlin.random.Random

class DiceActivity : AppCompatActivity() {
    lateinit var binding: ActivityDiceBinding
    val random = Random(System.currentTimeMillis())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiceBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        init()
    }

    fun init() {
        with(binding) {
            diceBtn.setOnClickListener {
                val diceNumber = random.nextInt(1, 7)
                imageView.setImageResource(
                    when(diceNumber) {
                        1 -> R.drawable.dice_1
                        2 -> R.drawable.dice_2
                        3 -> R.drawable.dice_3
                        4 -> R.drawable.dice_4
                        5 -> R.drawable.dice_5
                        6 -> R.drawable.dice_6
                        else -> R.drawable.empty_dice
                    }
                )
                diceBtn.visibility = View.GONE
                resetBtn.visibility = View.VISIBLE
            }

            resetBtn.setOnClickListener {
                imageView.setImageResource(R.drawable.empty_dice)
                resetBtn.visibility = View.GONE
                diceBtn.visibility = View.VISIBLE
            }
        }
    }


}