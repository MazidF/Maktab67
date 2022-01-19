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
import android.view.MotionEvent
import androidx.core.view.MotionEventCompat


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.root.setOnClickListener {
            toast("touched")
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add("Dice").apply {
            setOnMenuItemClickListener {
                startActivity(Intent(this@MainActivity, DiceActivity::class.java))
                false
            }
        }
        menu.add("Dooz").apply {
            setOnMenuItemClickListener {
                startActivity(Intent(this@MainActivity, DoozActivity::class.java))
                false
            }
        }
        menu.add("Login").apply {
            setOnMenuItemClickListener {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                false
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

}
