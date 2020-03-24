package com.example.grade_calculator.MyPage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grade_calculator.App
import com.example.grade_calculator.Calculator
import com.example.grade_calculator.R

class MyPage_ViewPagerAdapter(
    val c: Context,
    val itemlist:ArrayList<ArrayList<MyPage_item>>,
    val addListener: MyPageEventListener
): RecyclerView.Adapter<MyPage_ViewPagerAdapter.ViewHolder>(){

    private val SETTINGS_PLAYER_JSON = "settings_item_json"

    interface MyPageEventListener{
        fun addGrade(view:View, position: Int)
        //fun onChangeCallback2(view:View, itemlist: ArrayList<ArrayList<MyPage_item>>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.mypage_page, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val layoutManager:RecyclerView.LayoutManager
        val adapter:MyPage_RecyclerViewAdapter
        layoutManager = LinearLayoutManager(c, RecyclerView.VERTICAL, false)
        holder.recyclerView.layoutManager = layoutManager

        val listener = object : MyPage_RecyclerViewAdapter.RecyclerViewAdapterEventListener{
            override fun onChangeCallback(view: View, items: ArrayList<MyPage_item>) {
                val str = Calculator().semesterCalculate(itemlist,position) + "(" + Calculator().retakeCalculate(itemlist,position) + ")"
                holder.semester_status.text = str

//                for(i in 0 until itemlist.size) {
//                    val strList = ArrayList<String>()
//                    for (j in 0 until itemlist[i].size) {
//                        val tempStr =
//                            i.toString() + " " + itemlist[i][j].className + " " + itemlist[i][j].credit.toString() + " " + itemlist[i][j].grade.toString() + " " + itemlist[i][j].category.toString() + " " + itemlist[i][j].retakeGrade.toString()
//                        strList.add(tempStr)
//                        App.prefs.setStringArrayPref(SETTINGS_PLAYER_JSON, strList)
//                    }
//                }
            }
        }

        adapter = MyPage_RecyclerViewAdapter(c, listener, itemlist[position])
        holder.recyclerView.adapter = adapter

        holder.addBtn.setOnClickListener {
            addListener.addGrade(it, position)
        }

        val str = Calculator().semesterCalculate(itemlist,position) + "(" + Calculator().retakeCalculate(itemlist,position) + ")"
        holder.semester_status.text = str
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var semester_status:TextView
        var recyclerView: RecyclerView
        var addBtn:Button

        init{
            semester_status = itemView.findViewById(R.id.tv_semester_status)
            recyclerView = itemView.findViewById(R.id.my_page_recyclerview)
            addBtn = itemView.findViewById(R.id.addBtn)

        }
    }

}