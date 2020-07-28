package com.kok.grade_calculator.setting

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kok.grade_calculator.App
import com.kok.grade_calculator.R
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment(){

    private val SETTINGS_PLAYER_JSON = "settings_item_json"

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
        initBtn()
    }

    private fun init() {

        //졸업 기준 학점
        val graduateCredit = App.prefs.getGraduate()
        //졸업 전공 기준 학점
        val graduateMajorCredit = App.prefs.getMajorGraduate()
        //목표 평점
        val goalGPA = App.prefs.getUserGoal()

        //현재 이수한 학점(전체)
        var nowCredit = App.prefs.getTotalCredit()
        //현재 이수한 패논패 과목 학점
        var nowCredit_P = App.prefs.getTotalCreditPF()
        //현재 평점
        val nowGPA = App.prefs.getTotalGPA()
        //재수강 후 평점
        val retakeGPA = App.prefs.getRetakeGPA()

        tv_graduate_credit.text = (graduateCredit.toString() + " 학점")
        tv_graduate_major_credit.text = (graduateMajorCredit.toString() + " 학점")
        tv_goalGPA.text = goalGPA.toString()
        tv_rest_credit.text = ((graduateCredit - nowCredit).toString() + " 학점")
        tv_nowGPA.text = nowGPA.toString()
        tv_retakeGPA.text= retakeGPA.toString()
    }

    fun initBtn(){

        editBtn.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.edit_dialog,null)
            val et_credit = dialogView.findViewById<EditText>(R.id.et_credit)
            et_credit.hint = App.prefs.getGraduate().toString()
            val et_credit_major = dialogView.findViewById<EditText>(R.id.et_credit_major)
            et_credit_major.hint = App.prefs.getMajorGraduate().toString()
            val et_gpa = dialogView.findViewById<EditText>(R.id.et_gpa)
            et_gpa.hint = App.prefs.getUserGoal().toString()

            val builder = AlertDialog.Builder(requireContext())
            builder.setView(dialogView)
            builder.setTitle("졸업 요건 변경하기")
                .setPositiveButton("수정",null)
                .setNegativeButton("취소"){
                        dialogInterface, i -> dialogInterface.cancel()
                }
            val dialog = builder.create()

            dialog.setOnShowListener{
                val positiveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveBtn.setOnClickListener{

                    var graduate:Int = 0; var majorGraduate:Int = 0; var userGoal:Float = 0.toFloat()
                    var flag1 = false; var flag2 = false; var flag3 = false

                    if(et_credit.text.toString() != ""){
                        if(et_credit.text.toString().toIntOrNull() != null){
                            when(et_credit.text.toString().toInt()){
                                in 0..200 -> {
                                    graduate = et_credit.text.toString().toInt()
                                    flag1 = true
                                }
                            }
                        }
                    }
                    else{
                        graduate = App.prefs.getGraduate()
                        flag1 = true
                    }


                    if(et_credit_major.text.toString() != ""){
                        if(et_credit_major.text.toString().toIntOrNull() != null){
                            when(et_credit_major.text.toString().toInt()){
                                in 0..200 -> {
                                    majorGraduate = et_credit_major.text.toString().toInt()
                                    flag2 = true
                                }
                            }
                        }
                    }
                    else{
                        majorGraduate = App.prefs.getMajorGraduate()
                        flag2 = true
                    }


                    if(et_gpa.text.toString() != ""){
                        if(et_gpa.text.toString().toFloatOrNull() != null){
                            when(et_gpa.text.toString().toFloat()){
                                in 0.0..4.5 -> {
                                    userGoal = et_gpa.text.toString().toFloat()
                                    flag3 = true
                                }
                            }
                        }
                    }
                    else {
                        userGoal = App.prefs.getUserGoal()
                        flag3 = true
                    }

                    if(flag1 && flag2 && flag3){
                        App.prefs.setUserInfo(graduate,majorGraduate,userGoal)
                        Toast.makeText(requireContext(),"졸업 요건이 변경 되었습니다.",Toast.LENGTH_SHORT).show()

                        val ft = fragmentManager!!.beginTransaction()
                        ft.detach(this@SettingFragment).attach(this@SettingFragment).commit()

                        dialog.dismiss()
                    }else if(!flag1 || !flag2 ){
                        Toast.makeText(requireContext(),"0 ~ 200사이 숫자만 입력해 주세요", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireContext(),"0.0 ~ 4.5사이 숫자만 입력해 주세요", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            dialog.show()
        }

    }

}