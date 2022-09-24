package jatx.mybooks.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import jatx.mybooks.domain.models.Book
import jatx.mybooks.domain.models.BookType
import java.util.*

@Entity(
    tableName = "books",
    indices = [
        Index("author"),
        Index("title")
    ]
)
data class BookEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val author: String,
    val title: String,
    val type: Int,
    val date: Long
)

fun BookEntity.toModel() = Book(
    id = id,
    author = author,
    title = title,
    type = BookType.bookTypeByIndex(type),
    date = Date(date)
)

fun Book.toEntity() = BookEntity(
    id = if (id < 0) 0 else id,
    author = author,
    title = title,
    type = BookType.indexOfBookType(type),
    date = date.time
)