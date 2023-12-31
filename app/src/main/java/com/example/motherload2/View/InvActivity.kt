package com.example.motherload2.View

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.motherload2.ConnectView
import com.example.motherload2.R
import com.example.motherload2.View.Frag.SacFragment
import com.example.motherload2.View.Frag.fragmentUpgrade

class InvActivity: AppCompatActivity() {
    /*
    activiter qui contient l'inventaire du personnage
     */
    private lateinit var connectView: ConnectView
    private var sacFragmaent : SacFragment? = null



    // Pour faire une mise à jour toutes les 15 secondes : on va donner un job toutes les
    // 15 secondes au thread principal. On récupère son handler ici...
    private val handler = Handler(Looper.getMainLooper())
    // ... Et voici le runnable que l'on va lui donner. Il déclenche la mise à jour et se programme
    // lui-même à nouveau pour être exécuté dans 15 secondes.
    private val updateRunnable = object : Runnable {
        override fun run() {
            connectView.statusplayer()
            handler.postDelayed(this, 15000) // Appelle toutes les 15 secondes

            val name : TextView = findViewById(R.id.name)
            name.text = connectView.perso.name
            val money : TextView = findViewById(R.id.money)
            money.text = connectView.perso.money
            val pick : TextView = findViewById(R.id.pick)
            pick.text = connectView.perso.pickaxe
        }
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.invactivity)

        connectView = ViewModelProvider(this).get(ConnectView::class.java)
        sacFragmaent = supportFragmentManager.findFragmentById(R.id.mainNoteFrag) as SacFragment?




       val connectButton : Button = findViewById(R.id.sell)
        connectButton.setOnClickListener {
            connectView.sell()
        }
        val listButton : Button = findViewById(R.id.list)
        listButton.setOnClickListener {
            connectView.upgradelist()
            val frag = fragmentUpgrade()
            val fragment = supportFragmentManager.findFragmentById(R.id.pickup)
            val fragmentT = supportFragmentManager.beginTransaction()
            if (fragment != null){
                fragmentT.remove(fragment)
                fragmentT.commit()
            }else {
                fragmentT.add(R.id.pickup, frag!!)
                fragmentT.commit()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        // L'activity repasse en premier plan : on relance la mise à jour des messages
        handler.post(updateRunnable)
    }

    override fun onPause() {
        // L'activity passe en arrière-plan : on coupe la mise à jour des messages :
        // Pour ce faire, on enlève de la file d'attente le job qui était posté.
        handler.removeCallbacks(updateRunnable)
        super.onPause()
    }
}