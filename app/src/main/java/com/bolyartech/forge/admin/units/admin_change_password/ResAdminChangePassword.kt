package com.bolyartech.forge.admin.units.admin_change_password

import com.bolyartech.forge.android.app_unit.rc_task.AbstractRctResidentComponent
import com.bolyartech.forge.android.app_unit.rc_task.RctResidentComponent
import com.bolyartech.forge.android.app_unit.rc_task.executor.RcTaskExecutor
import com.bolyartech.forge.base.rc_task.RcTaskToExecutor
import javax.inject.Inject
import javax.inject.Provider

interface ResAdminChangePassword : RctResidentComponent {
    fun init(userId: Int)
    fun changePassword(password: String)
    fun getErrorCode(): Int
}


class ResAdminChangePasswordImpl @Inject constructor(
    taskExecutor: RcTaskExecutor,
    private val taskProvider: Provider<AdminChangePasswordTask>
) : ResAdminChangePassword, AbstractRctResidentComponent(taskExecutor) {

    private var userId: Int = 0

    private var task: AdminChangePasswordTask? = null
    private var errorCode: Int = 0

    override fun init(userId: Int) {
        this.userId = userId
    }

    override fun changePassword(password: String) {
        if (userId == 0) {
            throw IllegalStateException("Did you forgot to call init()?")
        }

        val t = taskProvider.get()
        t.init(userId, password)
        task = t
        executeTask(t)
    }

    override fun getErrorCode(): Int {
        return errorCode
    }

    override fun onTaskPostExecute(endedTask: RcTaskToExecutor) {
        if (endedTask.isFailure) {
            errorCode = task!!.result.errorValue
        }
    }
}