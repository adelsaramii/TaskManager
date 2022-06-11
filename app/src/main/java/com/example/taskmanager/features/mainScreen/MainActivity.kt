package com.example.taskmanager.features.mainScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.taskmanager.databinding.ActivityMainBinding
import com.example.taskmanager.databinding.ItemAddBinding
import com.example.taskmanager.features.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var dialogBinding: ItemAddBinding
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setViewPager()

        binding.deleteAll.setOnClickListener {
            lifecycleScope.launchWhenCreated{
                mainViewModel.deleteAllTasks()
                setViewPager()
            }
        }

        binding.add.setOnClickListener {

        }

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


    private fun dialog(){

        val dialog = AlertDialog.Builder(this).create()
        dialogBinding = ItemAddBinding.inflate(layoutInflater)

        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()

    }

}