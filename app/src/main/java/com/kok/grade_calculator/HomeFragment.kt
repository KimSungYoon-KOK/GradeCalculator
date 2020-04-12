package com.kok.grade_calculator

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdListener
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
        init()
        initText()
    }

    fun setProgressBar(nowGPA: Float, goalGPA: Float) {
        val animationDuration = 2500 // 2500ms = 2,5s
        progressBar.setProgressWithAnimation(
            //퍼센테이지
            (nowGPA / goalGPA * 100),
            animationDuration
        ) // Default duration = 1500ms

    }

    //광고 로드
    fun init() {
        //광고 추가
        MobileAds.initialize(requireContext()) {}
        adView_home.loadAd(AdRequest.Builder().build())


        // 광고가 제대로 로드 되는지 테스트 하기 위한 코드입니다.
        adView_home.setAdListener(object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d("@@@", "onAdLoaded")
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                // Code to be executed when an ad request fails.
                Log.d("@@@", "onAdFailedToLoad $errorCode")
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that covers the screen.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return to the app after tapping on an ad.
            }
        })
    }

    fun initText(){
        tv_nowGrede.text = App.prefs.getTotalGPA().toString()
        tv_restCredit.text = ((App.prefs.getGraduate() - App.prefs.getTotalCredit()).toString() + " 학점")
        val temp = Calculator().needGPA_Calculate(App.prefs.getGraduate(), App.prefs.getTotalCredit(), App.prefs.getTotalCredit_P(), App.prefs.getUserGoal(), App.prefs.getTotalGPA())
        if(temp <= App.prefs.getUserGoal() && App.prefs.getTotalCredit() != 0){
            tv_needgrade.text = "목표 달성"
        }else {
            tv_needgrade.text = (Math.round(temp*100)/100.0).toString()
        }

        val temp2 = Calculator().needGPA_Calculate(App.prefs.getGraduate(), App.prefs.getTotalCredit(), App.prefs.getTotalCredit_P(), App.prefs.getUserGoal(), App.prefs.getRetakeGPA())
        if(temp2 <= App.prefs.getUserGoal() && App.prefs.getTotalCredit() != 0){
            tv_retake_needgrade.text = "목표 달성"
        }else {
            tv_retake_needgrade.text = (Math.round(temp2*100)/100.0).toString()
        }

    }
}