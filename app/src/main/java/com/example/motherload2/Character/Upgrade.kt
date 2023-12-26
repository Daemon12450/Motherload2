package com.example.motherload2.Character

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class Upgrade(id:Int) {

    internal val pick_id = id
    internal var items = listOf<Item>()
    private val _item = MutableLiveData<List<Item>>()
    val item: LiveData<List<Item>> get() = _item

    fun additems(item : Item){
        this.items += listOf(item)
        _item.postValue(this.items)
    }
}