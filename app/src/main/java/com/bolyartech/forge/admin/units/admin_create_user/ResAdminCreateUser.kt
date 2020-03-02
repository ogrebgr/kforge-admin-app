package com.bolyartech.forge.admin.units.admin_create_user

import com.bolyartech.forge.android.app_unit.rc_task.AbstractRctResidentComponent
import com.bolyartech.forge.android.app_unit.rc_task.RctResidentComponent
import com.bolyartech.forge.android.app_unit.rc_task.executor.RcTaskExecutor
import com.bolyartech.forge.base.rc_task.RcTaskToExecutor
import javax.inject.Inject
import javax.inject.Provider

interface ResAdminCreateUser : RctResidentComponent {
    fun createUser(username: String, password: String, name: String, isSuperAdmin: Boolean)
    fun getErrorCode(): Int
}


class ResAdminCreateUserImpl @Inject constructor(
    taskExecutor: RcTaskExecutor,
    private val taskProvider: Provider<AdminCreateUserTask>
) : ResAdminCreateUser,
    AbstractRctResidentComponent(taskExecutor) {

    private var task: AdminCreateUserTask? = null
    private var errorCode: Int = 0

    override fun createUser(username: String, password: String, name: String, isSuperAdmin: Boolean) {
        val t = taskProvider.get()
        t.init(username, password, name, isSuperAdmin)
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