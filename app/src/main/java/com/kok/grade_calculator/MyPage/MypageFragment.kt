package com.kok.grade_calculator.MyPage

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
import kotlinx.android.synthetic.main.mypage_page.*


class MypageFragment : Fragment() {

    private val SETTINGS_PLAYER_JSON = "settings_item_json"

    lateinit var saveGPA:ArrayList<ArrayList<MyPage_item>>          //학점 정보 저장하는 이차원 리스트
    lateinit var sharedPrefList:ArrayList<String>                   //sharedpreferences 저장시 사용하는 임시 리스트

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

        init()
        initViewPager()
    }

    fun init(){
        //광고 추가
        MobileAds.initialize(requireContext()) {}
        adView.loadAd(AdRequest.Builder().build())

        // 광고가 제대로 로드 되는지 테스트 하기 위한 코드입니다.
        adView.setAdListener(object : AdListener() {
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

        saveGPA = ArrayList()
        sharedPrefList = ArrayList()
    }

    fun initViewPager(){

        ///////////////////////////// RecyclerView Height Setting /////////////////////////////
        val resId = requireContext().resources.getIdentifier("navigation_bar_height","dimen","android")
        val navigationBarHeight = requireContext().resources.getDimensionPixelSize(resId)
        val dm = requireContext().resources.displayMetrics
        val height = dm.heightPixels - (280 * (dm.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt() - navigationBarHeight      //280dp to px


        ////////////////////////////1학년 1학기 ~ 4학년 2학기까지 8개의 뷰페이저 생성/////////////////////////////
        for(i in 0..7){
            val temp = ArrayList<MyPage_item>()
            saveGPA.add(temp)
        }

        ////////////////////////////이전에 저장된 정보를 불러와서 리사이클러뷰에 부착///////////////////////////
        sharedPrefList = App.prefs.getStringArrayPref(SETTINGS_PLAYER_JSON)
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
                builder.setTitle("과목을 추가합니다")
                    .setPositiveButton("추가"){
                        dialog, i ->

                        val tempClassName = et_className.text.toString().split(" ")
                        var strClassName = ""
                        for(i in tempClassName.indices){
                            strClassName += tempClassName[i]
                        }

                        if(strClassName.length < 2){
                            Toast.makeText(requireContext(),"과목명을 두 글자 이상 입력해주세요", Toast.LENGTH_SHORT).show()
                        }else{
                            val tempGrade:Float = when(spinner_grade.selectedItemPosition){
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
                                else -> -1.toFloat()
                            }
                            val tempCategory = when(spinner_category.selectedItemPosition){
                                0 -> true
                                1 -> false
                                else -> false
                            }

                            //dialog에 작성한 과목 정보를 saveGPA에 추가
                            saveGPA[position].add(MyPage_item(position, strClassName, (spinner_credit.selectedItemPosition+1), tempGrade, tempCategory,tempGrade))

                            //MyPage_item 객체를 하나의 String으로 만들어서 sharedpreference에 저장
                            sharedPrefList.clear()
                            for(k in saveGPA.indices){
                                for(j in saveGPA[k].indices){
                                    sharedPrefList.add(saveGPA[k][j].semester.toString() + " " + saveGPA[k][j].className + " " + saveGPA[k][j].credit.toString() + " " + saveGPA[k][j].grade.toString() + " " + saveGPA[k][j].category.toString() + " " + saveGPA[k][j].retakeGrade.toString())
                                }
                            }

                            App.prefs.setStringArrayPref(SETTINGS_PLAYER_JSON, sharedPrefList)

                            //전체 수강 학점 저장
                            val totalCredit = App.prefs.getTotalCredit() + (spinner_credit.selectedItemPosition+1)
                            App.prefs.setTotalCredit(totalCredit)

                            //패논패 과목일 경우 패논패 과목 이수 학점만 따로 저장
                            if(tempGrade == 10.toFloat()){
                               val totalCredit_P = App.prefs.getTotalCredit_P() + (spinner_credit.selectedItemPosition+1)
                                App.prefs.setTotalCredit_P(totalCredit_P)
                            }

                            //현재 평점 저장
                            App.prefs.setTotalGPA(Calculator().totalCalculate(saveGPA))

                            //과목 추가 메세지 출력 및 프레그먼트 교체
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

        mypage_viewpager.adapter = MyPage_ViewPagerAdapter(requireContext(), saveGPA, listener, height)

        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        TabLayoutMediator(tabLayout, mypage_viewpager){
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
                else ->"ER"
            }
        }.attach()

    }

}