package com.example.grade_calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment(){

       override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()

    }

    fun init(){

        //졸업 기준 학점
        var graduateCredit = App.prefs.getGraduate()
        //졸업 전공 기준 학점
        var graduateMajorCredit = App.prefs.getMajorGraduate()
        //목표 평점
        var goalGPA = App.prefs.getUserGoal()
        //현재 이수한 학점(전체)
        val nowCredit = App.prefs.getTotalCredit()
        //현재 이수한 패논패 과목 학점
        val nowCredit_P = App.prefs.getTotalCredit_P()
        //현재 평점
        val nowGPA = App.prefs.getTotalGPA()

        tv_graduate_credit.text = graduateCredit.toString()
        tv_graduate_major_credit.text = graduateMajorCredit.toString()
        tv_goalGPA.text = goalGPA.toString()
        tv_rest_credit.text = (graduateCredit - nowCredit).toString()
        tv_nowGPA.text = nowGPA.toString()
        tv_needGPA.text= Calculator().needGPA_Calculate(graduateCredit, nowCredit, nowCredit_P, goalGPA, nowGPA).toString()
    }

}