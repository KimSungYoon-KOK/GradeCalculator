package com.example.grade_calculator.MyPage

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.grade_calculator.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.add_dialog.*
import kotlinx.android.synthetic.main.fragment_mypage.*

class MypageFragment : Fragment() {

    lateinit var saveGPA:ArrayList<ArrayList<MyPage_item>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mypage, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        saveGPA = ArrayList()
        initViewPager()

    }

    lateinit var adapter:MyPage_ViewPagerAdapter
    fun initViewPager(){

        //임시 데이터
        var tempItem = MyPage_item("과목명",3,2.5.toFloat(),true,3.5.toFloat())
        var tempList = ArrayList<MyPage_item>()
        tempList.add(tempItem)
        for(i in 0..7){
            saveGPA.add(tempList)
        }

        val listener = object : MyPage_ViewPagerAdapter.MyPageEventListener{
            override fun addGrade(view: View):MyPage_item{

                var temp:MyPage_item = MyPage_item("과목명",3,2.5.toFloat(),true,3.5.toFloat())
                val dialogView = layoutInflater.inflate(R.layout.add_dialog,null)
                val et_className = dialogView.findViewById<EditText>(R.id.et_className)
                val spinner_credit = dialogView.findViewById<Spinner>(R.id.spinner_credit)
                val spinner_grade = dialogView.findViewById<Spinner>(R.id.spinner_grade)
                val spinner_category = dialogView.findViewById<Spinner>(R.id.spinner_category)

                val builder = AlertDialog.Builder(requireContext())
                builder.setView(dialogView)
                builder.setMessage("성적 추가")
                    .setPositiveButton("추가"){
                        dialog, i ->
                        var tempGrade:Float = 0.toFloat()
                        when(spinner_grade.selectedItemPosition){
                            0-> tempGrade = 4.5.toFloat()
                            1-> tempGrade = 4.0.toFloat()
                            2-> tempGrade = 3.5.toFloat()
                            3-> tempGrade = 3.0.toFloat()
                            4-> tempGrade = 2.5.toFloat()
                            5-> tempGrade = 2.0.toFloat()
                            6-> tempGrade = 1.5.toFloat()
                            7-> tempGrade = 1.0.toFloat()
                            8-> tempGrade = 0.toFloat()
                            9-> tempGrade = 10.toFloat()
                        }
                        var tempCategory:Boolean = true
                        when(spinner_category.selectedItemPosition){
                            0->true
                            1->false
                        }

                        temp = MyPage_item(et_className.text.toString(), (spinner_credit.selectedItemPosition+1), tempGrade, tempCategory,tempGrade)

                        //Toast.makeText(requireContext(),temp.toString(),Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("취소"){
                        dialogInterface, i -> dialogInterface.cancel()
                    }
                builder.create()
                builder.show()

                return temp
            }

        }

        mypage_viewpager.adapter = MyPage_ViewPagerAdapter(activity!!.applicationContext, saveGPA,listener)

        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        TabLayoutMediator(tabLayout, mypage_viewpager){
            tab, position ->
            when(position){
                0->{
                    tab.text = "1학년 1학기"
                }
                1->{
                    tab.text = "1학년 2학기"
                }
                2->{
                    tab.text = "2학년 1학기"
                }
                3->{
                    tab.text = "2학년 2학기"
                }
                4->{
                    tab.text = "3학년 1학기"
                }
                5->{
                    tab.text = "3학년 2학기"
                }
                6->{
                    tab.text = "4학년 1학기"
                }
                7->{
                    tab.text = "4학년 2학기"
                }
            }
        }.attach()

    }

}