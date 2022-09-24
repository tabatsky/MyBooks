package jatx.mybooks.util

import android.app.Activity
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import jatx.mybooks.MainActivity

fun Activity.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun Activity.showToast(@StringRes resId: Int) {
    val text = getString(resId)
    showToast(text)
}

fun Fragment.showToast(text: String) = requireActivity().showToast(text)

fun Fragment.showToast(@StringRes resId: Int) = requireActivity().showToast(resId)