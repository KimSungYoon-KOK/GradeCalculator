package com.kok.grade_calculator.MyPage

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kok.grade_calculator.App
import com.kok.grade_calculator.R

class MyPage_RecyclerViewAdapter(
    val context: Context,
    var listener: RecyclerViewAdapterEventListener,
    var items:ArrayList<MyPage_item>
): RecyclerView.Adapter<MyPage_RecyclerViewAdapter.ViewHolder>(){

    interface RecyclerViewAdapterEventListener{
        fun onChangeCallback(items: ArrayList<MyPage_item>, index:Int, flag:Int)
    }

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

        val data = items[position]
        holder.item_className.text =  data.className
        holder.item_credit.text = data.credit.toString()
        holder.item_grade.text = when(data.grade){
            4.5.toFloat() -> "A+"
            4.0.toFloat() -> "A"
            3.5.toFloat() -> "B+"
            3.0.toFloat() -> "B"
            2.5.toFloat() -> "C+"
            2.0.toFloat() -> "C"
            1.5.toFloat() -> "D+"
            1.0.toFloat() -> "D"
            10.toFloat() -> "P"
            else -> data.grade.toString()
        }
        holder.item_category.text  = when(data.category){
            true -> "전공"
            false -> "교양"
        }

        if(data.grade >= 3.0){
            holder.item_retakeGrade.visibility = GONE
            holder.item_retakeGrade_not.visibility = VISIBLE
        }else{
            holder.item_retakeGrade.text = when(data.retakeGrade){
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
                else -> "ER"
            }
        }

        //재수강 클릭 리스너
        holder.item_retakeGrade.setOnClickListener {
            val popupMenu = PopupMenu(context, it)
            popupMenu.setOnMenuItemClickListener {
                items[position].retakeGrade = when(it.itemId){
                    R.id.aa -> 4.5.toFloat()
                    R.id.a0 -> 4.0.toFloat()
                    R.id.bb -> 3.5.toFloat()
                    R.id.b0 -> 3.0.toFloat()
                    R.id.cc -> 2.5.toFloat()
                    R.id.c0 -> 2.0.toFloat()
                    R.id.dd -> 1.5.toFloat()
                    R.id.d0 -> 1.0.toFloat()
                    R.id.p -> 10.toFloat()
                    else -> -1.toFloat()
                }
                notifyDataSetChanged()
                //Log.d("tag_recycler",items.toString())
                listener.onChangeCallback(items, position,1)
                true
            }
            popupMenu.inflate(R.menu.popupmenu_retake)

            try {
                val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                fieldMPopup.isAccessible = true
                val mPopup = fieldMPopup.get(popupMenu)
                mPopup.javaClass
                    .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(mPopup, true)
            } catch (e: Exception){
                Log.e("Main", "Error showing menu icons.", e)
            } finally {
                popupMenu.show()
            }

        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item_className: TextView
        var item_credit: TextView
        var item_grade: TextView
        var item_category: TextView
        var item_retakeGrade: TextView
        var item_retakeGrade_not : TextView

        init {
            item_className = itemView.findViewById(R.id.tv_className)
            item_credit = itemView.findViewById(R.id.tv_credit)
            item_grade = itemView.findViewById(R.id.tv_grade)
            item_category = itemView.findViewById(R.id.tv_category)
            item_retakeGrade = itemView.findViewById(R.id.tv_retakeGrade)
            item_retakeGrade_not = itemView.findViewById(R.id.tv_retakeGrade_not)
        }
    }

    private val SETTINGS_PLAYER_JSON = "settings_item_json"

    // 내부 데이터 값 제거
    fun removeTask(position: Int) {


        val dataList = App.prefs.getStringArrayPref(SETTINGS_PLAYER_JSON)

        for(i in dataList.indices) {
            val data = dataList[i].split(" ")

            if (data[0].toInt() == items[position].semester) {
                dataList.removeAt(i + position)
                break
            }
        }

        App.prefs.setStringArrayPref(SETTINGS_PLAYER_JSON,dataList)

        items.removeAt(position)

        listener.onChangeCallback(items, position,2)
        notifyDataSetChanged()
    }

    fun NoRemove(){
        notifyDataSetChanged()
    }

}