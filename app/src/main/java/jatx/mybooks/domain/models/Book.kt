package jatx.mybooks.domain.models

import jatx.mybooks.R
import java.text.SimpleDateFormat
import java.util.*

data class Book(
    var id: Int = -1,
    var author: String = "",
    var title: String = "",
    var type: BookType = BookType.PROGRAMMING,
    var date: Date = lastDate,
    var isAudioBook: Boolean = false
) {
    val dateAsString: String
        get(){
            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale("ru-RU"))
            return sdf.format(date)
        }

    val backupString: String
        get() {
            val typeIndex = BookType.indexOfBookType(type)
            return "$id|$author|$title|$dateAsString|$typeIndex|$isAudioBook"
        }

    companion object {
        var lastDate = Date()
    }
}

enum class BookType(
    val typeName: String,
    val index: Int,
    val color: Int
) {
    ALL("Все", -1, R.color.black),
    PROGRAMMING("Программирование", 0, R.color.light_green),
    FICTION("Художественная", 1, R.color.light_blue),
    OTHER("Прочее", 2, R.color.light_yellow);

    companion object {
        fun bookTypeByIndex(index: Int) = values().firstOrNull { it.index == index }
            ?: throw IllegalArgumentException("No element with such index")

        fun indexOfBookType(bookType: BookType) = bookType.index

        val stringsForSpinner = values().filter { it.index >= 0 }.map { it.typeName }
        val allStringsForSpinner = values().map { it.typeName }
    }
}