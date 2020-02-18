package com.example.grade_calculator

import com.example.grade_calculator.MyPage.MyPage_item
import kotlin.math.round

class Calculator {

    //학기별 이수 학점 계산
    fun semesterCalculate(itemlist : ArrayList<ArrayList<MyPage_item>>, position : Int)
    : String{

        var semesterCredit = 0              //학기별 수강 학점
        var semesterCredit_P = 0            //학기별 패논패 과목 제외 후 수강 학점
        var semesterGrade = 0.0             //학기별 학점

        for(i in 0 until itemlist[position].size){
            semesterCredit += itemlist[position][i].credit
            if(itemlist[position][i].grade == 10.toFloat()){
                continue
            }
            semesterCredit_P += itemlist[position][i].credit
            semesterGrade += itemlist[position][i].grade*itemlist[position][i].credit
        }

        if(semesterCredit_P == 0){
            semesterGrade = 0.0
        }else{
            semesterGrade /= semesterCredit_P
            semesterGrade *= 100
            semesterGrade = round(semesterGrade)/100
        }

        return "이수 학점 : $semesterCredit     성적 : $semesterGrade"
    }

    fun retakeCalculate(itemlist : ArrayList<ArrayList<MyPage_item>>, position : Int)
    :String {

        var semesterCredit = 0              //학기별 수강 학점
        var semesterCredit_P = 0            //학기별 패논패 과목 제외 후 수강 학점
        var semesterGrade = 0.0             //학기별 학점

        for(i in 0 until itemlist[position].size){
            semesterCredit += itemlist[position][i].credit
            if(itemlist[position][i].grade == 10.toFloat()){
                continue
            }
            semesterCredit_P += itemlist[position][i].credit
            semesterGrade += itemlist[position][i].retakeGrade*itemlist[position][i].credit
        }

        if(semesterCredit_P == 0){
            semesterGrade = 0.0
        }else{
            semesterGrade /= semesterCredit_P
            semesterGrade *= 100
            semesterGrade = round(semesterGrade)/100
        }

        return semesterGrade.toString()
    }

}
