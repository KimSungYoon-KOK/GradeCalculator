package com.example.grade_calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_firststart.*

class FirststartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firststart)

        //시작하기 버튼 눌렀을때 클릭 리스너
        startBtn.setOnClickListener {

            var flag1 = false
            var flag2 = false
            var flag3 = false

            //비어 있는 칸이 있는지 확인
            if(editText.text.isNotEmpty() && editText2.text.isNotEmpty() && editText3.text.isNotEmpty()){
                //졸업 기준 학점
                val graduate = editText.text.toString()
                if(graduate.toIntOrNull() != null){
                    when(graduate.toInt()){
                        in 0..200 -> flag1 = true
                    }
                }

                //전공 기준 학점
                val majorGraduate = editText2.text.toString()
                if(majorGraduate.toIntOrNull() != null){
                    when(majorGraduate.toInt()){
                        in 0..200 -> flag2 = true
                    }
                }

                //목표 학점
                val goal = editText3.text.toString()
                if(goal.toFloatOrNull() != null){
                    when(goal.toFloat()){
                        in 0.0..4.5 -> flag3 = true
                    }
                }

                if(flag1 && flag2 && flag3){
                    App.prefs.setUserInfo(graduate.toInt(),majorGraduate.toInt(),goal.toFloat())
                    Toast.makeText(this,"졸업 이수 조건이 저장 되었습니다.",Toast.LENGTH_SHORT).show()

                    //intent 넘기기
                    val Intent = Intent(this, MainActivity::class.java)
                    startActivity(Intent)
                    finish()

                }else if(!flag1 || !flag2 ){
                    Toast.makeText(this,"0 ~ 200사이 숫자만 입력해 주세요", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,"0.0 ~ 4.5사이 숫자만 입력해 주세요", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "빠짐 없이 입력해주세요",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
