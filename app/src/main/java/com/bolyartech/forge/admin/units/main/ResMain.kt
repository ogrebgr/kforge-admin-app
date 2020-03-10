package com.bolyartech.forge.admin.units.main

import com.bolyartech.forge.admin.misc.LoginPrefs
import com.bolyartech.forge.admin.misc.TaskIds
import com.bolyartech.forge.admin.units.login.LoginTask
import com.bolyartech.forge.android.app_unit.rc_task.AbstractRctResidentComponent
import com.bolyartech.forge.android.app_unit.rc_task.RctResidentComponent
import com.bolyartech.forge.android.app_unit.rc_task.executor.RcTaskExecutor
import com.bolyartech.forge.base.rc_task.RcTaskToExecutor
import javax.inject.Inject
import javax.inject.Provider

interface ResMain : RctResidentComponent {
    fun login()
    fun getErrorCore(): Int

    fun logout()
}


class ResMainImpl @Inject constructor(
    taskExecutor: RcTaskExecutor,
    private val logoutTaskProvider: Provider<LogoutTask>,
    private val loginTaskProvider: Provider<LoginTask>,
    private val loginPrefs: LoginPrefs
) : ResMain, AbstractRctResidentComponent(taskExecutor) {

    private var loginTask: LoginTask? = null
    private var errorCode: Int = 0

    override fun login() {
        val t = loginTaskProvider.get()
        t.init(loginPrefs.getUsername()!!, loginPrefs.getPassword()!!)
        loginTask = t
        executeTask(t)
    }

    override fun getErrorCore(): Int {
        return errorCode
    }


    override fun logout() {
        executeTask(logoutTaskProvider.get())
    }

    override fun onTaskPostExecute(endedTask: RcTaskToExecutor) {
        if (endedTask.id == TaskIds.LOGIN_TASK && endedTask.isFailure) {
            errorCode = loginTask!!.result.errorValue
        }
    }
}