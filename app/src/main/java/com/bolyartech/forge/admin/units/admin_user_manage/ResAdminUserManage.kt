package com.bolyartech.forge.admin.units.admin_user_manage

import com.bolyartech.forge.admin.data.AdminUserExportedView
import com.bolyartech.forge.admin.misc.TaskIds
import com.bolyartech.forge.android.app_unit.rc_task.AbstractRctResidentComponent
import com.bolyartech.forge.android.app_unit.rc_task.RctResidentComponent
import com.bolyartech.forge.android.app_unit.rc_task.executor.RcTaskExecutor
import com.bolyartech.forge.base.rc_task.RcTaskToExecutor
import javax.inject.Inject
import javax.inject.Provider

interface ResAdminUserManage : RctResidentComponent {
    fun init(user: AdminUserExportedView)
    fun loadUser()
    fun getUser(): AdminUserExportedView
    fun getErrorCode(): Int

    fun raiseDisable()
    fun lowerDisable()
    fun raiseSuperAdmin()
    fun lowerSuperAdmin()
}

class ResAdminUserManageImpl @Inject constructor(
    taskExecutor: RcTaskExecutor,
    private val loadUserProvider: Provider<LoadAdminUserTask>
) : ResAdminUserManage,
    AbstractRctResidentComponent(taskExecutor) {

    private var loadAdminUserTask: LoadAdminUserTask? = null

    private var errorCode: Int = 0

    private var user: AdminUserExportedView? = null

    override fun loadUser() {
        val task = loadUserProvider.get()
        task.init(user!!.id)
        loadAdminUserTask = task
        executeTask(loadAdminUserTask)
    }

    override fun init(user: AdminUserExportedView) {
        this.user = user
    }

    override fun getUser(): AdminUserExportedView {
        return user!!
    }

    override fun getErrorCode(): Int {
        return errorCode
    }

    override fun raiseDisable() {
        TODO("not implemented")
    }

    override fun lowerDisable() {
        TODO("not implemented")
    }

    override fun raiseSuperAdmin() {
        TODO("not implemented")
    }

    override fun lowerSuperAdmin() {
        TODO("not implemented")
    }

    override fun onTaskPostExecute(endedTask: RcTaskToExecutor) {
        when (endedTask.id) {
            TaskIds.LOAD_ADMIN_USER_TASK -> {
                if (endedTask.isSuccess) {
                    user = loadAdminUserTask!!.result.successValue
                } else {
                    errorCode = loadAdminUserTask!!.result.errorValue
                }
            }
        }
    }

}