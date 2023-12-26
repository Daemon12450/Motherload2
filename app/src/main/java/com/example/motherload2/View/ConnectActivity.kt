package com.example.motherload2.View

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.motherload2.Character.Item
import com.example.motherload2.ConnectView
import com.example.motherload2.R

class ConnectActivity : AppCompatActivity() {
    /*
    Activite de teste pour verifier que toute les
    fonction dappel au srveur fonctionne correctement mais nest plus utiliser
    */

    private lateinit var connectView: ConnectView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.connectactivity)

        connectView = ViewModelProvider(this).get(ConnectView::class.java)
        //editTextLog = findViewById(R.id.edit_log)
        //editTextPass = findViewById(R.id.edit_pass)

        val buttonConnect : Button = findViewById(R.id.connect)
        buttonConnect.setOnClickListener {
            // on récupère le login et le mot de passe
            val log = "alewis"//editTextLog.text.toString()
            val pass = "LAt%24yc6@" //editTextPass.text.toString()
            connectView.connectWeb(log,pass)
            //Log.d("connected ?",connectView.getconnect().toString())
            if (connectView.getconnect()) {
                // button ferme avant la fin de la verif donc faut appuyer 2 fois pour l'instant, faut faire un delay ou att que la fonction fini
                val intent = Intent(this, GameActivity::class.java)

                startActivity(intent)
                // Une fois connecter, on peut quitter l'activity. Cela reviendra automatiquement
                // à l'activity précédente, c'est à dire MainActivity.
                finish()
            }
            //finish()
        }

        val buttonname : Button = findViewById(R.id.changename)
        buttonname.setOnClickListener {
            // on récupère le nom que l'on veut changer
            val name = "alewis"//editTextName.text.toString()
            connectView.changename(name)
            // Une fois le nom changé, on peut quitter l'activity. Cela reviendra automatiquement
            // à l'activity précédente, c'est à dire MainActivity.
            finish()
        }

        val buttoncoords : Button = findViewById(R.id.coords)
        buttoncoords.setOnClickListener {
            connectView.deplacement()

        }
        val buttonStatus : Button = findViewById(R.id.status)
        buttonStatus.setOnClickListener {
            connectView.statusplayer()
        }
        val buttonreinit : Button = findViewById(R.id.reinit)
        buttonreinit.setOnClickListener {
            connectView.reinitplayer()
        }

        val buttondig : Button = findViewById(R.id.dig)
        buttondig.setOnClickListener {
            connectView.dig()
        }

        val buttonitem : Button = findViewById(R.id.item)
        buttonitem.setOnClickListener{
            val id = "5"// get texte id
            val item = Item(id)
            connectView.detailitem(id,item)
        }

        val buttonmarket : Button = findViewById(R.id.market)
        buttonmarket.setOnClickListener {
            connectView.market()
        }
    }
}