package com.mcompany.a7minuteworkout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mcompany.a7minuteworkout.databinding.ItemHistoryRowBinding

class HistoryAdapter(private var items : ArrayList<String>): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {


    class ViewHolder(binding: ItemHistoryRowBinding) : RecyclerView.ViewHolder(binding.root){
        val layout  = binding.llHistoryItemMain
        val number  = binding.tvPosition
        val date  = binding.tvItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ItemHistoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val date: String = items.get(position)

        holder.number.text = (position + 1).toString()
        holder.date.text = date

        if (position % 2 == 0) {
            holder.layout.setBackgroundColor(
                Color.parseColor("#EBEBEB")
            )
        } else {
            holder.layout.setBackgroundColor(
                Color.parseColor("#FFFFFF")
            )
        }




    }

}