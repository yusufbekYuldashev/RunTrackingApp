package com.yusufbek.runtrackingapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yusufbek.runtrackingapp.BaseApplication
import com.yusufbek.runtrackingapp.R
import com.yusufbek.runtrackingapp.databinding.ItemRunBinding
import com.yusufbek.runtrackingapp.db.RunEntity
import com.yusufbek.runtrackingapp.other.TrackingUtilities
import java.text.SimpleDateFormat
import java.util.*

class RunRecyclerViewAdapter(var ctx:Context) : RecyclerView.Adapter<RunRecyclerViewAdapter.RunViewHolder>() {

    private lateinit var binding: ItemRunBinding

    private val diffCallback = object : DiffUtil.ItemCallback<RunEntity>() {
        override fun areItemsTheSame(oldItem: RunEntity, newItem: RunEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RunEntity, newItem: RunEntity): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<RunEntity>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        binding = ItemRunBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RunViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val run = differ.currentList[position]
        holder.bind(run)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class RunViewHolder(binding: ItemRunBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(run:RunEntity){
            Glide.with(ctx).load(run.img).into(binding.ivRunImage)

            val calendar = Calendar.getInstance().apply {
                timeInMillis = run.timeStamp
            }
            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            binding.tvDate.text = dateFormat.format(calendar.time)

            "${run.avgSpeed}km/h".also { binding.tvAvgSpeed.text = it }

            "${run.distanceInMeters / 1000f}km".also { binding.tvDistance.text = it }

            binding.tvTime.text = TrackingUtilities.getFormattedStopWatchTime(run.timeInMillis)

            "${run.caloriesBurned}ccal".also { binding.tvCalories.text = it }
        }
    }

}