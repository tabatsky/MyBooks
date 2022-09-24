package jatx.mybooks.domain.models

import jatx.mybooks.R
import java.text.SimpleDateFormat
import java.util.*

data class Book(
    var id: Int = -1,
    var author: String = "",
    var title: String = "",
    var type: BookType = BookType.PROGRAMMING,
    var date: Date = lastDate
) {
    val dateAsString: String
        get(){
            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale("ru-RU"))
            return sdf.format(date)
        }

    val backupString: String
        get() {
            val typeIndex = BookType.indexOfBookType(type)
            return "$id|$author|$title|$dateAsString|$typeIndex"
        }

    companion object {
        var lastDate = Date()
    }
}

enum class BookType(
    val typeName: String,
    val color: Int
) {
    PROGRAMMING("Программирование", R.color.light_green),
    FICTION("Художественная", R.color.light_blue),
    OTHER("Прочее", R.color.light_yellow);

    companion object {
        fun bookTypeByIndex(index: Int) = values().getOrNull(index)
            ?: throw IllegalArgumentException("No element with such index")

        fun indexOfBookType(bookType: BookType) = bookType.ordinal

        val stringsForSpinner = values().map { it.typeName }
    }
}