package com.example.maktab67

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.maktab67.databinding.ActivityMorabarabaBinding
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import androidx.core.view.allViews
import com.example.maktab67.databinding.ActivityMorabaraba2Binding
import com.example.maktab67.databinding.MyLayoutBinding


class MorabarabaActivity : AppCompatActivity() {
    lateinit var binding: ActivityMorabaraba2Binding
    var height = -1
    var width = -1
    lateinit var board: Array<Array<Int>> // blue -> 1, red -> -1, empty -> 0
    lateinit var views: Array<Array<ImageView>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMorabaraba2Binding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        init()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun init() {
        getSize()
        with(binding) {
            views = arrayOf(included1.load(), included2.load(), included3.load())
        }
    }

    fun MyLayoutBinding.load() = this.root.allViews
        .filterIsInstance<ImageView>().toList().toTypedArray()

    private fun getSize() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        width = displayMetrics.widthPixels
    }

    fun imageClick(view: View?) {

    }
}
