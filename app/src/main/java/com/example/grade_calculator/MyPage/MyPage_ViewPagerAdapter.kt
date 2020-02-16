package com.example.grade_calculator.MyPage

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grade_calculator.R
import kotlinx.android.synthetic.main.mypage_page.view.*

class MyPage_ViewPagerAdapter(
    val c: Context,
    val itemlist:ArrayList<ArrayList<MyPage_item>>,
    val listener: MyPageEventListener
): RecyclerView.Adapter<MyPage_ViewPagerAdapter.ViewHolder>(){


    interface MyPageEventListener{
        fun addGrade(view:View):MyPage_item
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

        adapter = MyPage_RecyclerViewAdapter(c, itemlist[position])
        holder.recyclerView.adapter = adapter

    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var recyclerView: RecyclerView
        var semester_status:TextView
        var addBtn:Button

        init{
            semester_status = itemView.findViewById(R.id.tv_semester_status)
            recyclerView = itemView.findViewById(R.id.my_page_recyclerview)
            addBtn = itemView.findViewById(R.id.addBtn)

            itemView.addBtn.setOnClickListener {
                var temp = listener.addGrade(itemView)
                Log.d("return1",temp.toString())
            }
        }
    }

}