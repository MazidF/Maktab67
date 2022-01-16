package com.example.maktab67

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Color.RED
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.InputType.*
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.maktab67.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    var focused: View? = null
    lateinit var emailRegex: Regex
    lateinit var nameRegex: Regex


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        init()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun init() {
        emailRegex = Regex("^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
        nameRegex = Regex("^\\s*([a-zA-Z]+\\s)+[a-zA-Z]+\\s*$")
        with(binding) {
            loginList.apply {
                var i = 0
                var passwordView: EditText? = null
                var rePasswordView: EditText? = null
                addView(addEditText("FullName", TYPE_TEXT_VARIATION_PERSON_NAME) {
                    checkInput(nameRegex)
                }, i++)
                addView(addEditText("UserName", TYPE_TEXT_VARIATION_NORMAL), i++)
                addView(addEditText("Email", TYPE_TEXT_VARIATION_EMAIL_ADDRESS) {
                    checkInput(emailRegex)
                }, i++)
                addView(addEditText("Password", TYPE_TEXT_VARIATION_PASSWORD) {
                    passwordView = this
                    transformationMethod = PasswordTransformationMethod.getInstance()
                    this.tag = this.textColors
                    doOnTextChanged { text, _, _, _ ->
                        if (text!!.count() < 6) {
                            this.setTextColor(RED)
                        } else {
                            this.setTextColor(tag as? ColorStateList)
                        }
                        rePasswordView?.text = rePasswordView!!.text
                    }
                }, i++)
                addView(addEditText("Re-type Password", TYPE_TEXT_VARIATION_PASSWORD) {
                    rePasswordView = this
                    transformationMethod = PasswordTransformationMethod.getInstance()
                    this.tag = this.textColors
                    doOnTextChanged { text, _, _, _ ->
                        Log.e("myLog", text!!.toString())
                        if (text.toString() == passwordView!!.text.toString() && text.count() >= 6) {
                            this.setTextColor(tag as? ColorStateList)
                        } else {
                            this.setTextColor(RED)
                        }
                    }
                }, i)
                setOnClickListener {
                    focused?.clearFocus()
                }
            }
            loginScroller.setOnClickListener {
                removeFocus()
            }
        }
    }
    private fun EditText.checkInput(regex: Regex) {
        this.tag = this.textColors
        doOnTextChanged { text, _, _, _ ->
            if (!regex.containsMatchIn(text!!)) {
                this.setTextColor(RED)
            } else {
                this.setTextColor(tag as? ColorStateList)
            }
        }
    }

    private fun removeFocus() {
        focused?.run {
            clearFocus()
            focused = null
        }
    }

    private fun addEditText(title: String, inputType: Int, apply: (EditText.() -> Unit)? = null): View {
        val view = View.inflate(this, R.layout.my_edit_text, null)
        val textView = view.findViewById<TextView>(R.id.editTextTitle).apply {
            text = title
        }
        view.findViewById<EditText>(R.id.editTextText).apply {
            hint = title
            setInputType(inputType)
            setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    focused = view
                    if (textView.visibility != View.VISIBLE) {
                        textView.visibility = View.VISIBLE
                        background.setColorFilter(Color.parseColor("#0048FF"), PorterDuff.Mode.SRC_ATOP)
                        hint = ""
                    }
                } else {
                    if (text.toString().isEmpty()) {
                        background.clearColorFilter()
                        hint = title
                    }
                    textView.visibility = View.GONE
                }
            }
            if (apply != null) {
                apply(this)
            }
        }
        return view
    }
}
