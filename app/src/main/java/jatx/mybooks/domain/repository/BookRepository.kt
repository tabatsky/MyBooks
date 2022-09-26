package jatx.mybooks.domain.repository

import jatx.mybooks.domain.models.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getAllBooks(): Flow<List<Book>>
    suspend fun getBookById(id: Int): Book
    suspend fun addBook(book: Book)
    suspend fun updateBook(book: Book)
    suspend fun deleteBook(book: Book)

    companion object {
        lateinit var INSTANCE: BookRepository
    }
}