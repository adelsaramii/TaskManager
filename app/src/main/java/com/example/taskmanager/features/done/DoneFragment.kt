package com.example.taskmanager.features.done

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.databinding.FragmentDoneBinding
import com.example.taskmanager.di.MyApp
import com.example.taskmanager.features.MainViewModel
import com.example.taskmanager.features.toDo.ToDoAdapter
import com.example.taskmanager.model.local.TaskModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DoneFragment : Fragment(), DoneAdapter.DoneEvent {

    lateinit var binding: FragmentDoneBinding
    private val mainViewModel by sharedViewModel<MainViewModel>()
    private lateinit var adapter: DoneAdapter
    private val picasso: MyApp.ImageLoaderService by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDoneBinding.inflate(layoutInflater, container, false)
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
                if (it.state.equals("done")) {
                    dataHelper.add(it)
                }
            }
            adapter.refreshData(dataHelper)
        }
    }

    private fun initRecycler() {
        val data = arrayListOf<TaskModel>()
        adapter = DoneAdapter(data, this, picasso)
        binding.recyclerDone.adapter = adapter
        binding.recyclerDone.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
    }

    override fun onLongClick(task: TaskModel) {
        lifecycleScope.launchWhenCreated {
            mainViewModel.deleteTask(task)
        }
    }

}