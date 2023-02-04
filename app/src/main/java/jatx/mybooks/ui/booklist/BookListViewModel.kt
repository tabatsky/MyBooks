package jatx.mybooks.ui.booklist

import androidx.lifecycle.*
import jatx.mybooks.domain.models.Book
import jatx.mybooks.domain.models.BookType
import jatx.mybooks.domain.repository.BookRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BookListViewModel : ViewModel() {
    private val _books: MutableStateFlow<List<Book>> = MutableStateFlow(emptyList())
    private val books: StateFlow<List<Book>>
        get() = _books.asStateFlow()

    val allStringsForSpinner: MutableStateFlow<List<String>> = MutableStateFlow(BookType.allStringsForSpinner)
    val spinnerPosition = MutableStateFlow(0)

    val actualBooks = books.combine(spinnerPosition) { list, position ->
        list.filterByType(position)
    }.stateIn(viewModelScope, SharingStarted.Lazily, listOf())

    init {
        viewModelScope.launch {
            BookRepository.INSTANCE.getAllBooks().collect {
                _books.value = it
            }
        }
    }

    fun onSpinnerItemSelected(position: Any?) {
        (position as? Int)?.let {
            spinnerPosition.value = it
        }
    }

    private fun List<Book>.filterByType(spinnerPosition: Int): List<Book> {
        val type = BookType.bookTypeByIndex(spinnerPosition - 1)
        return filter {
            (it.type == type).or(type == BookType.ALL)
        }
    }
}