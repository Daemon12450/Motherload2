package com.example.motherload2.View

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.motherload2.ConnectView
import com.example.motherload2.R
import com.example.motherload2.View.Frag.FragmentPlus
import com.example.motherload2.View.Frag.SacFragment
import com.example.motherload2.databinding.GameactivityBinding
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class GameActivity : AppCompatActivity(), MapListener {
    private lateinit var connectView: ConnectView
    private var fragmentplus : FragmentPlus? = null

    lateinit var mMap: MapView
    lateinit var controller: IMapController;
    lateinit var mMyLocationOverlay: MyLocationNewOverlay;

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            connectView.deplacement()
            handler.postDelayed(this, 15000) // Appelle toutes les 15 secondes
        }
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        setContentView(R.layout.gameactivity)
        connectView = ViewModelProvider(this).get(ConnectView::class.java)

        val binding = GameactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        )
        mMap = binding.mapView
        mMap.setTileSource(TileSourceFactory.MAPNIK)
        mMap.mapCenter
        mMap.setMultiTouchControls(true)
        mMap.getLocalVisibleRect(Rect())

        mMyLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), mMap)
        controller = mMap.controller

        mMyLocationOverlay.enableMyLocation()
        mMyLocationOverlay.enableFollowLocation()
        mMyLocationOverlay.isDrawAccuracyEnabled = true
        mMyLocationOverlay.runOnFirstFix {
            runOnUiThread {
                controller.setCenter(mMyLocationOverlay.myLocation);
                controller.animateTo(mMyLocationOverlay.myLocation)
            }
        }

        controller.setZoom(6.0)

        Log.e("TAG", "onCreate:in ${controller.zoomIn()}")
        Log.e("TAG", "onCreate: out  ${controller.zoomOut()}")

        // controller.animateTo(mapPoint)
        mMap.overlays.add(mMyLocationOverlay)

        mMap.addMapListener(this)

        Log.d("same ?",connectView.getconnect().toString())

        val buttonShop : Button = findViewById(R.id.shop)
        buttonShop.setOnClickListener {
            val intent = Intent(this, ShopActivity::class.java)
            startActivity(intent)
        }
        val buttonDig : Button = findViewById(R.id.dig)
        buttonDig.setOnClickListener {
            connectView.dig()
        }

        val buttonInv : Button = findViewById(R.id.inv)
        buttonInv.setOnClickListener {
            val intent = Intent(this, InvActivity::class.java)

            startActivity(intent)
        }
        fragmentplus = FragmentPlus()

        val buttonPlus : Button = findViewById(R.id.plus)
        buttonPlus.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.fragmentplus)
            val fragmentT = supportFragmentManager.beginTransaction()
            if (fragment != null){
                fragmentT.remove(fragment)
                fragmentT.commit()
            }else {
                fragmentT.add(R.id.fragmentplus, fragmentplus!!)
                fragmentT.commit()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        // L'activity repasse en avant plan : on relance la mise à jour des messages
        handler.post(updateRunnable)
    }

    override fun onPause() {
        // L'activity passe en arrière-plan : on coupe la mise à jour des messages :
        // Pour ce faire, on vire de la file d'attente le job qui était posté.
        handler.removeCallbacks(updateRunnable)
        super.onPause()
    }

    override fun onScroll(event: ScrollEvent?): Boolean {
        Log.e("TAG", "onCreate:la ${event?.source?.getMapCenter()?.latitude}")
        Log.e("TAG", "onCreate:lo ${event?.source?.getMapCenter()?.longitude}")
        //  Log.e("TAG", "onScroll   x: ${event?.x}  y: ${event?.y}", )
        return true
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        Log.e("TAG", "onZoom zoom level: ${event?.zoomLevel}   source:  ${event?.source}")
        return false;
    }
}


