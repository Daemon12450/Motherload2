package com.example.motherload2.View.Frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motherload2.Character.Character
import com.example.motherload2.Character.Item
import com.example.motherload2.Character.Offers
import com.example.motherload2.ConnectView
import com.example.motherload2.R


class SacFragment(): Fragment() {
    private val mListener: OnListFragmentInteractionListener = object :
        OnListFragmentInteractionListener {
        override fun onListFragmentInteraction(item: Item?) {connectView.selectItem(item)}
    }
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Item?)
    }
    private var litem : List<Item>? = null
    private var mAdapter:SacRecycler? = null
    private lateinit var connectView: ConnectView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connectView = ViewModelProvider(requireActivity())[ConnectView::class.java]
        connectView.sacitem.observe(viewLifecycleOwner, { item -> mAdapter?.updateItem(item)
        })
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_sac, container, false)

        //mdecFrText = ret.findViewById<View>(R.id.dec) as TextView
        // définir l'adapter
        //val view = ret.findViewById<View>(R.id.list) as RecyclerView
        if (view is RecyclerView) {
            val context = view.getContext()
            val recyclerView = view
            recyclerView.layoutManager = LinearLayoutManager(context)
            if (litem == null) litem = ArrayList()
            if (mAdapter == null) mAdapter = SacRecycler(mListener)
            recyclerView.adapter = mAdapter
        }
        return view
    }
}