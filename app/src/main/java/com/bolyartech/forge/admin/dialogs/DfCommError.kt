package com.bolyartech.forge.admin.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.bolyartech.forge.admin.R

class DfCommError : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layoutInflater = LayoutInflater.from(context)
        val v = layoutInflater.inflate(R.layout.dlg__comm_error, null)

        val b = AlertDialog.Builder(activity)
        b.setView(v)
        b.setNeutralButton(R.string.global_btn_close, null)

        return b.create()
    }

    companion object {
        const val DIALOG_TAG = "DfCommError"
    }
}