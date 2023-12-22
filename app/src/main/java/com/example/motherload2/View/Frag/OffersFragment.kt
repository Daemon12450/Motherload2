package com.example.motherload2.View.Frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motherload2.Character.Offers
import com.example.motherload2.ConnectView
import com.example.motherload2.R

class OffersFragment : Fragment() {// aucune idée de ce que je fais

    private val mListener: OnListFragmentInteractionListener = object :
        OnListFragmentInteractionListener {
        override fun onListFragmentInteraction(item: Offers?) {connectView.selectOffer(item)}
    }
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Offers?)
    }

    private var loffres : List<Offers>? = null
    private var mAdapter:OffersRecycler? = null
    private lateinit var connectView: ConnectView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connectView = ViewModelProvider(requireActivity())[ConnectView::class.java]
        connectView.offre.observe(viewLifecycleOwner, { offers -> mAdapter?.updateOffres(offers)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_offers_list, container, false)
        // définir l'adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            val recyclerView = view
            recyclerView.layoutManager = LinearLayoutManager(context)
            if (loffres == null) loffres = ArrayList()
            if (mAdapter == null) mAdapter = OffersRecycler(mListener)
            recyclerView.adapter = mAdapter
        }
        return view
    }
}