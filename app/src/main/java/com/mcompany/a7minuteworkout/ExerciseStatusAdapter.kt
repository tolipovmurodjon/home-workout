package com.mcompany.a7minuteworkout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mcompany.a7minuteworkout.databinding.ExerciseItemStatusBinding

class ExerciseStatusAdapter(val items: ArrayList<ExerciseModel>):
    RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {


        inner class ViewHolder(val itemStatusBinding: ExerciseItemStatusBinding):
            RecyclerView.ViewHolder(itemStatusBinding.root){

                val exerciseNumber = itemStatusBinding.exerciseNumber
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ExerciseItemStatusBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model : ExerciseModel = items[position]
        holder.exerciseNumber.text = model.getId().toString()


        when  {

            model.isSelected() -> {
                holder.exerciseNumber.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.exercise_selected)
                holder.exerciseNumber.setTextColor(Color.parseColor("#FF000000"))

            }
            model.isCompleted() -> {
                holder.exerciseNumber.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.exercise_completed)
                holder.exerciseNumber.setTextColor(Color.parseColor("#FFFFFFFF"))

            }

            else -> {
                holder.exerciseNumber.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.exercise_status)
                holder.exerciseNumber.setTextColor(Color.parseColor("#FFFFFFFF"))

            }


        }

    }


}