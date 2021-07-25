package com.example.bookreview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookreview.databinding.ItemHistoryBinding
import com.example.bookreview.model.History

class HistoryAdapter(val historyDeleteClickedListener: (String) -> Unit): ListAdapter<History, HistoryAdapter.HistoryItemViewHolder>(diffUtil) {
    inner class HistoryItemViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(historyModel : History) {
            binding.historyKeywordTextView.text = historyModel.keyword

            binding.keywordDeleteBtn.setOnClickListener {
                historyDeleteClickedListener(historyModel.keyword.orEmpty())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        return HistoryItemViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.bind(currentList[position])
        //현재 리스트의 위치에서 서적 데이터를 가져오기
    }

    companion object {
        //같은 데이터가 이미 있는지 없는지를 확인하고 추가 여부를 판단
        val diffUtil = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.keyword == newItem.keyword
            }

        }
    }
}