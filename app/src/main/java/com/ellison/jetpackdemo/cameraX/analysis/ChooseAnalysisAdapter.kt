package com.ellison.jetpackdemo.cameraX.analysis

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ellison.jetpackdemo.databinding.AnalysisListItemBinding

class ChooseAnalysisAdapter(
    private val data: List<AnalysisType>,
    private val listener: AnalysisChooseListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface AnalysisChooseListener { fun onAnalysisChoose(type: AnalysisType) }

    private class MyViewHolder(val binding: AnalysisListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        AnalysisListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).let {
            MyViewHolder(it)
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myViewHolder = holder as MyViewHolder
        myViewHolder.binding.text1.text = data[position].name
        myViewHolder.binding.logo.setImageResource(data[position].logo)

        holder.itemView.tag = position
        holder.itemView.setOnClickListener { listener.onAnalysisChoose(data[it.tag as Int]) }
    }
}