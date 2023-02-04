package jatx.mybooks.ui.booklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jatx.mybooks.databinding.ItemBookListBinding
import jatx.mybooks.domain.models.Book

class BookListAdapter: ListAdapter<Book, BookHolder>(BookDiffUtil()) {

    var onItemClick: (Int) -> Unit  = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBookListBinding.inflate(inflater, parent, false)
        return BookHolder(binding)
    }

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        holder.bind(getItem(position)) {
            onItemClick(it)
        }
    }
}

class BookHolder(
    private val binding: ItemBookListBinding
    ): RecyclerView.ViewHolder(binding.root) {

    fun bind(book: Book, onItemClick: (Int) -> Unit) {
        with(binding) {
            this.book = book
            root.setOnClickListener {
                onItemClick(book.id)
            }
            executePendingBindings()
        }

    }
}

class BookDiffUtil(): DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return (oldItem.author == newItem.author)
            .and(oldItem.title == newItem.title)
            .and(oldItem.type == newItem.type)
            .and(oldItem.date == newItem.date)
            .and(oldItem.isAudioBook == newItem.isAudioBook)
    }

}