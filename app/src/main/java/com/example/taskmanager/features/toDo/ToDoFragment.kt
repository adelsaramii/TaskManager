package com.example.taskmanager.features.toDo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.databinding.FragmentToDoBinding
import com.example.taskmanager.di.MyApp
import com.example.taskmanager.features.MainViewModel
import com.example.taskmanager.model.local.TaskModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ToDoFragment : Fragment(), ToDoAdapter.ToDoEvent {

    lateinit var binding: FragmentToDoBinding
    private val mainViewModel by sharedViewModel<MainViewModel>()
    private lateinit var adapter: ToDoAdapter
    private val picasso: MyApp.ImageLoaderService by inject()

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

    private fun initRecycler() {
        val data = arrayListOf<TaskModel>()
        adapter = ToDoAdapter(data, this, picasso)
        binding.recyclerToDo.adapter = adapter
        binding.recyclerToDo.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
    }

    override fun onDoneClick(task: TaskModel, position: Int) {
        lifecycleScope.launchWhenCreated {
            mainViewModel.updateTask(task)
        }
    }

    override fun onClick(task: TaskModel, position: Int) {
        lifecycleScope.launchWhenCreated {
            mainViewModel.updateTask(task)
        }
    }

    override fun onLongClick(task: TaskModel) {
        lifecycleScope.launchWhenCreated {
            mainViewModel.deleteTask(task)
        }
    }

}