package com.example.motherload2.View

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.motherload2.ConnectView
import com.example.motherload2.R
import com.example.motherload2.View.Frag.FragmentPlus
import com.example.motherload2.databinding.GameactivityBinding
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class GameActivity : AppCompatActivity(), MapListener {
    /*
    activitée principale du jeu qui contient la map
     */
    private lateinit var connectView: ConnectView
    private var fragmentplus: FragmentPlus? = null

    lateinit var mMap: MapView;
    lateinit var controller: IMapController;
    lateinit var mMyLocationOverlay: MyLocationNewOverlay;
    private var myLocationMarker: Marker? = null

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            connectView.deplacement()
            handler.postDelayed(this, 15000) // Appelle toutes les 5 secondes
        }
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.gameactivity)
        connectView = ViewModelProvider(this).get(ConnectView::class.java)

        val binding = GameactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        )

        requestLocationPermission()

        mMap = binding.mapView
        mMap.setTileSource(TileSourceFactory.MAPNIK)
        mMap.setMultiTouchControls(true)
        mMap.getLocalVisibleRect(Rect())

        initializeLocation()

        //Log.d("same ?",connectView.getconnect().toString())

        val buttonShop: Button = findViewById(R.id.shop)
        buttonShop.setOnClickListener {
            val intent = Intent(this, ShopActivity::class.java)
            startActivity(intent)
        }

        val buttonDig: Button = findViewById(R.id.dig)
        buttonDig.setOnClickListener {
            connectView.dig(this)
        }

        val buttonInv: Button = findViewById(R.id.inv)
        buttonInv.setOnClickListener {
            val intent = Intent(this, InvActivity::class.java)

            startActivity(intent)
        }
        fragmentplus = FragmentPlus()

        val buttonPlus: Button = findViewById(R.id.plus)
        buttonPlus.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.fragmentplus)
            val fragmentT = supportFragmentManager.beginTransaction()
            if (fragment != null) {
                fragmentT.remove(fragment)
                fragmentT.commit()
            } else {
                fragmentT.add(R.id.fragmentplus, fragmentplus!!)
                fragmentT.commit()
            }
        }

        Log.d("TAG", "onCreate: mMap initialized")
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            initializeLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        }
    }

    private fun initializeLocation() {
        mMyLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), mMap)

        controller = mMap.controller

        mMyLocationOverlay.enableMyLocation()
        mMyLocationOverlay.enableFollowLocation()
        mMyLocationOverlay.isDrawAccuracyEnabled = true
        mMyLocationOverlay.runOnFirstFix {
            runOnUiThread {
                controller.setCenter(mMyLocationOverlay.myLocation)
                controller.animateTo(mMyLocationOverlay.myLocation)
            }
        }

        controller.setZoom(16.0)

        // Log.e("TAG", "onCreate:in ${controller.zoomIn()}")
        // Log.e("TAG", "onCreate: out  ${controller.zoomOut()}")

        // controller.animateTo(mapPoint)
        mMap.overlays.add(mMyLocationOverlay)

        mMap.addMapListener(this)
    }

    private fun addMyLocationMarker(latitude: Double, longitude: Double) {
        val startPoint = GeoPoint(latitude, longitude)

        if (myLocationMarker == null) {
            myLocationMarker = Marker(mMap)
            myLocationMarker!!.position = startPoint
            myLocationMarker!!.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            mMap.overlays.add(myLocationMarker)
        } else {
            myLocationMarker!!.position = startPoint
        }
        mMap.invalidate()

        connectView.perso.changecood(longitude.toString(), latitude.toString())

        Log.d("TAG", "addMyLocationMarker: Latitude = $latitude, Longitude = $longitude")
    }

    override fun onResume() {
        super.onResume()

        mMyLocationOverlay.runOnFirstFix {
            runOnUiThread {
                controller.setCenter(mMyLocationOverlay.myLocation)
                controller.animateTo(mMyLocationOverlay.myLocation)

                // Ajouter le marqueur avec vos coordonnées
                addMyLocationMarker(
                    mMyLocationOverlay.myLocation.latitude,
                    mMyLocationOverlay.myLocation.longitude
                )

                Log.d(
                    "TAG",
                    "addMyLocationMarker: ${mMyLocationOverlay.myLocation.latitude}, ${mMyLocationOverlay.myLocation.longitude}"
                )
            }
        }

        // L'activity repasse en avant plan : on relance la mise à jour des messages
        handler.post(updateRunnable)

        Log.d("TAG", "onResume: fonction utilisé")
    }

    override fun onPause() {
        // L'activity passe en arrière-plan : on coupe la mise à jour des messages :
        // Pour ce faire, on vire de la file d'attente le job qui était posté.

        // Le marqueur est supprimé car la carte n'est pas actuellement affiché
        removeMyLocationMarker()

        handler.removeCallbacks(updateRunnable)
        super.onPause()

        Log.d("TAG", "onPause: fonction utilisé")
    }

    override fun onScroll(event: ScrollEvent?): Boolean {
        // Log.e("TAG", "onCreate:la ${event?.source?.getMapCenter()?.latitude}")
        // Log.e("TAG", "onCreate:lo ${event?.source?.getMapCenter()?.longitude}")
        //  Log.e("TAG", "onScroll   x: ${event?.x}  y: ${event?.y}", )
        return true

        Log.d("TAG", "onScroll: fonction utilisé")
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        //  Log.e("TAG", "onZoom zoom level: ${event?.zoomLevel}   source:  ${event?.source}")
        return false;

        Log.d("TAG", "onZoom: fonction utilisé")
    }

    private fun removeMyLocationMarker() {
        if (myLocationMarker != null) {
            mMap.overlays.remove(myLocationMarker)
            myLocationMarker = null
            mMap.invalidate()
        }

        Log.d("TAG", "removeMyLocationMarker: fonction utilisé")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission accordée, initialisez la localisation
                    initializeLocation()
                }
                return
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
