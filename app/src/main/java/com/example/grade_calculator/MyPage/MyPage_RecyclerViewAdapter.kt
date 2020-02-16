package com.example.grade_calculator.MyPage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.grade_calculator.R

class MyPage_RecyclerViewAdapter(
    val context: Context,
    var items:ArrayList<MyPage_item>
): RecyclerView.Adapter<MyPage_RecyclerViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyPage_RecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.mypage_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item_className: TextView
        var item_credit: TextView
        var item_grade: TextView
        var item_category: TextView
        var item_retakeGrade: TextView

        init {
            item_className = itemView.findViewById(R.id.tv_className)
            item_credit = itemView.findViewById(R.id.tv_credit)
            item_grade = itemView.findViewById(R.id.tv_grade)
            item_category = itemView.findViewById(R.id.tv_category)
            item_retakeGrade = itemView.findViewById(R.id.tv_retakeGrade)
        }
    }
}