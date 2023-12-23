package com.example.motherload2.View.Frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.motherload2.ConnectView
import com.example.motherload2.R

class FragmentReset : Fragment() {
    private lateinit var connectView: ConnectView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragmentreset, container, false)
        connectView = ViewModelProvider(this).get(ConnectView::class.java)


        val confButton : Button = view.findViewById(R.id.confirme)
        confButton.setOnClickListener {
            connectView.reinitplayer()
            val fragment = parentFragmentManager.findFragmentById(R.id.fragmentplus)
            val fragmentT = parentFragmentManager.beginTransaction()
            if (fragment != null) {
                fragmentT.remove(fragment)
                fragmentT.commit()
            }

        }

        val cancelButton : Button = view.findViewById(R.id.cancel)
        cancelButton.setOnClickListener {
            val fragment = parentFragmentManager.findFragmentById(R.id.fragmentplus)
            val fragmentT = parentFragmentManager.beginTransaction()
            if (fragment != null) {
                fragmentT.remove(fragment)
                fragmentT.commit()
            }
        }



        return view
    }
}