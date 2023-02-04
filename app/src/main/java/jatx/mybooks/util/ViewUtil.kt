package jatx.mybooks.util

import android.view.View
import androidx.annotation.ColorRes
import androidx.databinding.BindingAdapter

@BindingAdapter("backgroundColorResource")
fun setBackgroundColorResource(view: View, @ColorRes colorRes: Int) {
    @Suppress("DEPRECATION") val bgColor = view.context.resources.getColor(colorRes)
    view.setBackgroundColor(bgColor)
}