package com.example.maktab67

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.maktab67.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var launcher: ActivityResultLauncher<Void?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        tackPicture()
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
        menu.add("Take Picture").apply {
            setOnMenuItemClickListener {
                launcher.launch(null)
                false
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun <I, O> startActivityForResult(i: I) {
        registerForActivityResult(object : ActivityResultContract<I, O>() {
            override fun createIntent(context: Context, input: I): Intent {
                TODO("Not yet implemented")
            }

            override fun parseResult(resultCode: Int, intent: Intent?): O {
                TODO("Not yet implemented")
            }

        }, object : ActivityResultCallback<O> {
            override fun onActivityResult(result: O) {
                TODO("Not yet implemented")
            }

        }).launch(i)
    }

    fun startActivityForResult2() {
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        }
    }

    fun <T> chooser(action: String, type: String, title: String, putExtra: Intent.() -> Unit): Intent {
        val intent = Intent().apply {
            this.action = action
            this.type = type
            putExtra()
        }
        return Intent.createChooser(intent, title)
    }

    fun tackPicture() {
        launcher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            binding.imageView11.setImageBitmap(it)
        }
    }

}
