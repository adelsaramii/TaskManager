package com.example.taskmanager.features.done

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanager.R
import com.example.taskmanager.databinding.FragmentDoneBinding
import com.example.taskmanager.features.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DoneFragment : Fragment() {

    lateinit var binding: FragmentDoneBinding
    private val mainViewModel by sharedViewModel<MainViewModel>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDoneBinding.inflate(layoutInflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

}