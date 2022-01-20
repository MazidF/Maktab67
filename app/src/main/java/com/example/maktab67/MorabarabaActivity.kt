package com.example.maktab67

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import androidx.core.view.allViews
import com.example.maktab67.databinding.ActivityMorabaraba2Binding
import com.example.maktab67.databinding.MyLayoutBinding

typealias Index = Pair<Int, Int>

class MorabarabaActivity : AppCompatActivity() {

    companion object {
        const val BLUE = "#1C1CD5"
        const val RED = "#E41010"
    }

    lateinit var binding: ActivityMorabaraba2Binding
    var height = -1
    var width = -1
    var isBlue = true
    var currentIsBlue = true
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
            for (i in views.indices) {
                for (j in views[i].indices) {
                    views[i][j].apply {
                        tag = j
                        setOnClickListener {
                            onClick(this)
                        }
                    }
                }
            }
        }
    }

    private fun getSize() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        width = displayMetrics.widthPixels
    }

    private fun MyLayoutBinding.load() = this.root.allViews
        .filterIsInstance<ImageView>().toList().toTypedArray()

    private fun View.index(): Index {
        val index = tag as Int
        for (i in 0..2) {
            if (views[i][index] == this) return Index(i, index)
        }
        return Index(-1, -1)
    }

    private fun Index.board() = board[first][second]

    private fun onClick(view: ImageView, isPut: Boolean = true) {
        if (currentIsBlue) {
            setImage(view)
        }

        currentIsBlue = !currentIsBlue
    }

    fun put(view: ImageView) {
        val (x, y) = view.index()

        setImage(view)

    }

    private fun setImage(view: ImageView) {
        view.setImageResource(
            if (currentIsBlue) {
                R.drawable.blue_circle
            } else {
                R.drawable.red_circle
            }
        )
    }

    private fun removeImage(view: ImageView) {
        view.setImageResource(R.drawable.circle_shape)
    }

    fun remove(view: ImageView) {
        removeImage(view)

    }

    class Player {

    }
}
