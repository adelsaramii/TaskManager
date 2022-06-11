package com.example.taskmanager.features.done

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.taskmanager.R
import com.example.taskmanager.databinding.FragmentDoneBinding


class DoneFragment : Fragment() {

    lateinit var binding: FragmentDoneBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDoneBinding.inflate(layoutInflater , container , false)



        return binding.root
    }

}