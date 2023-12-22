package com.example.motherload2.View.Frag

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.motherload2.Character.Item
import com.example.motherload2.Character.Offers
import com.example.motherload2.ConnectView
import com.example.motherload2.R

class OffredetailFrag : Fragment() {
    var mnameText: TextView? = null
    var mtypeText: TextView? = null
    var mrareText: TextView? = null
    var mdecFrText: TextView? = null
    var web : WebView? = null
    private lateinit var connectView: ConnectView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val ret = inflater.inflate(R.layout.fragment_offers_detail, container, false)
        mnameText = ret.findViewById<View>(R.id.name) as TextView
        mrareText = ret.findViewById<View>(R.id.rare) as TextView
        mtypeText = ret.findViewById<View>(R.id.type) as TextView
        mdecFrText = ret.findViewById<View>(R.id.dec) as TextView
        web = ret.findViewById(R.id.web) as WebView
        return ret
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connectView = ViewModelProvider(requireActivity()).get(ConnectView::class.java)
        connectView.selectedMessage.observe(viewLifecycleOwner, Observer {
                item -> update(item)
        })
    }

    fun update(offers: Offers?) {
        Log.d("NDFrag", "Update called")
        if (offers == null) {
            mnameText?.text = ""
            mtypeText?.text = ""
            mrareText?.text=""
            mdecFrText?.text=""
            return
        }

        // bidul
        mnameText?.text = offers.item.nom
        mtypeText?.text = offers.item.type
        mrareText?.text= offers.item.rarete
        mdecFrText?.text= offers.item.decFr
        val url = "https://test.vautard.fr/creuse_imgs/" + offers.item.image
        web?.loadUrl( url )

    }

    companion object {
        fun newInstance(): OffredetailFrag {
            return OffredetailFrag()
        }
    }
}