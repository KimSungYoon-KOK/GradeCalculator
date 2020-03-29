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
                var str = editText.text.toString()
                for(i in editText.text.indices){
                    //editText의 text가 하나라도 숫자가 아니라면
                    if(str[i] !in '0'..'9'){
                        flag1 = false
                        break
                    }
                    flag1 = true
                }

                //전공 기준 학점
                if(flag1){
                    str = editText2.text.toString()
                    for(i in editText2.text.indices){
                        //editText의 text가 하나라도 숫자가 아니라면
                        if(str[i] !in '0'..'9'){
                            flag2 = false
                            break
                        }
                        flag2 = true
                    }
                }

                //목표 학점
                if(flag1 && flag2){
                    str = editText3.text.toString()
                    var floatFlag = false
                    for(i in editText3.text.indices){
                        //editText의 text가 하나라도 숫자가 아니라면
                        if(str[i] in '0'..'9'){
                            flag3 = true
                        }else if(str[i] == '.'){
                            floatFlag = true
                        }else{
                            flag3 = false
                            break
                        }
                    }

                    if(floatFlag) {
                        val goal = str.toFloat()
                        if (goal !in 0.0..4.5) {
                            Toast.makeText(this, "0.0 ~ 4.5 사이의 숫자만 입력해주세요", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            //startActivity 안뜨게 sharedpreference 조정하기
                            MySharedPreferences(this).setUserInfo(editText.text.toString().toInt(), editText2.text.toString().toInt(), editText3.text.toString().toFloat())
                            //intent 넘기기
                            val Intent = Intent(this, MainActivity::class.java)
                            startActivity(Intent)
                            finish()
                        }
                    }
                }

                Toast.makeText(this, "숫자만 입력해주세요",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "빠짐 없이 입력해주세요",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
