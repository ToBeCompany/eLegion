package com.castprogramms.elegion.tools

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.core.widget.addTextChangedListener
import com.castprogramms.elegion.R
import com.castprogramms.elegion.databinding.BasicEditTextDialogBinding

fun buildTextInputDialog(
    context: Context,
    title: String,
    result: (input: String) -> Unit
): AlertDialog {
    var address = ""
    val view = LayoutInflater.from(context).inflate(R.layout.basic_edit_text_dialog, null)
    val binding = BasicEditTextDialogBinding.bind(view)
    val ad = AlertDialog.Builder(context)
        .setView(view)
        .create()
        .apply {
            if (window != null)
                window!!.setBackgroundDrawable(ColorDrawable(0))
        }

    binding.button.setOnClickListener {
        result(address)
        ad.dismiss()
    }
    binding.quantityBatutCoin.apply {
        addTextChangedListener { address = text.toString() }
    }
    binding.textTitle.text = title
    return ad
}