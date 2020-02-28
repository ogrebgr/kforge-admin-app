package com.bolyartech.forge.admin.units.login

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.bolyartech.forge.admin.R

class DfInvalidLogin : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layoutInflater = LayoutInflater.from(context)
        val v = layoutInflater.inflate(R.layout.dlg__invalid_login, null)

        val b = AlertDialog.Builder(activity)
        b.setView(v)
        b.setNeutralButton(R.string.global_btn_close, null)

        return b.create()
    }

    companion object {
        const val DIALOG_TAG = "DfInvalidLogin"
    }
}