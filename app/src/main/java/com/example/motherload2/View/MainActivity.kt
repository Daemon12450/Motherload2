package com.example.motherload2.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.motherload2.Connect.LanguageManager
import com.example.motherload2.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val connectButton : Button = findViewById(R.id.Connect)
        connectButton.setOnClickListener {
            val intent = Intent(this, ConnectActivity::class.java)

            startActivity(intent)
        }
        val lang = LanguageManager(this)
        val fr : ImageButton = findViewById(R.id.Fr)
        fr.setOnClickListener{
            lang.updateResouces("fr")
            recreate()
        }
        val en : ImageButton = findViewById(R.id.En)
        en.setOnClickListener{
            lang.updateResouces("en")
            recreate()
        }
    }
}