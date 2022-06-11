package com.example.taskmanager.features.toDo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.databinding.FragmentToDoBinding
import com.example.taskmanager.di.MyApp
import com.example.taskmanager.features.MainViewModel
import com.example.taskmanager.model.local.TaskModel
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ToDoFragment : Fragment() , ToDoAdapter.ToDoEvent{

    lateinit var binding: FragmentToDoBinding
    private val mainViewModel by sharedViewModel<MainViewModel>()
    private lateinit var adapter: ToDoAdapter
    private val picasso : MyApp.PicassoLoader by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentToDoBinding.inflate(layoutInflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = mainViewModel.getAllTasks()

        adapter = ToDoAdapter(data as ArrayList<TaskModel>, this , picasso)
        binding.recyclerToDo.adapter = adapter
        binding.recyclerToDo.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
    }

    override fun onDoneClick(task: TaskModel) {
        TODO("Not yet implemented")
    }

    override fun onLongClick(task: TaskModel) {
        TODO("Not yet implemented")
    }

}