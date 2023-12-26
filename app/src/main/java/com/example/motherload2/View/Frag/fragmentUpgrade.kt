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
import com.example.motherload2.Character.Upgrade
import com.example.motherload2.ConnectView
import com.example.motherload2.R

class fragmentUpgrade : Fragment() {
    private val mListener: OnListFragmentInteractionListener = object :
        OnListFragmentInteractionListener {
        override fun onListFragmentInteraction(item: Upgrade?) {connectView.selectup(item)}
    }
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Upgrade?)
    }

    private var Fragmaent : Recette? = null
    private var litem : List<Upgrade>? = null
    private var mAdapter: UpgradeRecycler? = null
    private lateinit var connectView: ConnectView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connectView = ViewModelProvider(requireActivity())[ConnectView::class.java]
        connectView.upgrade.observe(viewLifecycleOwner, { item -> mAdapter?.updateUpgrades(item)
        })
        Fragmaent = parentFragmentManager.findFragmentById(R.id.Items) as Recette?
        Log.d("teste","end")
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
            recyclerView.layoutManager = LinearLayoutManager(context)
            if (litem == null) litem = ArrayList()
            if (mAdapter == null) mAdapter = UpgradeRecycler(mListener)
            recyclerView.adapter = mAdapter
        }
        return view
    }
}