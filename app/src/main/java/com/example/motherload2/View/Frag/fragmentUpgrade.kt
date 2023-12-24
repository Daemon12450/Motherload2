package com.example.motherload2.View.Frag

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motherload2.Character.Item
import com.example.motherload2.Character.Upgrades
import com.example.motherload2.ConnectView
import com.example.motherload2.R

class fragmentUpgrade : Fragment() {
    private val mListener: OnListFragmentInteractionListener = object :
        OnListFragmentInteractionListener {
        override fun onListFragmentInteraction(item: Upgrades?) {connectView.selectup(item)}
    }
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Upgrades?)
    }


    private var litem : List<Upgrades>? = null
    private var mAdapter: UpgradeRecycler? = null
    private lateinit var connectView: ConnectView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connectView = ViewModelProvider(requireActivity())[ConnectView::class.java]
        connectView.upgrades.observe(viewLifecycleOwner, { item -> mAdapter?.updateUpgrades(item)
        })
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_upgrade, container, false)

        //mdecFrText = ret.findViewById<View>(R.id.dec) as TextView
        // définir l'adapter
        //val view = ret.findViewById<View>(R.id.list) //as RecyclerView
        if (view is RecyclerView) {

            val context = view.context
            val recyclerView = view
            Log.d("teste",context.toString())
            recyclerView.layoutManager = LinearLayoutManager(context)
            Log.d("teste","out")
            if (litem == null) litem = ArrayList()
            if (mAdapter == null) mAdapter = UpgradeRecycler(mListener)
            recyclerView.adapter = mAdapter
        }
        return view
    }
}