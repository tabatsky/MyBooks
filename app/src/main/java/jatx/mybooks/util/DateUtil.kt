package jatx.mybooks.util

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import java.util.*

val Date.theYear: Int
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar.get(Calendar.YEAR)
    }

val Date.theMonth: Int
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar.get(Calendar.MONTH)
    }

val Date.theDayOfMonth: Int
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

fun Fragment.selectDate(date: Date, onDateSet: (Date) -> Unit) {
    val onDateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            onDateSet(calendar.time)
        }

    val dpd = DatePickerDialog(
        requireContext(),
        onDateSetListener,
        date.theYear,
        date.theMonth,
        date.theDayOfMonth
    )
    dpd.show()
}