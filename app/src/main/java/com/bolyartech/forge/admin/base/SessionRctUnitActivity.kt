package com.bolyartech.forge.admin.base

import android.content.Intent
import com.bolyartech.forge.admin.units.main.ActMain
import com.bolyartech.forge.android.app_unit.rc_task.RctResidentComponent
import com.bolyartech.forge.android.app_unit.rc_task.TaskExecutionStateful
import com.bolyartech.forge.base.session.Session
import javax.inject.Inject

abstract class SessionRctUnitActivity<T> : RctUnitActivity<T>()
        where T : RctResidentComponent, T : TaskExecutionStateful {

    @Inject
    internal lateinit var session: Session

    override fun onResume() {
        super.onResume()
        if (this !is PerformsLogin) {
            if (!session.isLoggedIn) {
                goHome()
            }
        }
    }

    private fun goHome() {
        val intent = Intent(this, ActMain::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}
