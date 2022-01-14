package com.example.maktab67

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.widget.Toast
import com.example.maktab67.databinding.ActivityMainBinding
import android.util.DisplayMetrics


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var height = -1
    var width = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        width = displayMetrics.widthPixels
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add("dice").apply {
            setOnMenuItemClickListener {
                startActivity(Intent(this@MainActivity, DiceActivity::class.java))
                false
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

}
