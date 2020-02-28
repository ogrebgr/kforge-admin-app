package com.bolyartech.forge.admin.units.admin_users

import com.bolyartech.forge.admin.data.AdminUserExportedView
import com.bolyartech.forge.android.app_unit.rc_task.AbstractRctResidentComponent
import com.bolyartech.forge.android.app_unit.rc_task.RctResidentComponent
import com.bolyartech.forge.android.app_unit.rc_task.executor.RcTaskExecutor
import com.bolyartech.forge.base.rc_task.RcTaskToExecutor
import javax.inject.Inject
import javax.inject.Provider

interface ResAdminUsers : RctResidentComponent {
    fun listAdminUsers()
    fun getAdminUsers(): List<AdminUserExportedView>
    fun getErrorCode(): Int
}


class ResAdminUsersImpl @Inject constructor(
    taskExecutor: RcTaskExecutor,
    private val taskProvider: Provider<ListAdminUsersTask>
) : ResAdminUsers, AbstractRctResidentComponent(taskExecutor) {

    private var task: ListAdminUsersTask? = null

    private var users: List<AdminUserExportedView> = mutableListOf()
    private var errorCode: Int = 0

    override fun listAdminUsers() {
        val t = taskProvider.get()
        task = t
        executeTask(t)
    }

    override fun getAdminUsers(): List<AdminUserExportedView> {
        return users
    }

    override fun getErrorCode(): Int {
        return errorCode
    }

    override fun onTaskPostExecute(endedTask: RcTaskToExecutor) {
        if (endedTask.isSuccess) {
            users = task!!.result.successValue
        } else {
            errorCode = task!!.result.errorValue
        }
    }
}