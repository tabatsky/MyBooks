package jatx.mybooks.ui.addbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jatx.mybooks.App
import jatx.mybooks.domain.models.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddBookViewModel : ViewModel() {
    private var _book: Book? = null
    val book: Book
        get() = _book ?: throw IllegalStateException("Seems book is null")

    fun loadBookById(id: Int, onLoadSuccess: () -> Unit) {
        if (id < 0) {
            _book = Book()
            onLoadSuccess()
        } else {
            viewModelScope.launch {
                _book = App.bookRepository.getBookById(id)
                onLoadSuccess()
            }
        }
    }

    fun save(onSaved: () -> Unit) = viewModelScope.launch {
        if (book.id < 0) {
            App.bookRepository.addBook(book)
        } else {
            App.bookRepository.updateBook(book)
        }
        Book.lastDate = book.date
        onSaved()
    }
}