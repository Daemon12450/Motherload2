package com.example.motherload2.Character

class Offers(offer_id:Int,item_id:String,quantite : String, prix:String) {
    internal val offer_id = offer_id
    internal val item_id = item_id
    internal val quantite = quantite
    internal val prix = prix
    internal var name :String = ""

    fun setname(name: String){
        this.name = name
    }
}