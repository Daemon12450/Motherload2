package com.example.motherload2.View.Frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.motherload2.Connect.LanguageManager
import com.example.motherload2.ConnectView
import com.example.motherload2.R
import java.util.Locale.LanguageRange

class FragmentPlus: Fragment(R.layout.fragmentplus) {
    private lateinit var connectView: ConnectView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragmentplus, container, false)
        connectView = ViewModelProvider(this).get(ConnectView::class.java)


        val resetButton : Button = view.findViewById(R.id.reset)
        resetButton.setOnClickListener {
            val fragmentreset = FragmentReset()
            val fragmentT = parentFragmentManager.beginTransaction()
            fragmentT.replace(R.id.fragmentplus, fragmentreset)
            fragmentT.commit()
            connectView.reinitplayer()
        }

        val changeButton : Button = view.findViewById(R.id.changename)
        changeButton.setOnClickListener {
            val fragmentname = fragmentchangename()
            val fragmentT = parentFragmentManager.beginTransaction()
            fragmentT.replace(R.id.fragmentplus, fragmentname)
            fragmentT.commit()
        }





        return view
    }
}