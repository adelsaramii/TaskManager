package com.example.taskmanager.features.mainScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanager.databinding.ActivityMainBinding
import com.example.taskmanager.features.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setViewPager()



    }

    private fun setViewPager(){

        val myAdapter = MyViewPagerAdapter(this)
        binding.viewPagerMain.adapter = myAdapter
        binding.viewPagerMain.offscreenPageLimit = 3

        val mediator = TabLayoutMediator(
            binding.tabLayoutMain ,
            binding.viewPagerMain
        ) { tab, position ->
            when (position) {

                0 -> {
                    tab.text = "TODO"
                }

                1 -> {
                    tab.text = "DONE"
                }

            }
        }

        mediator.attach()

    }

}