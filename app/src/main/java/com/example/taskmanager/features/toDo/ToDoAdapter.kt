package com.example.taskmanager.features.toDo

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.di.MyApp
import com.example.taskmanager.model.local.TaskModel
import com.squareup.picasso.Picasso

class ToDoAdapter(private val data : ArrayList<TaskModel> , private val toDoEvent: ToDoEvent , private val picasso: MyApp.PicassoLoader) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    inner class ToDoViewHolder(itemView: View, private val context: Context) :
        RecyclerView.ViewHolder(itemView) {

        private val image = itemView.findViewById<ImageView>(R.id.image)
        private val title = itemView.findViewById<TextView>(R.id.title)
        private val description = itemView.findViewById<TextView>(R.id.description)
        private val date = itemView.findViewById<TextView>(R.id.date)
        private val time = itemView.findViewById<TextView>(R.id.time)
        private val done = itemView.findViewById<Button>(R.id.done)

        @SuppressLint("SetTextI18n")
        fun bindData(position: Int) {

            title.text = data[position].title
            description.text = data[position].description
            date.text = data[position].date
            time.text = data[position].time

            picasso.loadImage(data[position].url  , image)

            done.setOnClickListener {
                toDoEvent.onDoneClick(data[position])
            }

            itemView.setOnLongClickListener {
                toDoEvent.onLongClick(data[position])
                true
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return ToDoViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun addTask(newTask :TaskModel) {

        data.add( 0 , newTask )
        notifyItemInserted(0)

    }

    fun removeTask(oldTask :TaskModel , oldPosition :Int) {

        data.remove(oldTask)
        notifyItemRemoved(oldPosition)

    }

    fun updateTask(newTask :TaskModel , position :Int) {

        data[position] = newTask
        notifyItemChanged( position )

    }

    fun setTask(newListTask :ArrayList<TaskModel>) {

        data.clear()
        data.addAll( newListTask )
        notifyDataSetChanged()

    }

    interface ToDoEvent{
        fun onDoneClick(task: TaskModel)
        fun onLongClick(task: TaskModel)
    }

}


