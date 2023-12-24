package com.example.motherload2.View.Frag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.motherload2.Character.Item
import com.example.motherload2.R

class RecetteRecycler(private val mListener: Recette.OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<RecetteRecycler.ViewHolder>()  {
    private val mValues = mutableListOf<Item>()

    // Utiliser cette fonction pour mettre à jour la liste depuis le ViewModel
    fun updateItem(newItem: List<Item>) {
        mValues.clear()
        mValues.addAll(newItem)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetteRecycler.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }
    // Cette méthode est utilisée par la recyclerView pour afficher un message dans un item de la liste.
    // Il faut donc dans cette méthode mettre à jour les éléments de 'holder' de manière à ce qu'ils
    // affichent le message en position 'position'.
    override fun onBindViewHolder(holder: RecetteRecycler.ViewHolder, position: Int) {
        holder.mItem = mValues[position]
        holder.mnameView.text = mValues[position].nom
        var textContent = mValues[position].decFr
        if (textContent.length > 50) textContent = textContent.substring(0, 47) + "..."
        holder.mdecView.text = textContent
        holder.mtypeView.text = mValues[position].type
        holder.mrareView.text = mValues[position].rarete
        holder.mquantityView.text = mValues[position].quantity


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
        val mdecView: TextView
        val mtypeView: TextView
        val mrareView: TextView
        val mquantityView: TextView

        var mItem: Item? = null
        override fun toString(): String {
            return super.toString() + " '" + mnameView.text + "'"
        }

        init {
            mnameView = mView.findViewById<View>(R.id.name) as TextView
            mdecView = mView.findViewById<View>(R.id.dec) as TextView
            mtypeView = mView.findViewById<View>(R.id.type) as TextView
            mquantityView = mView.findViewById<View>(R.id.quantity) as TextView

            mrareView = mView.findViewById<View>(R.id.rare) as TextView
        }
    }


}