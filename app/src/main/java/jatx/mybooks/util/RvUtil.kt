package jatx.mybooks.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import jatx.mybooks.domain.models.Book
import jatx.mybooks.ui.booklist.BookListAdapter

@BindingAdapter("items")
fun <T> setItems(rv: RecyclerView, list: List<T>) {
    (rv.adapter as? BookListAdapter)?.submitList(list.map { it as Book })
}