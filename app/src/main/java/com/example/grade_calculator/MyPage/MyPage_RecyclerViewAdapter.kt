package com.example.grade_calculator.MyPage

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
import com.example.grade_calculator.R
import kotlinx.android.synthetic.main.mypage_item.view.*

class MyPage_RecyclerViewAdapter(
    val context: Context,
    var listener: RecyclerViewAdapterEventListener,
    var items:ArrayList<MyPage_item>
): RecyclerView.Adapter<MyPage_RecyclerViewAdapter.ViewHolder>(){

    interface RecyclerViewAdapterEventListener{
        fun onChangeCallback(view: View, items: ArrayList<MyPage_item>)
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
        holder.item_grade.text = data.grade.toString()
        when(data.grade){
            10.toFloat() -> holder.item_grade.text = "P"
            else ->  holder.item_grade.text = data.grade.toString()
        }
        when(data.category){
            true -> holder.item_category.text = "전공"
            false -> holder.item_category.text = "교양"
        }

        if(data.grade >= 3.0){
            holder.item_retakeGrade.visibility = GONE
            holder.item_retakeGrade_not.visibility = VISIBLE
        }else{
            if(data.grade >= data.retakeGrade){
                holder.item_retakeGrade.text = "선택"
                //spinner 이용해서 재수강 성적 받고 다시 계산
            }else{
                //다시 계산하고 리사이클러 뷰 갱신
                when(data.retakeGrade){
                    4.5.toFloat()->{
                        holder.item_retakeGrade.text = "A+"
                    }
                    4.0.toFloat()->{
                        holder.item_retakeGrade.text = "A0"
                    }
                    3.5.toFloat()->{
                        holder.item_retakeGrade.text = "B+"
                    }
                    3.0.toFloat()->{
                        holder.item_retakeGrade.text = "B0"
                    }
                    2.5.toFloat()->{
                        holder.item_retakeGrade.text = "C+"
                    }
                    2.0.toFloat()->{
                        holder.item_retakeGrade.text = "C0"
                    }
                    1.5.toFloat()->{
                        holder.item_retakeGrade.text = "D+"
                    }
                    1.0.toFloat()->{
                        holder.item_retakeGrade.text = "D0"
                    }
                    10.toFloat()->{
                        holder.item_retakeGrade.text = "P"
                    }
                }
            }
        }

        listener.onChangeCallback(holder.itemView, items)

        //재수강 클릭 리스너
        holder.item_retakeGrade.setOnClickListener {
            val popupMenu = PopupMenu(context, it)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.aa->{
                        items[position].retakeGrade = 4.5.toFloat()
                        Log.d("itemslist",items.toString())
//                        listener.onChangeCallback(holder.itemView, items)
                        notifyDataSetChanged()
                        true
                    }
                    R.id.a0->{
                        items[position].retakeGrade = 4.0.toFloat()
                        Log.d("itemslist",items.toString())
//                        listener.onChangeCallback(holder.itemView, items)
                        notifyDataSetChanged()
                        true
                    }
                    R.id.bb->{
                        items[position].retakeGrade = 3.5.toFloat()
                        Log.d("itemslist",items.toString())
//                        listener.onChangeCallback(holder.itemView, items)
                        notifyDataSetChanged()
                        true
                    }
                    R.id.b0->{
                        items[position].retakeGrade = 3.0.toFloat()
                        Log.d("itemslist",items.toString())
//                        listener.onChangeCallback(holder.itemView, items)
                        notifyDataSetChanged()
                        true
                    }
                    R.id.cc->{
                        items[position].retakeGrade = 2.5.toFloat()
                       Log.d("itemslist",items.toString())
//                        listener.onChangeCallback(holder.itemView, items)
                        notifyDataSetChanged()
                        true
                    }
                    R.id.c0->{
                        items[position].retakeGrade = 2.0.toFloat()
                        Log.d("itemslist",items.toString())
//                        listener.onChangeCallback(holder.itemView, items)
                        notifyDataSetChanged()
                        true
                    }
                    R.id.dd->{
                        items[position].retakeGrade = 1.5.toFloat()
                        Log.d("itemslist",items.toString())
//                        listener.onChangeCallback(holder.itemView, items)
                        notifyDataSetChanged()
                        true
                    }
                    R.id.d0->{
                        items[position].retakeGrade = 1.0.toFloat()
                        Log.d("itemslist",items.toString())
                        //listener.onChangeCallback(holder.itemView, items)
                        notifyDataSetChanged()
                        true
                    }
                    R.id.p->{
                        items[position].retakeGrade = 10.toFloat()
                        Log.d("itemslist",items.toString())
                        //listener.onChangeCallback(holder.itemView, items)
                        notifyDataSetChanged()
                        true

                    }
                    else -> false
                }
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
}