package com.example.motherload2.View.Frag

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.motherload2.Character.Item
import com.example.motherload2.Character.Upgrades
import com.example.motherload2.ConnectView
import com.example.motherload2.R

class UpgradeRecycler(private val mListener: fragmentUpgrade.OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<UpgradeRecycler.ViewHolder>() {
    private val mValues = mutableListOf<Upgrades>()

    // Utiliser cette fonction pour mettre à jour la liste depuis le ViewModel
    fun updateUpgrades(newItem: List<Upgrades>) {
        mValues.clear()
        mValues.addAll(newItem)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpgradeRecycler.ViewHolder {
        Log.d("teste","in")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.upgrades, parent, false)
        return ViewHolder(view)
    }
    // Cette méthode est utilisée par la recyclerView pour afficher un message dans un item de la liste.
    // Il faut donc dans cette méthode mettre à jour les éléments de 'holder' de manière à ce qu'ils
    // affichent le message en position 'position'.
    override fun onBindViewHolder(holder: UpgradeRecycler.ViewHolder, position: Int) {
        holder.mItem = mValues[position]
        holder.mnameView.text = mValues[position].pick_id.toString()

        holder.mView.setOnClickListener { mListener?.onListFragmentInteraction(holder.mItem) }

    }
    override fun getItemCount(): Int {
        return mValues.size
    }
    // Notre viewHolder. Il s'agit de l'élément graphique correspondant à un item de la liste.
    // Le notre contient 3 champs texte destinés à afficher la date, l'auteur et le début du contenu
    // du message
    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mnameView: TextView

        var mItem: Upgrades? = null
        override fun toString(): String {
            return super.toString() + " '" + mnameView.text + "'"
        }

        init {

            mnameView = mView.findViewById<View>(R.id.pick) as TextView

        }
    }



}