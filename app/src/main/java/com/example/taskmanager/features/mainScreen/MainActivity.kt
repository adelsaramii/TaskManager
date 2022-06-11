package com.example.taskmanager.features.mainScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.taskmanager.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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