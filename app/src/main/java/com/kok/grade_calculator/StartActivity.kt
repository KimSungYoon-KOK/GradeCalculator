package com.kok.grade_calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        //시작하기 버튼 눌렀을때 클릭 리스너
        startBtn.setOnClickListener {
            val flag1 = editText1.text.isNotEmpty()     //졸업 이수 학점 EditText
            val flag2 = editText2.text.isNotEmpty()     //전공 이수 학점 EditText
            val flag3 = editText3.text.isNotEmpty()     //목표 학점 EditText

            //양식 작성 검사
            if(flag1 && flag2 && flag3) {
                //졸업 기준 학점
                val graduate = editText1.text.toString().toIntOrNull()
                if((graduate == null) || (graduate !in 1..250)) {
                    Toast.makeText(this, "1~250 사이의 숫자만 입력해주세요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                //전공 기준 학점
                val majorGraduate = editText2.text.toString().toIntOrNull()
                if((majorGraduate == null) || (majorGraduate !in 0..250)) {
                    Toast.makeText(this, "0~250 사이의 숫자만 입력해주세요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else if (majorGraduate > graduate) {
                    Toast.makeText(this, "전공 학점이 졸업 학점보다 높을 수 없습니다", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                //목표 학점
                val goalGPA = editText3.text.toString().toFloatOrNull()
                if((goalGPA == null) || (goalGPA !in 0.0..4.5)){
                    Toast.makeText(this, "0~4.5 사이의 숫자만 입력해주세요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                App.prefs.setUserInfo(graduate, majorGraduate, goalGPA)
                Toast.makeText(this,"졸업 이수 조건이 저장 되었습니다",Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this, "빠짐 없이 입력해주세요",Toast.LENGTH_SHORT).show()
            }

        }
    }
}
