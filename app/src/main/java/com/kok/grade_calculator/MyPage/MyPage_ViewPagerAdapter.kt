package com.kok.grade_calculator.MyPage

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kok.grade_calculator.App
import com.kok.grade_calculator.Calculator
import com.kok.grade_calculator.MainActivity
import com.kok.grade_calculator.R

class MyPage_ViewPagerAdapter(
    val c: Context,
    val itemlist:ArrayList<ArrayList<MyPage_item>>,
    val addListener: MyPageEventListener,
    val height: Int
): RecyclerView.Adapter<MyPage_ViewPagerAdapter.ViewHolder>(){

    private val SETTINGS_PLAYER_JSON = "settings_item_json"

    interface MyPageEventListener{
        fun addGrade(view:View, position: Int)
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
        holder.recyclerView.layoutParams.height = height
        //Log.d("height_recycler", holder.recyclerView.layoutParams.height.toString())

        val listener = object : MyPage_RecyclerViewAdapter.RecyclerViewAdapterEventListener{
            override fun onChangeCallback(items: ArrayList<MyPage_item>, index: Int, flag:Int) {
                val str = Calculator().semesterCalculate(itemlist,position) + "(" + Calculator().retakeCalculate(itemlist,position) + ")"
                holder.semester_status.text = str

                if(flag == 1){
                    val dataList = App.prefs.getStringArrayPref(SETTINGS_PLAYER_JSON)

                    for(i in dataList.indices) {
                        val data = dataList[i].split(" ")


                        if (data[1] == items[index].className && data[3].toFloat() == items[index].grade) {
                            dataList[i] =
                                items[index].semester.toString() + " " + items[index].className + " " + items[index].credit.toString() + " " + items[index].grade.toString() + " " + items[index].category.toString() + " " + items[index].retakeGrade.toString()
                        }
                    }

                    App.prefs.setStringArrayPref(SETTINGS_PLAYER_JSON,dataList)
                }


                //데이터 리스트 갱신
                val saveGPA:ArrayList<ArrayList<MyPage_item>> = ArrayList()
                for(i in 0..7){
                    val temp = ArrayList<MyPage_item>()
                    saveGPA.add(temp)
                }

                val sharedPrefList = App.prefs.getStringArrayPref(SETTINGS_PLAYER_JSON)
                if(sharedPrefList.isNotEmpty()){

                    for(i in sharedPrefList.indices){
                        val str = sharedPrefList[i].split(" ")

                        //str[0] : viewpager position / str[1] : className / str[2] : credit / str[3] : grade / str[4] : category / str[5] : retakeGrade
                        val tab_index = str[0].toInt()
                        val className = str[1]
                        val credit = str[2].toInt()
                        val grade = str[3].toFloat()
                        val category = when(str[4]){
                            "true" -> true
                            "false" -> false
                            else -> false
                        }
                        val retakeGrade = str[5].toFloat()

                        saveGPA[tab_index].add(MyPage_item(tab_index, className, credit, grade, category, retakeGrade))
                    }
                }

                var nowCredit = 0               //현재 이수한 학점(전체)
                var nowCredit_P = 0             //현재 이수한 패논패 과목 학점
                for(i in saveGPA.indices){
                    for(j in saveGPA[i].indices){
                        nowCredit += saveGPA[i][j].credit

                        if(saveGPA[i][j].grade == 10.toFloat())
                            nowCredit_P += saveGPA[i][j].credit
                    }
                }
                App.prefs.setTotalCredit(nowCredit)
                App.prefs.setTotalCredit_P(nowCredit_P)

                //현재 평점
                val nowGPA = Calculator().totalCalculate(saveGPA)
                App.prefs.setTotalGPA(nowGPA)

                //재수강 후 평점
                val retakeGPA = Calculator().retakeGPA(saveGPA)
                App.prefs.setRetakeGPA(retakeGPA)
            }
        }

        adapter = MyPage_RecyclerViewAdapter(c, listener, itemlist[position])
        holder.recyclerView.adapter = adapter

        // 밀어서 아이템 삭제 구현
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean { return true }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Adapter에 아이템 삭제 요청

                val builder = AlertDialog.Builder(c)
                builder.setTitle("과목을 삭제 하시겠습니까?")
                    .setPositiveButton("삭제") {
                        dialog, i -> adapter.removeTask(viewHolder.adapterPosition)
                    }
                    .setNegativeButton("취소") {
                        dialogInterface, i ->
                        adapter.NoRemove()
                        dialogInterface.cancel()
                    }
                    .create().show()
            }
        }).apply {
                // ItemTouchHelper에 RecyclerView 설정
                attachToRecyclerView(holder.recyclerView)
        }


        holder.addBtn.setOnClickListener {
            addListener.addGrade(it, position)
        }


        val str = Calculator().semesterCalculate(itemlist,position) + "(" + Calculator().retakeCalculate(itemlist,position) + ")"
        holder.semester_status.text = str
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var semester_status:TextView
        var recyclerView: RecyclerView
        var addBtn:TextView

        init{
            semester_status = itemView.findViewById(R.id.tv_semester_status)
            recyclerView = itemView.findViewById(R.id.my_page_recyclerview)
            addBtn = itemView.findViewById(R.id.addBtn)
        }
    }

}