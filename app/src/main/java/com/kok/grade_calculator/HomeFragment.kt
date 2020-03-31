package com.kok.grade_calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
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

        //광고 추가
        //MobileAds.initialize(requireContext()) {}
        //adView_home.loadAd(AdRequest.Builder().build())
    }


    fun setProgressBar(nowGPA: Float, goalGPA: Float) {
        val animationDuration = 2500 // 2500ms = 2,5s
        progressBar.setProgressWithAnimation(
            //퍼센테이지
            (nowGPA / goalGPA * 100),
            animationDuration
        ) // Default duration = 1500ms

    }

    fun initText(){
        tv_nowGrede.text = App.prefs.getTotalGPA().toString()
        tv_restCredit.text = ((App.prefs.getGraduate() - App.prefs.getTotalCredit()).toString() + " 학점")
        val temp = Calculator().needGPA_Calculate(App.prefs.getGraduate(), App.prefs.getTotalCredit(), App.prefs.getTotalCredit_P(), App.prefs.getUserGoal(), App.prefs.getTotalGPA())
        tv_needgrade.text = (Math.round(temp*100)/100.0).toString()
    }
}