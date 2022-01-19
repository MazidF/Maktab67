package com.example.maktab67

import android.content.res.ColorStateList
import android.graphics.Color.RED
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.maktab67.databinding.ActivityMorabarabaBinding
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.view.allViews


class MorabarabaActivity : AppCompatActivity() {
    lateinit var binding: ActivityMorabarabaBinding
    var height = -1
    var width = -1
    lateinit var board: Array<Array<Int>> // blue -> 1, red -> -1, empty -> 0
    lateinit var views: Array<Array<ImageView>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMorabarabaBinding.inflate(LayoutInflater.from(this))
        setContentView(R.layout.activity_morabaraba)
        init()
    }

    private fun init() {
        getSize()
        board = Array(3) { Array(9) { 0 } }
        with(binding) {
            views = arrayOf(list1.load(), list2.load(), list3.load())
        }
    }

    private fun FrameLayout.load() = allViews
        .toList()
        .filterIsInstance<ImageView>()
        .map {
                it.apply {
                    background?.setTint(RED)
                }
        }.toTypedArray()

    private fun getSize() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        width = displayMetrics.widthPixels
    }
}
