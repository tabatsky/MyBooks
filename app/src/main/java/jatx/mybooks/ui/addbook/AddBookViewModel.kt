package jatx.mybooks.ui.addbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jatx.mybooks.domain.models.Book
import jatx.mybooks.domain.repository.BookRepository
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
                _book = BookRepository.INSTANCE.getBookById(id)
                onLoadSuccess()
            }
        }
    }

    fun save(onSaved: () -> Unit) = viewModelScope.launch {
        if (book.id < 0) {
            BookRepository.INSTANCE.addBook(book)
        } else {
            BookRepository.INSTANCE.updateBook(book)
        }
        Book.lastDate = book.date
        onSaved()
    }
}