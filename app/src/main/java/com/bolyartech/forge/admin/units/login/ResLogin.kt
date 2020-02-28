package com.bolyartech.forge.admin.units.login

import com.bolyartech.forge.android.app_unit.rc_task.AbstractRctResidentComponent
import com.bolyartech.forge.android.app_unit.rc_task.RctResidentComponent
import com.bolyartech.forge.android.app_unit.rc_task.executor.RcTaskExecutor
import com.bolyartech.forge.base.rc_task.RcTaskToExecutor
import javax.inject.Inject
import javax.inject.Provider

interface ResLogin : RctResidentComponent {
    fun login(username: String, password: String)
    fun getErrorCode(): Int
}


class ResLoginImpl @Inject constructor(
    taskExecutor: RcTaskExecutor,
    private val loginTaskProvider: Provider<LoginTask>
) : ResLogin, AbstractRctResidentComponent(taskExecutor) {

    private var loginTask: LoginTask? = null

    private var errorCode: Int = 0

    override fun login(username: String, password: String) {
        if (isIdle) {
            val lt = loginTaskProvider.get()
            lt.init(username, password)
            loginTask = lt
            executeTask(lt)
        } else {
            throw IllegalStateException("Not in IDLE")
        }
    }

    override fun getErrorCode(): Int {
        return errorCode
    }


    override fun onTaskPostExecute(endedTask: RcTaskToExecutor) {
        if (endedTask.isFailure) {
            errorCode = loginTask!!.result.errorValue
        }
    }
}