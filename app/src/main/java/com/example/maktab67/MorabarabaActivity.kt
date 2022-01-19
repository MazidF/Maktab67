package com.example.maktab67

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.maktab67.databinding.ActivityMorabarabaBinding
import android.util.DisplayMetrics




class MorabarabaActivity : AppCompatActivity() {
    lateinit var binding: ActivityMorabarabaBinding
    var height = -1
    var width = -1
    lateinit var board: Array<Array<Int>> // blue -> 1, red -> -1, empty -> 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMorabarabaBinding.inflate(LayoutInflater.from(this))
        setContentView(R.layout.activity_morabaraba)
        init()


    }

    private fun init() {
        getSize()
        board = Array(3) { Array(9) { 0 } }
    }

    private fun getSize() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        width = displayMetrics.widthPixels
    }
}
