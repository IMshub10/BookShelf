package com.summer.bookshelf.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.summer.bookshelf.R
import com.summer.bookshelf.databinding.ItemBookBinding
import com.summer.bookshelf.ui.models.BookModel

class BookAdapter(private val selectionCallback: SelectionCallback) :
    ListAdapter<BookModel, BookAdapter.ContentHolder>(Callback()) {

    inner class ContentHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: BookModel) {
            binding.run {
                this.model = model
                ivItemBookBookmark.setOnClickListener {
                    selectionCallback.onBookmark(model)
                }
            }
        }
    }

    internal class Callback : DiffUtil.ItemCallback<BookModel>() {
        override fun areItemsTheSame(
            oldItem: BookModel,
            newItem: BookModel,
        ): Boolean = false

        override fun areContentsTheSame(
            oldItem: BookModel,
            newItem: BookModel,
        ): Boolean =
            oldItem.id == newItem.id && oldItem.title == newItem.title && oldItem.score == newItem.score && oldItem.image == newItem.image
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ContentHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_book,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ContentHolder, position: Int) =
        holder.bind(getItem(position))

    interface SelectionCallback {
        fun onBookmark(bookModel: BookModel)
    }
}