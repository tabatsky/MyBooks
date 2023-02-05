package jatx.mybooks.ui.booklist

import androidx.lifecycle.*
import jatx.mybooks.domain.models.Book
import jatx.mybooks.domain.models.BookType
import jatx.mybooks.domain.repository.BookRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BookListViewModel : ViewModel() {
    private val books: MutableStateFlow<List<Book>> = MutableStateFlow(emptyList())

    val allStringsForSpinner = BookType.allStringsForSpinner
    val spinnerPosition = MutableStateFlow(0)

    val actualBooks = books.combine(spinnerPosition) { list, position ->
        list.filterByType(position)
    }.stateIn(viewModelScope, SharingStarted.Lazily, listOf())

    init {
        viewModelScope.launch {
            BookRepository.INSTANCE.getAllBooks().collect {
                books.value = it
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