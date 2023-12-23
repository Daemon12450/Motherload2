package com.example.motherload2.Connect

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import java.util.Locale

class LanguageManager(ct:Context) {

    private var ct:Context = ct

    fun updateResouces(code:String){
        var local : Locale = Locale(code)
        Locale.setDefault(local)
        val resource : Resources = ct.resources
        val conf : Configuration = resource.configuration
        conf.locale = local
        resource.updateConfiguration(conf,resource.displayMetrics)


    }

}