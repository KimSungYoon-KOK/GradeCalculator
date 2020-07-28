package com.kok.grade_calculator.mypage

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kok.grade_calculator.App
import com.kok.grade_calculator.R

class MyPageRecyclerViewAdapter(
    private var listener: RecyclerViewAdapterEventListener,
    private var items:ArrayList<MyPageItem>,
    private var prefList: ArrayList<String>
): RecyclerView.Adapter<MyPageRecyclerViewAdapter.ViewHolder>() {

    lateinit var context: Context
    private val SETTINGS_PLAYER_JSON = "settings_item_json"

    interface RecyclerViewAdapterEventListener{
        fun changeCallback()
//        fun retakeGradeCallback(position: Int, retakeGrade: Float)
//        fun removeCallback()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var className: TextView = itemView.findViewById(R.id.tv_className)
        var credit: TextView = itemView.findViewById(R.id.tv_credit)
        var grade: TextView = itemView.findViewById(R.id.tv_grade)
        var category: TextView = itemView.findViewById(R.id.tv_category)
        var retakeGrade: TextView = itemView.findViewById(R.id.tv_retakeGrade)
        var retakeGradeNot: TextView = itemView.findViewById(R.id.tv_retakeGrade_not)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageRecyclerViewAdapter.ViewHolder {
        context = parent.context
        val v = LayoutInflater.from(context).inflate(R.layout.mypage_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = items[position]
        holder.className.text =  data.className
        holder.credit.text = data.credit.toString()
        holder.grade.text = when(data.grade) {
            4.5.toFloat() -> "A+"
            4.0.toFloat() -> "A"
            3.5.toFloat() -> "B+"
            3.0.toFloat() -> "B"
            2.5.toFloat() -> "C+"
            2.0.toFloat() -> "C"
            1.5.toFloat() -> "D+"
            1.0.toFloat() -> "D"
            0.0.toFloat() -> "F"
            10.toFloat() -> "P"
            else -> "ERR"
        }
        holder.category.text  = when(data.category) {
            true -> "전공"
            false -> "교양"
        }

        if(data.grade >= 3.0){
            holder.retakeGrade.visibility = GONE
            holder.retakeGradeNot.visibility = VISIBLE
        }else{
            holder.retakeGrade.text = when(data.retakeGrade) {
                4.5.toFloat() -> "A+"
                4.0.toFloat() -> "A"
                3.5.toFloat() -> "B+"
                3.0.toFloat() -> "B"
                2.5.toFloat() -> "C+"
                2.0.toFloat() -> "C"
                1.5.toFloat() -> "D+"
                1.0.toFloat() -> "D"
                0.0.toFloat() -> "F"
                10.toFloat() -> "P"
                else -> "ERR"
            }
        }

        //재수강 클릭 리스너
        holder.retakeGrade.setOnClickListener {
            showPopupMenu(it, position)
        }

    }

    //재수강 클릭 팝업 메뉴
    private fun showPopupMenu(v: View, position: Int) {
        PopupMenu(context, v).apply {
            setOnMenuItemClickListener {
                return@setOnMenuItemClickListener when(it.itemId) {
                    R.id.aa -> {
                        retakeGrade(position, 4.5.toFloat())
                        true
                    }
                    R.id.a0 -> {
                        retakeGrade(position, 4.toFloat())
                        true
                    }
                    R.id.bb ->{
                        retakeGrade(position, 3.5.toFloat())
                        true
                    }
                    R.id.b0 -> {
                        retakeGrade(position, 3.toFloat())
                        true
                    }
                    R.id.cc -> {
                        retakeGrade(position, 2.5.toFloat())
                        true
                    }
                    R.id.c0 -> {
                        retakeGrade(position, 2.toFloat())
                        true
                    }
                    R.id.dd -> {
                        retakeGrade(position, 1.5.toFloat())
                        true
                    }
                    R.id.d0 -> {
                        retakeGrade(position, 1.toFloat())
                        true
                    }
                    R.id.p -> {
                        retakeGrade(position, 10.toFloat())
                        true
                    }
                    else -> false
                }
            }
            inflate(R.menu.popupmenu_retake)
            show()
        }
    }

    private fun retakeGrade(position: Int, retakeGrade: Float) {
        val item = items[position]
        for(i in prefList.indices) {
            val data = prefList[i].split(" ")
            if (data[1] == item.className) {
                val str = "${item.semester} ${item.className} ${item.credit} ${item.grade} ${item.category} $retakeGrade"
                prefList[i] = str
                break
            }
        }
        App.prefs.setStringArrayPref(SETTINGS_PLAYER_JSON, prefList)

        items[position].retakeGrade = retakeGrade
        listener.changeCallback()
        notifyDataSetChanged()
    }

    // 내부 데이터 값 제거
    fun deleteItem(position: Int) {
        val item = items[position]
        for(i in prefList.indices) {
            val data = prefList[i].split(" ")
            if (data[1] == item.className) {
                //SharedPreference 갱신
                val totalCredit = App.prefs.getTotalCredit() - item.credit
                App.prefs.setTotalCredit(totalCredit)
                if (item.grade == 10.toFloat()) {
                    val totalCreditPF = App.prefs.getTotalCreditPF() - item.credit
                    App.prefs.setTotalCreditPF(totalCreditPF)
                }
                prefList.removeAt(i)
                break
            }
        }
        App.prefs.setStringArrayPref(SETTINGS_PLAYER_JSON, prefList)
        items.removeAt(position)
        listener.changeCallback()
        notifyDataSetChanged()
    }

    fun unDeleteItem(){
        notifyDataSetChanged()
    }

}