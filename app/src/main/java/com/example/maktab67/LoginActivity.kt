package com.example.maktab67

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LoginActivity : AppCompatActivity() {
//    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
/*        binding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        init()*/
        setContentView(R.layout.test2_layout)
    }



/*    @SuppressLint("RtlHardcoded")
    private fun init() {
        with(binding) {
            loginBtn.setOnClickListener {
                var isAllRight = true
                val name = nameInput.text.trim()
                val family = familyInput.text.trim()
                if (family.isEmpty()) {
                    familyInput.error = "Invalid Input!!!"
                    familyInput.requestFocus()
                    isAllRight = false
                }
                if (name.isEmpty()) {
                    nameInput.error = "Invalid Input!!!"
                    nameInput.requestFocus()
                    isAllRight = false
                }
                loginBtn.isEnabled = false
                if (isAllRight) {
                    Toast.makeText(this@LoginActivity, "Welcome, $name!!!", Toast.LENGTH_SHORT).apply {
                        setGravity(Gravity.TOP, 0, 0)
                    }.show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java).apply {
                        putExtra("name", name)
                        putExtra("family", family)
                    })
                    finish()
                }
            }
        }
    }*/
}
