package com.stairwaytodev.project.utils

import android.content.Context

class SharedPrefsHelper(context: Context) {

    private var ctx  = context

    fun saveStr(field : String, value : String){
        val sharedPref = ctx.getSharedPreferences(field, Context.MODE_PRIVATE)
        sharedPref.edit().putString(field, value).apply()
    }

    fun getStr(field : String) : String? {
        val shared = ctx.getSharedPreferences(field, Context.MODE_PRIVATE)
        return shared.getString(field,"")
    }

    fun saveLong(field : String, value : Long){
        val sharedPref = ctx.getSharedPreferences(field, Context.MODE_PRIVATE)
        sharedPref.edit().putLong(field, value).apply()
    }

    fun getLong(field : String) : Long{
        val shared = ctx.getSharedPreferences(field, Context.MODE_PRIVATE)
        return shared.getLong(field,0)
    }
}