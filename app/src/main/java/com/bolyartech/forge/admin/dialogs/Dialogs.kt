package com.bolyartech.forge.admin.dialogs

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

fun showGenericWaitDialog(fm: FragmentManager) {
    if (fm.findFragmentByTag(DfGenericWait.DIALOG_TAG) == null) {
        val fra = DfGenericWait()
        fra.show(fm, DfGenericWait.DIALOG_TAG)
        fm.executePendingTransactions()
    }
}


fun hideGenericWaitDialog(fm: FragmentManager) {
    val df = fm.findFragmentByTag(DfGenericWait.DIALOG_TAG) as? DialogFragment
    if (df != null) {
        df.dismissAllowingStateLoss()
        fm.executePendingTransactions()
    }
}


fun showSessionExpiredDialog(fm: FragmentManager) {
    if (fm.findFragmentByTag(DfSessionExpired.DIALOG_TAG) == null) {
        val fra = DfSessionExpired()
        fra.show(fm, DfSessionExpired.DIALOG_TAG)
        fm.executePendingTransactions()
    }
}

fun showCommErrorDialog(fm: FragmentManager) {
    if (fm.findFragmentByTag(DfCommError.DIALOG_TAG) == null) {
        val fra = DfCommError()
        fra.show(fm, DfCommError.DIALOG_TAG)
        fm.executePendingTransactions()
    }
}
