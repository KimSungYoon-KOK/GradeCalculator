package com.kok.grade_calculator

import com.kok.grade_calculator.mypage.MyPageItem
import kotlin.math.round

class Calculator {

    //학기별 이수 학점 계산
    fun semesterCalculate(itemList : ArrayList<ArrayList<MyPageItem>>, position : Int)
    : String{

        var semesterCredit = 0              //학기별 수강 학점
        var semesterCreditPF = 0            //학기별 패논패 과목 제외 후 수강 학점
        var semesterGrade = 0.0             //학기별 학점

        for(i in 0 until itemList[position].size){
            semesterCredit += itemList[position][i].credit
            if(itemList[position][i].grade == 10.toFloat()){
                continue
            }
            semesterCreditPF += itemList[position][i].credit
            semesterGrade += itemList[position][i].grade*itemList[position][i].credit
        }

        if(semesterCreditPF == 0){
            semesterGrade = 0.0
        }else{
            semesterGrade /= semesterCreditPF
            semesterGrade *= 100
            semesterGrade = round(semesterGrade)/100
        }

        return "이수 학점 : $semesterCredit     성적 : $semesterGrade"
    }

    fun retakeCalculate(itemList : ArrayList<ArrayList<MyPageItem>>, position : Int)
    :String {

        var semesterCredit = 0              //학기별 수강 학점
        var semesterCreditPF = 0            //학기별 패논패 과목 제외 후 수강 학점
        var semesterGrade = 0.0             //학기별 학점

        for(i in 0 until itemList[position].size){
            semesterCredit += itemList[position][i].credit
            if(itemList[position][i].retakeGrade == 10.toFloat()){
                continue
            }
            semesterCreditPF += itemList[position][i].credit
            semesterGrade += itemList[position][i].retakeGrade*itemList[position][i].credit
        }

        if(semesterCreditPF == 0){
            semesterGrade = 0.0
        }else{
            semesterGrade /= semesterCreditPF
            semesterGrade *= 100
            semesterGrade = round(semesterGrade)/100
        }

        return semesterGrade.toString()
    }

    fun totalCalculate(itemList : ArrayList<ArrayList<MyPageItem>>):Float{

        var credit = 0
        var creditPF = 0
        var grade = 0.toFloat()

        for(i in 0 until  itemList.size){
            for(j in 0 until itemList[i].size){
                credit += itemList[i][j].credit
                if(itemList[i][j].grade == 10.toFloat()){
                    continue
                }
                creditPF += itemList[i][j].credit
                grade += itemList[i][j].grade*itemList[i][j].credit
            }
        }

        if(creditPF != 0){
            grade /= creditPF
            grade *= 100
            grade = round(grade)/100
        }

        return grade
    }

    //재수강 후 학점 계산
    fun retakeGPA(itemList : ArrayList<ArrayList<MyPageItem>>):Float {

        var credit = 0          //패논패 과목 포함 학점
        var creditPF = 0        //패논패 과목 제외 후 학점
        var grade = 0.toFloat()

        for(i in 0 until  itemList.size){
            for(j in 0 until itemList[i].size){
                credit += itemList[i][j].credit
                if(itemList[i][j].grade == 10.toFloat()){
                    continue
                }
                creditPF += itemList[i][j].credit
                grade += itemList[i][j].retakeGrade*itemList[i][j].credit
            }
        }

        if(creditPF != 0){
            grade /= creditPF
            grade *= 100
            grade = round(grade)/100
        }

        return grade
    }

    fun needGpaCalculate(graduateCredit: Int, nowCredit: Int, nowCredit_P: Int, goalGPA: Float, nowGPA: Float):Float{

        val goal = (graduateCredit - nowCredit_P) * goalGPA
        val now = (nowCredit - nowCredit_P) * nowGPA
        val rest = graduateCredit - nowCredit

        return (goal - now)/rest
    }


}
