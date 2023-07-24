package jatx.mybooks.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import jatx.mybooks.data.db.entity.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM books ORDER BY (date + id) DESC")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE id=:id")
    suspend fun getBookById(id: Int): BookEntity

    @Insert
    suspend fun addBook(bookEntity: BookEntity)

    @Update
    suspend fun updateBook(bookEntity: BookEntity)

    @Delete
    suspend fun deleteBook(bookEntity: BookEntity)

    @Query("DELETE FROM books")
    suspend fun deleteAllBooks()

    @Insert
    suspend fun addBooks(bookEntities: List<BookEntity>)
}