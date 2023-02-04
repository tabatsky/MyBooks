package jatx.mybooks.util

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter

@BindingAdapter("items")
fun <T> Spinner.setItems(list: List<T>) {
    adapter = ArrayAdapter(
        context,
        android.R.layout.simple_spinner_dropdown_item,
        list
    )
}

@BindingAdapter("onItemSelectedListener")
fun setOnItemSelectedListener(spinner: Spinner, onSelect: (Any?) -> Unit) {
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        var prevPosition = 0

        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            if (position != prevPosition) {
                prevPosition = position
                onSelect(position)
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
}

@BindingAdapter("selectedItem")
fun setSelectedItem(spinner: Spinner, position: Int) {
    Log.e("select position", position.toString())
    spinner.setSelection(position, false)
}
