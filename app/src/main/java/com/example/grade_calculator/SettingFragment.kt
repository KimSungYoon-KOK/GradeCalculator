package com.example.grade_calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment

class SettingFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initBtn()

    }

    fun initBtn(){
        val credit_items = resources.getStringArray(R.array.credit)
        val grade_items = resources.getStringArray(R.array.grade)
        val category_items = resources.getStringArray(R.array.category)

        val spinnerAdapter_credit = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, credit_items)
        val spinnerAdapter_grade = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, grade_items)
        val spinnerAdapter_category = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, category_items)




    }

}