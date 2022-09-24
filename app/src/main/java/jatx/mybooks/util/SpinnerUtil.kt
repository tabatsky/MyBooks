package jatx.mybooks.util

import android.R
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import jatx.mybooks.domain.models.BookType

fun <T> Spinner.setItems(list: List<T>) {
    adapter = ArrayAdapter(
        context,
        R.layout.simple_spinner_dropdown_item,
        list
    )
}

fun Spinner.setOnItemSelectedListener(onSelect: (Int) -> Unit) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            onSelect(position)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
}