package com.example.motherload2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.motherload2.Character.Character
import com.example.motherload2.Character.Item
import com.example.motherload2.Character.Marchant
import com.example.motherload2.Character.Offers
import com.example.motherload2.Character.Upgrade
import com.example.motherload2.Connect.Connection

class ConnectView : ViewModel() {
    /*

    Classe qui vas se changer de faire le lien entre les autre class et la class principal de communication avec le web

     */
    private val repository = Connection.getInstance()
    internal val perso = Character.getInstance("1.9365061f","47.8430441f")
    val marchant = Marchant.getInstance()
    val offre: LiveData<List<Offers>> = repository.offers // Directement lié au LiveData du Repository
    val sacitem: LiveData<List<Item>> = repository.item
    val upgrade: LiveData<List<Upgrade>> = repository.upgrad

    private val _selectedoffers = MutableLiveData<Offers?>()
    val selectedMessage: LiveData<Offers?> get() = _selectedoffers

    private val _selecteditem = MutableLiveData<Item?>()
    val selectedItem: LiveData<Item?> get() = _selecteditem
    private val _selectedup = MutableLiveData<Upgrade?>()
    val selectedup: LiveData<Upgrade?> get() = _selectedup

    fun connectWeb(login: String, password: String){
        repository.ConnectWeb(login,password)
        if (repository.connected){
            deplacement()
            statusplayer()
            market()
        }
    }
    fun getconnect():Boolean{
        return repository.getConnected()
    }
    fun changename(name:String){
        repository.changeName(name,perso)
    }
    fun deplacement(){
        repository.deplacement(perso)
    }
    fun statusplayer(){
        repository.statusplayer(perso)
    }
    fun reinitplayer(){
        repository.reinit_player()
    }
    fun dig(){
        repository.dig(perso)
    }
    fun detailitem(id:String,item:Item){
        repository.item_detail(id,item)
    }
    fun market(){
        repository.marketlist(marchant)
    }
    fun selectOffer(offers: Offers?){
        _selectedoffers.postValue (offers)
        Log.d("MsgViewModel","Offre sélectionné : "+offers?.offer_id)
    }
    fun selectItem(item: Item?){
        _selecteditem.postValue (item)
        Log.d("MsgViewModel","item sélectionné : "+item?.id)
    }
    fun selectup(item: Upgrade?){
        _selectedup.postValue (item)
        Log.d("MsgViewModel","item sélectionné : "+item?.pick_id)
    }
    fun buy(){

        repository.buy(selectedMessage.value?.offer_id.toString())
        //Log.d("Buy ",selectedMessage.value?.offer_id.toString())

    }
    fun sell(){
        repository.sell(selectedItem.value?.id.toString(),"1","100")
    }
    fun upgradelist(){
        repository.upgradelist()
    }
}