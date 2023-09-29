package com.example.scheduleforictis2.utils.ext

import android.widget.EditText

fun EditText.showKeyboard() {
    requestFocus()
    post { KeyboardUtils.showSoftInput(this) }
}

fun EditText.hideKeyboard() {
    post { KeyboardUtils.hideSoftInput(this) }
}