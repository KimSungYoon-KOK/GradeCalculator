package com.kok.grade_calculator.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.kok.grade_calculator.App
import com.kok.grade_calculator.Calculator
import com.kok.grade_calculator.R
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val nowGPA = App.prefs.getTotalGPA()
        val goalGPA = App.prefs.getUserGoal()
        setProgressBar(nowGPA, goalGPA)
        initText()
    }

    private fun setProgressBar(nowGPA: Float, goalGPA: Float) {
        val animationDuration = 2500 // 2500ms = 2,5s
        progressBar.setProgressWithAnimation(
            //퍼센테이지
            (nowGPA / goalGPA * 100),
            animationDuration
        ) // Default duration = 1500ms

    }

    private fun initText(){
        tv_nowGrade.text = App.prefs.getTotalGPA().toString()
        tv_restCredit.text = ((App.prefs.getGraduate() - App.prefs.getTotalCredit()).toString() + " 학점")
        val temp = Calculator().needGPA_Calculate(
            App.prefs.getGraduate(), App.prefs.getTotalCredit(), App.prefs.getTotalCredit_P(), App.prefs.getUserGoal(), App.prefs.getTotalGPA())
        if(temp <= App.prefs.getUserGoal() && App.prefs.getTotalCredit() != 0){
            tv_needgrade.text = "목표 달성"
        }else {
            tv_needgrade.text = (Math.round(temp*100)/100.0).toString()
        }

        val temp2 = Calculator().needGPA_Calculate(
            App.prefs.getGraduate(), App.prefs.getTotalCredit(), App.prefs.getTotalCredit_P(), App.prefs.getUserGoal(), App.prefs.getRetakeGPA())
        if(temp2 <= App.prefs.getUserGoal() && App.prefs.getTotalCredit() != 0){
            tv_retake_needgrade.text = "목표 달성"
        }else {
            tv_retake_needgrade.text = (Math.round(temp2*100)/100.0).toString()
        }

    }
}