package com.example.maktab67

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Color.*
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.InputType.*
import android.text.method.PasswordTransformationMethod
import android.text.method.TransformationMethod
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.size
import androidx.core.widget.doOnTextChanged
import com.example.maktab67.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    var hasSaved = false
    set(value) {
        if (value) {
            binding.autoCompleteBtn.visibility = View.VISIBLE
        } else {
            binding.autoCompleteBtn.visibility = View.GONE
        }
        field = value
    }
    lateinit var binding: ActivityLoginBinding
    var focused: View? = null
    lateinit var emailRegex: Regex
    lateinit var nameRegex: Regex
    lateinit var views: ArrayList<EditText>

    companion object {
        const val MARK = "mark"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        hasSaved = getSharedPreferences(packageName, MODE_PRIVATE).getBoolean("hasSaved", false)
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add("Clear").apply {
            this.setShowAsAction(1)
            setOnMenuItemClickListener {
                clear()
                removeFocus()
                false
            }
        }
        menu.add("Reset").apply {
            setOnMenuItemClickListener {
                reset()
                false
            }
        }
        menu.add("Edit").apply {
           setOnMenuItemClickListener {
               startActivityForResult(Intent(this@LoginActivity, EditActivity::class.java).apply {
                   putExtra(EditActivity.FULL_NAME, views[0].text.toString())
                   putExtra(EditActivity.USERNAME, views[1].text.toString())
                   putExtra(EditActivity.EMAIL, views[2].text.toString())
                   putExtra(EditActivity.PASSWORD, views[3].text.toString())
               }, 1234)
               false
           }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234) {
            clear()
            data!!.extras!!.let {
                views[0].setText(it.getString(EditActivity.FULL_NAME, "empty"))
                views[1].setText(it.getString(EditActivity.USERNAME, "empty"))
                views[2].setText(it.getString(EditActivity.EMAIL, "empty"))
                views[3].setText(it.getString(EditActivity.PASSWORD, "empty"))
            }
        }
    }

    private fun clear() {
        for (view in views) {
            view.setText("") // view.text.clear()
            view.requestFocus()
        }
        with(binding) {
            genderGroup.check(-1)
            val size = loginList.size
            for (i in 11 until size) {
                loginList.removeView(loginList[11])
            }
        }
    }

    private fun reset() {
        val dialog = AlertDialog.Builder(this@LoginActivity)
            .setTitle("RESET!!")
            .setMessage("Are you sure?")
            .setPositiveButton("Yes") { _, _ ->
                getSharedPreferences(packageName, MODE_PRIVATE).edit().clear().apply()
                hasSaved = false
            }.setNeutralButton("Cancel") { _, _ -> }
            .create()
        dialog.show()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun init() {
        views = ArrayList(5)
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
                    this.tag = object : TransformationMethod {
                        override fun getTransformation(p0: CharSequence, p1: View?) = p0
                        override fun onFocusChanged(p0: View?, p1: CharSequence?, p2: Boolean, p3: Int, p4: Rect?) {}
                    }
                    doOnTextChanged { text, _, _, _ ->
                        if (text!!.count() < 6) {
                            this.setTextColor(RED)
                        } else {
                            this.setTextColor(views[4].tag as? ColorStateList)
                        }
                        rePasswordView?.text = rePasswordView!!.text
                    }
                }, i++)
                addView(addEditText("Re-type Password", TYPE_TEXT_VARIATION_PASSWORD) {
                    rePasswordView = this
                    transformationMethod = PasswordTransformationMethod.getInstance()
                    this.tag = this.textColors
                    doOnTextChanged { text, _, _, _ ->
                        if (text.toString() == passwordView!!.text.toString() && text!!.count() >= 6) {
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
            registerBtn.apply {
                changeColor(this, Color.parseColor("#F33597"))
                setOnClickListener {
                    if (check()) {
                        save()
                    }
                }
            }
            autoCompleteBtn.apply {
                changeColor(this, Color.parseColor("#3158CD"))
                setOnClickListener {
                    load()
                }
            }
            checkBox.apply {
                val view = views[3]
                setOnCheckedChangeListener { _, _ ->
                    val temp = view.tag as TransformationMethod
                    view.tag = view.transformationMethod
                    view.transformationMethod = temp
                }
            }
            scrollOption.apply {
                val resource = this@LoginActivity.resources
                val background = resource.getDrawable(R.drawable.my_button)
                fun addView(text: String, background: Drawable): TextView {
                    return TextView(this@LoginActivity).apply {
                        this.textSize = 24f
                        this.text = text
                        this.background = background
                        changeColor(this, GREEN)
                    }
                }
                setOnClickListener {
                    repeat(10) {
                        loginList.addView(addView("\nTextView$it\n", background))
                    }
                }
            }
        }
    }

    private fun toast(msg: String, time: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, msg, time).show()
    }

    private fun check(): Boolean {
        var result = true
        with(binding) {
            genderTitle.error = if (genderGroup.checkedRadioButtonId == -1) {
                result = false
                "!!!"
            } else {
                null
            }
        }
        for (view in views.reversed()) {
            if (view.text.isEmpty() || view.textColors.defaultColor == RED) {
                view.error = "Invalid Input!!!"
                result = false
            }
        }
        return result
    }

    private fun save() {
        val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
        sharedPreferences.edit().apply {
            for (i in views.indices) {
                views[i].error = null
                putString("$i", views[i].text.toString())
            }
            hasSaved = true
            putBoolean("hasSaved", hasSaved)
            putInt("gender", binding.genderGroup.checkedRadioButtonId)
        }.apply()
    }

    private fun load() {
        if (!hasSaved) return
        getSharedPreferences(packageName, MODE_PRIVATE).run {
            for (i in views.indices) {
                views[i].setText(this.getString("$i", null))
            }
            binding.genderGroup.check(getInt("gender", -1))
        }
    }

    private fun changeColor(view: View, color: Int) {
        view.background.setTint(color)
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
        val root = View.inflate(this, R.layout.my_edit_text, null)
        val textView = root.findViewById<TextView>(R.id.editTextTitle).apply {
            text = title
        }
        val editText = root.findViewById<EditText>(R.id.editTextText).apply {
            hint = title
            setInputType(inputType)
            setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    focused = view
                    if (textView.visibility != View.VISIBLE) {
                        textView.visibility = View.VISIBLE
                        background.setColorFilter(parseColor("#0048FF"), PorterDuff.Mode.SRC_ATOP)
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
        views.add(editText)
        return root
    }
}
