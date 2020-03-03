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

    fun toggleDisable()
    fun toggleSuperAdmin()
}

class ResAdminUserManageImpl @Inject constructor(
    taskExecutor: RcTaskExecutor,
    private val loadUserProvider: Provider<LoadAdminUserTask>,
    private val storeSuperAdminTaskProvider: Provider<StoreSuperAdminTask>,
    private val storeDisabledTaskProvider: Provider<StoreAdminUserDisabledTask>

) : ResAdminUserManage,
    AbstractRctResidentComponent(taskExecutor) {

    private var loadAdminUserTask: LoadAdminUserTask? = null
    private var storeSuperAdminTask: StoreSuperAdminTask? = null
    private var storeDisabledTask: StoreAdminUserDisabledTask? = null


    private var errorCode: Int = 0

    private lateinit var user: AdminUserExportedView

    override fun loadUser() {
        val task = loadUserProvider.get()
        task.init(user.id)
        loadAdminUserTask = task
        executeTask(loadAdminUserTask)
    }

    override fun init(user: AdminUserExportedView) {
        this.user = user
    }

    override fun getUser(): AdminUserExportedView {
        return user
    }

    override fun getErrorCode(): Int {
        return errorCode
    }

    override fun toggleSuperAdmin() {
        val t = storeSuperAdminTaskProvider.get()
        t.init(user.id, !user.isSuperUser)
        storeSuperAdminTask = t
        executeTask(t)
    }

    override fun toggleDisable() {
        val t = storeDisabledTaskProvider.get()
        t.init(user.id, !user.isDisabled)
        storeDisabledTask = t
        executeTask(t)
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

            TaskIds.ADMIN_USER_STORE_SUPERADMIN -> {
                if (endedTask.isSuccess) {
                    user = AdminUserExportedView(user.id, user.username, user.isDisabled, !user.isSuperUser, user.name)
                } else {
                    errorCode = storeSuperAdminTask!!.result.errorValue
                }
            }

            TaskIds.ADMIN_USER_STORE_DISABLED -> {
                if (endedTask.isSuccess) {
                    user = AdminUserExportedView(user.id, user.username, !user.isDisabled, user.isSuperUser, user.name)
                } else {
                    errorCode = storeSuperAdminTask!!.result.errorValue
                }
            }
        }
    }
}