package com.example.grade_calculator.MyPage

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.grade_calculator.App
import com.example.grade_calculator.Calculator
import com.example.grade_calculator.MySharedPreferences
import com.example.grade_calculator.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_mypage.*


class MypageFragment : Fragment() {

    private val SETTINGS_PLAYER_JSON = "settings_item_json"

    lateinit var saveGPA:ArrayList<ArrayList<MyPage_item>>          //학점 정보 저장하는 이차원 리스트
    lateinit var sharedPrefList:ArrayList<String>                         //sharedpreferences 저장시 사용하는 임시 리스트


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
        sharedPrefList = ArrayList()
        initViewPager()
    }

    fun initViewPager(){

        ////////////////////////////1학년 1학기 ~ 4학년 2학기까지 8개의 뷰페이저 생성/////////////////////////////
        for(i in 0..7){
            val temp = ArrayList<MyPage_item>()
            saveGPA.add(temp)
        }

        ////////////////////////////이전에 저장된 정보를 불러와서 리사이클러뷰에 부착///////////////////////////
        sharedPrefList = App.prefs.getStringArrayPref(SETTINGS_PLAYER_JSON)
        if(sharedPrefList.isNotEmpty()){

            for(i in 0 until sharedPrefList.size){
                val str = sharedPrefList[i].split(" ")

                //str[0] : viewpager position / str[1] : className / str[2] : credit / str[3] : grade / str[4] : category / str[5] : retakeGrade
                val tab_index = str[0].toInt()
                val className = str[1]
                val credit = str[2].toInt()
                val grade = str[3].toFloat()
                var category:Boolean
                when(str[4]){
                    "true" -> category = true
                    "false" -> category = false
                    else -> category = false
                }
                val retakeGrade = str[5].toFloat()

                saveGPA[tab_index].add(MyPage_item(className, credit, grade, category, retakeGrade))
            }

            //Log.d("saveLog",saveGPA.toString())
        }


        ////////////////////////////////학점 추가하기 버튼 리스너////////////////////////////////////
        val listener = object : MyPage_ViewPagerAdapter.MyPageEventListener{
            override fun addGrade(view: View, position: Int) {

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

                        if(et_className.text.length < 2){
                            Toast.makeText(requireContext(),"과목명을 두 글자 이상 입력해주세요", Toast.LENGTH_SHORT).show()
                        }else{
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
                            var tempCategory = false
                            when(spinner_category.selectedItemPosition){
                                0->tempCategory = true
                                1->tempCategory = false
                            }

                            //dialog에 작성한 과목 정보를 saveGPA에 추가
                            saveGPA[position].add(MyPage_item(et_className.text.toString(), (spinner_credit.selectedItemPosition+1), tempGrade, tempCategory,tempGrade))

                            //MyPage_item 객체를 하나의 String으로 만들어서 sharedpreference에 저장
                            sharedPrefList.add(position.toString() + " " + et_className.text.toString() + " " + (spinner_credit.selectedItemPosition+1).toString() +  " " + tempGrade.toString() +  " " + tempCategory.toString() +  " " + tempGrade.toString())
                            App.prefs.setStringArrayPref(SETTINGS_PLAYER_JSON, sharedPrefList)

                            Toast.makeText(requireContext(), "${et_className.text}과목이 추가 되었습니다.",Toast.LENGTH_SHORT).show()

                            val ft = fragmentManager!!.beginTransaction()
                            ft.detach(this@MypageFragment).attach(this@MypageFragment).commit()
                        }
                    }
                    .setNegativeButton("취소"){
                        dialogInterface, i -> dialogInterface.cancel()
                    }
                builder.create()
                builder.show()

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