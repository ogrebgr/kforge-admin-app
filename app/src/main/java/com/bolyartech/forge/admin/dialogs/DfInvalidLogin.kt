package com.bolyartech.forge.admin.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.bolyartech.forge.admin.R

class DfInvalidLogin : DialogFragment() {
    private lateinit var listener: Listener

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity is Listener) {
            listener = activity as Listener
        }
    }


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


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        if (::listener.isInitialized) {
            listener.onInvalidLoginDialogClosed()
        }
    }

    interface Listener {
        fun onInvalidLoginDialogClosed()
    }
}