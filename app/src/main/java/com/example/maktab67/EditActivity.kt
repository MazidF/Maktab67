package com.example.maktab67

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.EditText
import com.example.maktab67.databinding.ActivityEditBinding
import com.google.android.material.textfield.TextInputEditText

class EditActivity : AppCompatActivity() {
    lateinit var views: Array<EditText>
    lateinit var binding: ActivityEditBinding

    companion object {
        const val FULL_NAME = "fullName"
        const val USERNAME = "username"
        const val EMAIL = "email"
        const val PASSWORD = "password"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        init()
    }

    override fun onBackPressed() {
        save()
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            save()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        intent!!.extras!!.let {
            with(binding) {
                fullNameEdit.setText(it.getString(FULL_NAME, "empty"))
                usernameEdit.setText(it.getString(USERNAME, "empty"))
                emailEdit.setText(it.getString(EMAIL, "empty"))
                passwordEdit.setText(it.getString(PASSWORD, "empty"))
            }
        }
    }

    fun save() {
        val intent = Intent().apply {
            with(binding) {
                putExtra(FULL_NAME, fullNameEdit.text.toString())
                putExtra(USERNAME, usernameEdit.text.toString())
                putExtra(EMAIL, emailEdit.text.toString())
                putExtra(PASSWORD, passwordEdit.text.toString())
            }
        }
        setResult(0, intent)
    }
}