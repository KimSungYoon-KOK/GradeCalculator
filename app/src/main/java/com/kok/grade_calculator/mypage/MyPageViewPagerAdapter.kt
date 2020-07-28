package com.kok.grade_calculator.mypage

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
import com.kok.grade_calculator.R

class MyPageViewPagerAdapter(
    private val gpaList:ArrayList<ArrayList<MyPageItem>>,
    private val prefList: ArrayList<String>
): RecyclerView.Adapter<MyPageViewPagerAdapter.ViewHolder>() {

    private lateinit var context: Context
    private val SETTINGS_PLAYER_JSON = "settings_item_json"
    private val calculator = Calculator()

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var semesterStatus:TextView = itemView.findViewById(R.id.tv_semester_status)
        var recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerview_mypage)
        var addBtn:TextView = itemView.findViewById(R.id.addBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val v = LayoutInflater.from(context).inflate(R.layout.mypage_page, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return gpaList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //RecyclerView Adapter
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        holder.recyclerView.layoutManager = layoutManager

        val listener = object : MyPageRecyclerViewAdapter.RecyclerViewAdapterEventListener {
            override fun changeCallback() {
                Log.d("Log_callback_gpaList", gpaList.toString())
                Log.d("Log_callback_prefList", prefList.toString())

                val str = "${calculator.semesterCalculate(gpaList,position)} (${calculator.retakeCalculate(gpaList,position)})"
                holder.semesterStatus.text = str
                notifyDataSetChanged()
            }
        }
        val adapter = MyPageRecyclerViewAdapter(listener, gpaList[position], prefList)
        holder.recyclerView.adapter = adapter

        //과목 추가 버튼 Listener
        holder.addBtn.setOnClickListener {
            addBtnClickEventListener(position)
        }

        // 밀어서 아이템 삭제 구현
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean { return true }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteDialog(adapter, viewHolder)
            }
        }).apply { attachToRecyclerView(holder.recyclerView) }

        val str = "${calculator.semesterCalculate(gpaList,position)} (${calculator.retakeCalculate(gpaList,position)})"
        holder.semesterStatus.text = str
    }


    //밀어서 아이템 삭제 시 다이얼로그 알림
    fun deleteDialog(adapter: MyPageRecyclerViewAdapter, viewHolder: RecyclerView.ViewHolder){
        AlertDialog.Builder(context).apply {
            setTitle("과목을 삭제 하시겠습니까?")
            setPositiveButton("삭제") { _,_ ->
                adapter.deleteItem(viewHolder.adapterPosition)
            }
            setNegativeButton("취소") { dialogInterface, _ ->
                adapter.unDeleteItem()
                dialogInterface.cancel()
            }
            create()
            show()
        }
    }


    //과목 추가 버튼 클릭 리스너
    private fun addBtnClickEventListener(position: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.add_dialog,null)
        val etClassname = dialogView.findViewById<EditText>(R.id.et_className)
        val spinnerCredit = dialogView.findViewById<Spinner>(R.id.spinner_credit)
        val spinnerGrade = dialogView.findViewById<Spinner>(R.id.spinner_grade)
        val spinnerCategory = dialogView.findViewById<Spinner>(R.id.spinner_category)

        AlertDialog.Builder(context).apply {
            setView(dialogView)
            setTitle("과목을 추가합니다")
            setPositiveButton("추가") { _, _ ->
                var className = etClassname.text.toString()
                className = className.replace(" ", "")

                if(className.length < 2){
                    Toast.makeText(context,"과목명은 두 글자 이상 입력해주세요", Toast.LENGTH_SHORT).show()
                } else {
                    val credit = spinnerCredit.selectedItemPosition + 1
                    val grade:Float = when(spinnerGrade.selectedItemPosition) {
                        0-> 4.5.toFloat()
                        1-> 4.0.toFloat()
                        2-> 3.5.toFloat()
                        3-> 3.0.toFloat()
                        4-> 2.5.toFloat()
                        5-> 2.0.toFloat()
                        6-> 1.5.toFloat()
                        7-> 1.0.toFloat()
                        8-> 0.toFloat()         //F
                        9-> 10.toFloat()        //P(패논패 과목)
                        else -> (-1).toFloat()
                    }
                    val category = when(spinnerCategory.selectedItemPosition) {
                        0 -> true
                        1 -> false
                        else -> false
                    }

                    //List 추가
                    val info = MyPageItem(position, className, credit, grade, category,grade)
                    gpaList[position].add(info)
                    Log.d("Log_GpaList_after_add", gpaList.toString())

                    //SharedPreference 저장
                    val str = "$position $className $credit $grade $category $grade"
                    prefList.add(str)
                    App.prefs.setStringArrayPref(SETTINGS_PLAYER_JSON, prefList)

                    //전체 수강 학점 저장
                    val totalCredit = App.prefs.getTotalCredit() + credit
                    if (grade == 10.toFloat()) { //패논패 과목일 경우 패논패 과목 이수 학점만 따로 저장
                        val totalCreditPF = App.prefs.getTotalCreditPF() + credit
                        App.prefs.updateCredit(totalCredit, totalCreditPF)
                    } else {
                        App.prefs.setTotalCredit(totalCredit)
                    }

                    //현재 평점 저장
                    App.prefs.setTotalGPA(calculator.totalCalculate(gpaList))

                    //과목 추가 메세지 출력 및 프레그먼트 교체
                    Toast.makeText(context, "${className}이 추가 되었습니다.",Toast.LENGTH_SHORT).show()
                    notifyDataSetChanged()
                }
            }
            setNegativeButton("취소") { dialogInterface, _ -> dialogInterface.cancel() }
            create()
            show()
        }

    }

}