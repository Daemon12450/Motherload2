package com.example.motherload2.View

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SharedMemory
import android.text.SpannableStringBuilder
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.motherload2.Connect.LanguageManager
import com.example.motherload2.ConnectView
import com.example.motherload2.R

class MainActivity : AppCompatActivity() {
    private lateinit var connectView: ConnectView
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        connectView = ViewModelProvider(this).get(ConnectView::class.java)

        setContentView(R.layout.activity_main)
        val log : EditText = findViewById(R.id.log)
        val pass : EditText = findViewById(R.id.pass)
        val pref :SharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE)
        val chec : String? = pref.getString("remember","")
        if (chec == "true"){
            val login : String? = pref.getString("log","")
            val password : String? = pref.getString("pass","")
            log.text = SpannableStringBuilder(login)
            pass.text = SpannableStringBuilder(password)
        }

        val checkBox : CheckBox = findViewById(R.id.checkBox)


        val connectButton : Button = findViewById(R.id.Connect)
        connectButton.setOnClickListener {
            connectView.connectWeb(log.text.toString(),pass.text.toString())
            if (connectView.getconnect()) {
                if (!checkBox.isChecked){// verifi si lutilisateur veut oublier ses log
                    /*on ecrit dans un fichier les log + un chec quon vas verifier a louerture de lapp*/
                    val pref = getSharedPreferences("checkbox", MODE_PRIVATE)
                    val edit = pref.edit()
                    edit.putString("remember","true")
                    edit.putString("log",log.text.toString())
                    edit.putString("pass",pass.text.toString())
                    edit.apply()
                }else{
                    val pref = getSharedPreferences("checkbox", MODE_PRIVATE)
                    val edit = pref.edit()
                    edit.putString("remember","false")
                    edit.apply()
                }
                // faut appuyer 2 fois pour l'instant, faut faire un delay ou att que la fonction fini
                val intent = Intent(this, GameActivity::class.java)
                startActivity(intent)

            }
        }

        /*deux bouton qui permet de changer la langue de lapp*/
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