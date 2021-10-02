package com.castprogramms.elegion.tools

import android.R
import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import androidx.core.widget.addTextChangedListener

fun buildTextInputDialog(
    context: Context,
    title: String,
    result: (input: String) -> Unit
): AlertDialog.Builder {
    var address = ""
    return AlertDialog.Builder(context)
        .setTitle(title)
        .setView(EditText(context).apply {
            addTextChangedListener { address = text.toString() }
        })
        .setPositiveButton(context.getString(R.string.ok)) { _, _ ->
            result(address)
        }
}