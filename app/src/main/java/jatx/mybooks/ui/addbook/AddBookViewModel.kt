package jatx.mybooks.ui.addbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jatx.mybooks.domain.models.Book
import jatx.mybooks.domain.models.BookType
import jatx.mybooks.domain.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AddBookViewModel : ViewModel() {
    val author: MutableStateFlow<String> = MutableStateFlow("")
    val title: MutableStateFlow<String> = MutableStateFlow("")
    val dateAsString: MutableStateFlow<String> = MutableStateFlow("")
    val isAudioBook: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isDeleteVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val stringsForSpinner: MutableStateFlow<List<String>> = MutableStateFlow(BookType.stringsForSpinner)
    val bookType: MutableStateFlow<Int> = MutableStateFlow(0)

    private var _book: Book? = null
        set(value) {
            field = value
            author.value = value?.author ?: ""
            title.value = value?.title ?: ""
            dateAsString.value = value?.dateAsString ?: ""
            isAudioBook.value = value?.isAudioBook ?: false
            isDeleteVisible.value = (value?.id ?: 0) > 0
            bookType.value = value?.type?.let { BookType.indexOfBookType(it) } ?: 0
        }
    val book: Book
        get() = _book ?: throw IllegalStateException("Seems book is null")

    fun loadBookById(id: Int) {
        if (id < 0) {
            _book = Book()
        } else {
            viewModelScope.launch {
                _book = BookRepository.INSTANCE.getBookById(id)
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
    
    fun delete(onSaved: () -> Unit) = viewModelScope.launch {
        BookRepository.INSTANCE.deleteBook(book)
        onSaved()
    }

    fun onSpinnerItemSelected(position: Any?) {
        (position as? Int)?.let {
            bookType.value = it
        }
    }
}