package com.example.grade_calculator

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONException


class MySharedPreferences(context: Context) {

    val PREFS_FILENAME = "prefs"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)
    /* 파일 이름과 EditText를 저장할 Key 값을 만들고 prefs 인스턴스 초기화 */

    fun setStringArrayPref(
        key: String,
        values: ArrayList<String>
    ) {
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until values.size) {
            a.put(values[i])
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString())
        } else {
            editor.putString(key, null)
        }
        editor.apply()
    }

    fun getStringArrayPref(key: String):ArrayList<String>{
        val json = prefs.getString(key,null)
        val urls = ArrayList<String>()
        if(json != null){
            try {
                val a = JSONArray(json)
                for(i in 0 until a.length()){
                    val url = a.optString(i)
                    urls.add(url)
                }
            }catch (e: JSONException){
                e.printStackTrace()
            }
        }
        return urls
    }

}