package com.example.grade_calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

        setProgressBar()
    }


    fun setProgressBar() {
        val animationDuration = 2500 // 2500ms = 2,5s
        progressBar.setProgressWithAnimation(
            //퍼센테이지
            (nowGPA / goalGPA * 100).toFloat(),
            animationDuration
        ) // Default duration = 1500ms

    }
}