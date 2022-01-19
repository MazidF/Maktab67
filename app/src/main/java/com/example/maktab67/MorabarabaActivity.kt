package com.example.maktab67

import android.annotation.SuppressLint
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
import androidx.core.widget.ImageViewCompat


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

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun init() {
        getSize()
        board = Array(3) { Array(9) { 0 } }
        with(binding) {
            this@MorabarabaActivity.views = arrayOf(list1.init(), list2.init(), list3.init())
            root.addView(View.inflate(this@MorabarabaActivity, R.layout.mylayout, null))
        }
    }

    private fun FrameLayout.init(): Array<ImageView> {
        val view = View.inflate(this@MorabarabaActivity, R.layout.mylayout, null)
        this.addView(view)
        return view.allViews.filterIsInstance<ImageView>().toList().toTypedArray()
    }

    private fun getSize() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        width = displayMetrics.widthPixels
    }
}
