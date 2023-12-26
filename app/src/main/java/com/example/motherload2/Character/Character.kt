package com.example.motherload2.Character

class Character (lon:String,lat:String) {
    internal var name : String = ""
    private var lon : String = lon
    private var lat : String = lat
    private lateinit var voisins : String
    internal var money : String = ""
    internal var pickaxe : String = ""

    internal var Lid = listOf<String>()
    internal var quantity = listOf<String>()
    internal var items = listOf<Item>()
    internal val CListe = ArrayList<Voisins>()

    companion object {
        @Volatile
        private var INSTANCE: Character? = null

        fun getInstance(lon:String,lat:String): Character {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Character(lon,lat).also { INSTANCE = it }
            }
        }
    }

    fun changename(name:String){
        this.name = name
    }
    fun changecood(lon: String, lat: String){
        this.lon = lon
        this.lat = lat
    }
    fun getlon():String {
        return this.lon
    }
    fun getlat():String{
        return this.lat
    }

    fun getvoisins():String{
        return this.voisins
    }

    fun setvoisins(voisins : String){
        this.voisins = voisins
    }

    fun setmoney(money : String){
        this.money = money
    }

    fun setpick(pick :String){
        this.pickaxe = pick
    }

    fun additems(item : Item){
        this.items += listOf(item)
    }

    fun addvoisins (voisins:Voisins){
        this.CListe += voisins
    }
    fun addquantity(quantity :String){
        this.quantity += quantity
    }
}