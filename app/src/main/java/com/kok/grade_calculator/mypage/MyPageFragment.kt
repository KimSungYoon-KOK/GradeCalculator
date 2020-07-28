package com.kok.grade_calculator.mypage

import android.app.AlertDialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdListener
import com.kok.grade_calculator.App
import com.kok.grade_calculator.Calculator
import com.kok.grade_calculator.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_mypage.*


class MyPageFragment : Fragment() {

    private val SETTINGS_PLAYER_JSON = "settings_item_json"

    var gpaList = ArrayList<ArrayList<MyPageItem>>()    //학점 정보 저장하는 이차원 리스트
    var prefList = ArrayList<String>()                  //SharedPreferences 저장시 사용하는 임시 리스트

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mypage, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
        initViewPager()
    }

    private fun init(){
        //광고 추가
        MobileAds.initialize(requireContext()) {}
        adView.loadAd(AdRequest.Builder().build())

        // 광고가 제대로 로드 되는지 테스트 하기 위한 코드
        adView.adListener = object : AdListener() {
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
        }
    }

    private fun initViewPager() {
        //1학년 1학기 ~ 6학년 2학기까지 8개의 뷰페이저 생성
        for(i in 0..11){
            val temp = ArrayList<MyPageItem>()
            gpaList.add(temp)
        }

        ////////////////////////////이전에 저장된 정보를 불러와서 리사이클러뷰에 부착///////////////////////////
        prefList = App.prefs.getStringArrayPref(SETTINGS_PLAYER_JSON)
        if(prefList.isNotEmpty()) {
            for(i in prefList.indices) {
                val str = prefList[i].split(" ")

                //str[0] : viewpager position / str[1] : className / str[2] : credit / str[3] : grade / str[4] : category / str[5] : retakeGrade
                val tabIndex = str[0].toInt()
                val className = str[1]
                val credit = str[2].toInt()
                val grade = str[3].toFloat()
                val category = when(str[4]) {
                    "true" -> true
                    "false" -> false
                    else -> false
                }
                val retakeGrade = str[5].toFloat()
                gpaList[tabIndex].add(MyPageItem(tabIndex, className, credit, grade, category, retakeGrade))
            }
        }
        Log.d("Log_GpaList_before_add", gpaList.toString())
        mypageViewpager.adapter = MyPageViewPagerAdapter(gpaList, prefList)

        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        TabLayoutMediator(tabLayout, mypageViewpager){
            tab, position ->
            tab.text = when(position){
                0 -> "1학년 1학기"
                1 -> "1학년 2학기"
                2 -> "2학년 1학기"
                3 -> "2학년 2학기"
                4 -> "3학년 1학기"
                5 -> "3학년 2학기"
                6 -> "4학년 1학기"
                7 -> "4학년 2학기"
                8 -> "5학년 1학기"
                9 -> "5학년 2학기"
                10 -> "6학년 1학기"
                11 -> "6학년 2학기"
                else ->"ER"
            }
        }.attach()

    }

}