package com.example.motherload2.View.Frag

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.motherload2.ConnectView
import com.example.motherload2.R

class fragmentchangename : Fragment(R.layout.fragmentname){
    private lateinit var connectView: ConnectView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragmentname, container, false)
        connectView = ViewModelProvider(this).get(ConnectView::class.java)

        val nom : EditText = view.findViewById(R.id.editname)

        val resetButton : Button = view.findViewById(R.id.change)
        resetButton.setOnClickListener {
            connectView.changename(nom.getText().toString())
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