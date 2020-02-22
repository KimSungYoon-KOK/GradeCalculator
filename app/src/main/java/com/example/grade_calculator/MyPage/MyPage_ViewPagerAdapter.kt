package com.example.grade_calculator.MyPage

import android.app.AlertDialog
import android.content.Context
import android.media.AudioTrack
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grade_calculator.Calculator
import com.example.grade_calculator.R
import kotlinx.android.synthetic.main.mypage_page.view.*

class MyPage_ViewPagerAdapter(
    val c: Context,
    val itemlist:ArrayList<ArrayList<MyPage_item>>,
    val addListener: MyPageEventListener
): RecyclerView.Adapter<MyPage_ViewPagerAdapter.ViewHolder>(){


    interface MyPageEventListener{
        fun addGrade(view:View, position: Int)
        fun onChangeCallback2(view:View, itemlist: ArrayList<ArrayList<MyPage_item>>)
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
        var layoutManager:RecyclerView.LayoutManager
        var adapter:MyPage_RecyclerViewAdapter
        layoutManager = LinearLayoutManager(c, RecyclerView.VERTICAL, false)
        holder.recyclerView.layoutManager = layoutManager

        val listener = object : MyPage_RecyclerViewAdapter.RecyclerViewAdapterEventListener{
            override fun onChangeCallback(view: View, items: ArrayList<MyPage_item>) {
                addListener.onChangeCallback2(view, itemlist)
                Log.d("tag_2","onchangecallback2 호출")
                holder.semester_status.text =
                    Calculator().semesterCalculate(itemlist,position) + "(" + Calculator().retakeCalculate(itemlist,position) + ")"
            }

        }

        adapter = MyPage_RecyclerViewAdapter(c, listener, itemlist[position])
        holder.recyclerView.adapter = adapter

        holder.addBtn.setOnClickListener {
            addListener.addGrade(it, position)
        }

        holder.semester_status.text =
            Calculator().semesterCalculate(itemlist,position) + "(" + Calculator().retakeCalculate(itemlist,position) + ")"

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