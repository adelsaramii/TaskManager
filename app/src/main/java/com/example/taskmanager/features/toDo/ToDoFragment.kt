package com.example.taskmanager.features.toDo

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.setPadding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.databinding.FragmentToDoBinding
import com.example.taskmanager.databinding.ItemAddBinding
import com.example.taskmanager.di.AppModule
import com.example.taskmanager.features.MainViewModel
import com.example.taskmanager.model.local.TaskModel
import com.example.taskmanager.utils.UploadUtility
import com.noowenz.customdatetimepicker.CustomDateTimePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*
import kotlin.collections.ArrayList

class ToDoFragment : Fragment(), ToDoAdapter.ToDoEvent {

    lateinit var binding: FragmentToDoBinding
    private lateinit var dialogBinding: ItemAddBinding
    private val mainViewModel by sharedViewModel<MainViewModel>()
    private lateinit var adapter: ToDoAdapter
    private val imageLoader: AppModule.ImageLoaderService by inject()
    private var newUrl: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToDoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        getLiveTasks()

    }

    //UI
    private fun initRecycler() {
        val data = arrayListOf<TaskModel>()
        adapter = ToDoAdapter(data, this, imageLoader)
        binding.recyclerToDo.adapter = adapter
        binding.recyclerToDo.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
    }
    private fun getLiveTasks() {
        mainViewModel.getAllTasks().observe(viewLifecycleOwner) {
            val data: ArrayList<TaskModel> = it as ArrayList<TaskModel>
            val dataHelper: ArrayList<TaskModel> = data.clone() as ArrayList<TaskModel>
            dataHelper.clear()
            data.forEach {
                if (it.state.equals("todo")) {
                    dataHelper.add(it)
                }
            }
            adapter.refreshData(dataHelper)
        }
    }

    //ITEMS EVENT
    override fun onDoneClick(task: TaskModel) {
        lifecycleScope.launchWhenCreated {
            mainViewModel.updateTask(task)
        }
    }
    override fun onClick(task: TaskModel) {
        dialog(task)
    }
    override fun onLongClick(task: TaskModel) {
        lifecycleScope.launchWhenCreated {
            mainViewModel.deleteTask(task)
        }
    }

    //UPDATE
    private fun dialog(task: TaskModel) {

        val dialog = AlertDialog.Builder(this.requireActivity()).create()
        dialogBinding = ItemAddBinding.inflate(layoutInflater)

        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        dialogBinding.add.text = "update"
        dialogBinding.time.text = "change time"
        dialogBinding.title.setText(task.title)
        dialogBinding.description.setText(task.description)
        dialogBinding.image.setPadding(0)
        imageLoader.loadImage(
            "http://private-app-key.ravanfix.com/app/apphelper/uploads/" + task.url,
            dialogBinding.image
        )

        dialogBinding.image.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, 1)
            }
        }

        dialogBinding.time.setOnClickListener {
            CustomDateTimePicker(
                this.requireActivity(),
                object : CustomDateTimePicker.ICustomDateTimeListener {
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
                        task.date = "$year/$monthNumber/$day"
                        task.time = "$hour24:$min"
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

            if (!newUrl.equals("")) {
                task.url = newUrl
            }

            if (dialogBinding.title.text.toString().equals("")) {
                dialogBinding.title.error = "enter title"
            } else if (dialogBinding.description.text.toString().equals("")) {
                dialogBinding.description.error = "enter description"
            } else if (task.date.equals("")) {
                Toast.makeText(this.context, "set a time", Toast.LENGTH_SHORT).show()
            } else if (task.time.equals("")) {
                Toast.makeText(this.context, "set a time", Toast.LENGTH_SHORT).show()
            } else if (task.url.equals("")) {
                Toast.makeText(this.context, "set a image", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("mdcmdc", dialogBinding.title.text.toString())
                lifecycleScope.launchWhenCreated {
                    task.title = dialogBinding.title.text.toString()
                    task.description = dialogBinding.description.text.toString()
                    mainViewModel.updateTask(task)
                    dialog.dismiss()
                }
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            lifecycleScope.launchWhenCreated {
                withContext(Dispatchers.IO) {
                    val uploadUtility = UploadUtility(requireActivity())
                    newUrl = uploadUtility.uploadFile(data?.data!!)
                }
                if (newUrl.equals("")) {
                    Toast.makeText(context, "UPLOAD FAILED! try again", Toast.LENGTH_SHORT).show()
                } else {
                    dialogBinding.image.setPadding(0)
                    dialogBinding.image.setImageURI(data?.data!!)
                }
            }
        }
    }

}