package jatx.mybooks.ui.booklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jatx.mybooks.domain.models.Book
import jatx.mybooks.domain.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookListViewModel : ViewModel() {
    private val _books: MutableStateFlow<List<Book>> = MutableStateFlow(emptyList())
    val books: StateFlow<List<Book>>
        get() = _books.asStateFlow()

    init {
        viewModelScope.launch {
            BookRepository.INSTANCE.getAllBooks().collect {
                _books.value = it
            }
        }
    }
}