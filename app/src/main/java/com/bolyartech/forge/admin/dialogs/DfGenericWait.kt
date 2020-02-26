package com.bolyartech.forge.admin.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.bolyartech.forge.admin.R

class DfGenericWait : DialogFragment() {
    private lateinit var listener: Listener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (activity is Listener) {
            listener = activity as Listener
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layoutInflater = LayoutInflater.from(context)
        val v = layoutInflater.inflate(R.layout.dlg__wait, null)

        val b = AlertDialog.Builder(activity)
        b.setView(v)
        b.setCancelable(false)

        val ret = b.create()
        ret.setCanceledOnTouchOutside(false)
        return ret
    }


    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)

        if (this::listener.isInitialized) {
            listener.onGenericWaitDialogCancelled()
        }
    }


    interface Listener {
        fun onGenericWaitDialogCancelled()
    }


    companion object {
        const val DIALOG_TAG = "DfGenericWait"
    }
}