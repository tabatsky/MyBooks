package jatx.mybooks.data.db.repository

import jatx.mybooks.data.db.AppDatabase
import jatx.mybooks.data.db.entity.toEntity
import jatx.mybooks.data.db.entity.toModel
import jatx.mybooks.domain.models.Book
import jatx.mybooks.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookRepositoryImpl(
    private val appDatabase: AppDatabase
    ): BookRepository {
    override fun getAllBooks(): Flow<List<Book>> = appDatabase
        .bookDao()
        .getAllBooks()
        .map { list ->
            list.map { it.toModel() }
        }

    override suspend fun getBookById(id: Int) = appDatabase
        .bookDao()
        .getBookById(id)
        .toModel()

    override suspend fun addBook(book: Book) = appDatabase
        .bookDao()
        .addBook(book.toEntity())

    override suspend fun updateBook(book: Book) = appDatabase
        .bookDao()
        .updateBook(book.toEntity())

    override suspend fun deleteBook(book: Book) = appDatabase
        .bookDao()
        .deleteBook(book.toEntity())

    override suspend fun deleteAllBooks() = appDatabase
        .bookDao()
        .deleteAllBooks()

    override suspend fun addBooks(books: List<Book>) = appDatabase
        .bookDao()
        .addBooks(books.map { it.toEntity() })
}