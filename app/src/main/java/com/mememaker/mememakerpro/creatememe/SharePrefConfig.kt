package com.mememaker.mememakerpro.creatememe

import android.content.Context
import android.net.Uri
import com.mememaker.mememakerpro.creatememe.model.PopularModel


class SharePrefConfig {
    companion object{
        var key="uri_list_key"

        // Function to save a list of URIs to SharedPreferences
        fun saveUriListToSharedPreferences(context: Context, uriList: List<Uri>) {
            val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val uriSet = uriList.map { it.toString() }.toSet()
            editor.putStringSet(key, uriSet)
            editor.apply()
        }

        // Function to retrieve a list of URIs from SharedPreferences
        fun getUriListFromSharedPreferences(context: Context): ArrayList<Uri> {
            val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val uriSet = sharedPreferences.getStringSet(key, emptySet()) ?: emptySet()
            return uriSet.map { Uri.parse(it) } as ArrayList<Uri>
        }

        // Function to save a list of URIs to SharedPreferences
//        fun saveUriListToSharedPreferences(context: Context, uriList: List<PopularModel>) {
//            val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//            val editor = sharedPreferences.edit()
//            val uriSet = uriList.map { it.toString() }.toSet()
//            editor.putStringSet(key, uriSet)
//            editor.apply()
//        }
//
//        // Function to retrieve a list of URIs from SharedPreferences
//        fun getUriListFromSharedPreferences(context: Context): ArrayList<PopularModel> {
//            val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//            val uriSet = sharedPreferences.getStringSet(key, emptySet()) ?: emptySet()
//            return uriSet.map { Uri.parse(it) } as ArrayList<PopularModel>
//        }

    }
}