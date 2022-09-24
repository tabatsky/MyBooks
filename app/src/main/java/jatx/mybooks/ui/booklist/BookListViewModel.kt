package jatx.mybooks.ui.booklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jatx.mybooks.App
import jatx.mybooks.domain.models.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BookListViewModel : ViewModel() {
    private val _books: MutableStateFlow<List<Book>> = MutableStateFlow(emptyList())
    val books: StateFlow<List<Book>>
        get() = _books.asStateFlow()

    init {
        viewModelScope.launch {
            App.bookRepository.getAllBooks().collect {
                _books.value = it
            }
        }
    }
}