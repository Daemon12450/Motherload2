package com.example.motherload2.View

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.motherload2.ConnectView
import com.example.motherload2.R
import com.example.motherload2.View.Frag.OffersFragment
import com.example.motherload2.View.Frag.OffredetailFrag

class ShopActivity : AppCompatActivity(){
    /*
    activitée qui contient les offres du marchant permet de les acheter et de voir les détails des objets
     */

    private lateinit var connectView: ConnectView
    private var offersFragmaent : OffersFragment? = null
    private var offersdetailFragment: OffredetailFrag? = null

    // Pour faire une mise à jour toutes les 15 secondes : on va donner un job toutes les
    // 15 secondes au thread principal. On récupère son handler ici...
    private val handler = Handler(Looper.getMainLooper())
    // ... Et voici le runnable que l'on va lui donner. Il déclenche la mise à jour et se programme
    // lui-même à nouveau pour être exécuté dnas 15 secondes.
    private val updateRunnable = object : Runnable {
        override fun run() {
            connectView.market()
            handler.postDelayed(this, 15000) // Appelle toutes les 15 secondes

            // on ajoute si il y a des nouveau mais on enlève pas si il y en a moins
        }
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shopactivity)

        connectView = ViewModelProvider(this).get(ConnectView::class.java)
        offersFragmaent = supportFragmentManager.findFragmentById(R.id.mainNoteFrag) as OffersFragment?
        offersdetailFragment = supportFragmentManager.findFragmentById(R.id.maindetailFrag)as OffredetailFrag?

        val connectButton : Button = findViewById(R.id.Buy)
        connectButton.setOnClickListener {
            connectView.buy()
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