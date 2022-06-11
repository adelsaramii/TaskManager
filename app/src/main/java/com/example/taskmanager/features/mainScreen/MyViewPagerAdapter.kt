package com.example.taskmanager.features.mainScreen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.taskmanager.features.done.DoneFragment
import com.example.taskmanager.features.toDo.ToDoFragment

class MyViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {

        return when (position) {

            0 -> {
                ToDoFragment()
            }

            1 -> {
                DoneFragment()
            }

            else -> {
                Fragment()
            }

        }
    }

    override fun getItemCount(): Int {
        return 2
    }


}