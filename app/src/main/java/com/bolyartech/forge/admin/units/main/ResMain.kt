package com.bolyartech.forge.admin.units.main

import com.bolyartech.forge.android.app_unit.rc_task.AbstractRctResidentComponent
import com.bolyartech.forge.android.app_unit.rc_task.RctResidentComponent
import com.bolyartech.forge.android.app_unit.rc_task.executor.RcTaskExecutor
import com.bolyartech.forge.base.rc_task.RcTaskToExecutor
import javax.inject.Inject
import javax.inject.Provider

interface ResMain : RctResidentComponent {
    fun logout()
}


class ResMainImpl @Inject constructor(
    taskExecutor: RcTaskExecutor,
    private val taskProvider: Provider<LogoutTask>
) : ResMain, AbstractRctResidentComponent(taskExecutor) {

    override fun logout() {
        executeTask(taskProvider.get())
    }

    override fun onTaskPostExecute(endedTask: RcTaskToExecutor) {
        // empty
    }
}