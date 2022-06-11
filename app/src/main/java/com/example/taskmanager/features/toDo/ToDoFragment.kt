package com.example.taskmanager.features.toDo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taskmanager.R
import com.example.taskmanager.databinding.FragmentToDoBinding

class ToDoFragment : Fragment() {

    lateinit var binding: FragmentToDoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToDoBinding.inflate(layoutInflater , container , false)



        return binding.root
    }

}