package com.example.bookreview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookreview.databinding.ItemBookBinding
import com.example.bookreview.model.Book

class BookAdapter(private val itemClickedListener: (Book) -> Unit): ListAdapter<Book, BookAdapter.BookItemViewHolder>(diffUtil) {
    inner class BookItemViewHolder(private val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(bookModel : Book) {
            binding.titleTextView.text = bookModel.title
            binding.descriptionTextView.text = bookModel.description
            //View에 데이터 연결을 위해 바인딩 접근

            binding.root.setOnClickListener {
                itemClickedListener(bookModel)
            } //root를 클릭하면 itemClickedListener 함수 호출

            Glide
                .with(binding.coverImg.context) //가져와서 반영할 context
                .load(bookModel.coverSmallUrl) //가져오는 데이터의 Url 값
                .into(binding.coverImg) //최종적으로 반영할 위치
        }
    }//Glide : 이미지 데이터를 가져오는 라이브러리

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        return BookItemViewHolder(ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        holder.bind(currentList[position])
        //현재 리스트의 위치에서 서적 데이터를 가져오기
    }

    companion object {
        //같은 데이터가 이미 있는지 없는지를 확인하고 추가 여부를 판단
        val diffUtil = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}