package com.example.taskmanager.features.mainScreen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.lifecycle.lifecycleScope
import com.example.taskmanager.databinding.ActivityMainBinding
import com.example.taskmanager.databinding.ItemAddBinding
import com.example.taskmanager.features.MainViewModel
import com.example.taskmanager.model.local.TaskModel
import com.example.taskmanager.utils.UploadUtility
import com.google.android.material.tabs.TabLayoutMediator
import com.noowenz.customdatetimepicker.CustomDateTimePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var dialogBinding: ItemAddBinding
    private val mainViewModel by viewModel<MainViewModel>()
    var newTask: TaskModel = TaskModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setViewPager()
        onClick()
    }

    //UI
    private fun onClick(){
        binding.deleteAll.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                mainViewModel.deleteAllTasks()
                setViewPager()
            }
        }
        binding.add.setOnClickListener {
            dialog()
        }
    }
    private fun setViewPager() {

        val myAdapter = MyViewPagerAdapter(this)
        binding.viewPagerMain.adapter = myAdapter
        binding.viewPagerMain.offscreenPageLimit = 3

        val mediator = TabLayoutMediator(
            binding.tabLayoutMain,
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

    //ADD
    private fun dialog() {

        val dialog = AlertDialog.Builder(this).create()
        dialogBinding = ItemAddBinding.inflate(layoutInflater)

        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        dialogBinding.image.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                    if (askForPermissions()){
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        startActivityForResult(intent, 1)
                    }
            }
        }

        dialogBinding.time.setOnClickListener {
            CustomDateTimePicker(this, object : CustomDateTimePicker.ICustomDateTimeListener {
                @SuppressLint("BinaryOperationInTimber", "SetTextI18n")
                override fun onSet(
                    dialog: Dialog,
                    calendarSelected: Calendar,
                    dateSelected: Date,
                    year: Int,
                    monthFullName: String,
                    monthShortName: String,
                    monthNumber: Int,
                    day: Int,
                    weekDayFullName: String,
                    weekDayShortName: String,
                    hour24: Int,
                    hour12: Int,
                    min: Int,
                    sec: Int,
                    AM_PM: String
                ) {
                    dialogBinding.time.visibility = View.INVISIBLE
                    dialogBinding.textTime.visibility = View.VISIBLE
                    dialogBinding.textTime.text = "$year/$monthNumber/$day   $hour24:$min"
                    newTask.date = "$year/$monthNumber/$day"
                    newTask.time = "$hour24:$min"
                }

                override fun onCancel() {

                }

            }).apply {
                set24HourFormat(true)
                setMaxMinDisplayDate(
                    maxDate = Calendar.getInstance().apply { add(Calendar.YEAR, 1) }.timeInMillis
                )
                setDate(Calendar.getInstance())
                showDialog()
            }

        }

        dialogBinding.add.setOnClickListener {
            if (dialogBinding.title.text.toString().equals("")) {
                dialogBinding.title.error = "enter title"
            }else if(dialogBinding.description.text.toString().equals("")) {
                dialogBinding.description.error = "enter description"
            }else if(newTask.date.equals("")) {
                Toast.makeText(this , "set a time" , Toast.LENGTH_SHORT).show()
            }else if(newTask.time.equals("")) {
                Toast.makeText(this , "set a time" , Toast.LENGTH_SHORT).show()
            }else if (newTask.url.equals("")){
                Toast.makeText(this , "set a image" , Toast.LENGTH_SHORT).show()
            }else{
                lifecycleScope.launchWhenCreated {
                    newTask.title = dialogBinding.title.text.toString()
                    newTask.description = dialogBinding.description.text.toString()
                    mainViewModel.insertTask(newTask)
                    dialog.dismiss()
                }
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1){
            lifecycleScope.launchWhenCreated {
                withContext(Dispatchers.IO){
                    val uploadUtility :UploadUtility = UploadUtility(this@MainActivity)
                    newTask.url = uploadUtility.uploadFile(data?.data!!)
                }
                if (newTask.url.equals("")){
                    Toast.makeText(this@MainActivity , "UPLOAD FAILED! try again" , Toast.LENGTH_SHORT).show()
                }else{
                    dialogBinding.image.setPadding(0)
                    dialogBinding.image.setImageURI(data?.data!!)
                }
            }
        }
    }

    //GALLERY PERMISSIONS
    private fun isPermissionsAllowed(): Boolean {
        return if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            false
        } else true
    }
    private fun askForPermissions(): Boolean {
        if (!isPermissionsAllowed()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this as Activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(this as Activity,arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),2)
            }
            return false
        }
        return true
    }
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            2 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, 1)
                } else {
                    // permission is denied, you can ask for permission again, if you want
                    //  askForPermissions()
                }
                return
            }
        }
    }
    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton("App Settings",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    // send to app settings if permission is denied permanently
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", getPackageName(), null)
                    intent.data = uri
                    startActivity(intent)
                })
            .setNegativeButton("Cancel",null)
            .show()
    }

}