package com.bolyartech.forge.admin.units.admin_create_user

import com.bolyartech.forge.admin.misc.TaskIds
import com.bolyartech.forge.base.exchange.ForgeExchangeOutcomeErrorCode
import com.bolyartech.forge.base.exchange.ForgeExchangeOutcomeOk
import com.bolyartech.forge.base.exchange.SessionForgeExchangeExecutor
import com.bolyartech.forge.base.exchange.forge.ForgeExchangeHelper
import com.bolyartech.forge.base.rc_task.AbstractRcTask
import com.bolyartech.forge.base.rc_task.RcTask
import com.bolyartech.forge.base.rc_task.RcTaskResult
import javax.inject.Inject

interface AdminCreateUserTask : RcTask<RcTaskResult<Void, Int>> {
    fun init(username: String, password: String, name: String, isSuperAdmin: Boolean)
}


class AdminCreateUserTaskImpl @Inject constructor(
    private val forgeExchangeHelper: ForgeExchangeHelper,
    private val sessionForgeExchangeExecutor: SessionForgeExchangeExecutor
) : AdminCreateUserTask, AbstractRcTask<RcTaskResult<Void, Int>>(TaskIds.ADMIN_USER_CREATE_TASK) {

    private val ENDPOINT = "admin_user_create"

    private val PARAM_USERNAME = "username"
    private val PARAM_PASSWORD = "password"
    private val PARAM_NAME = "name"
    private val PARAM_SUPER_ADMIN = "super_admin"

    private lateinit var username: String
    private lateinit var password: String
    private lateinit var name: String
    private var isSuperAdmin: Boolean = false

    override fun init(username: String, password: String, name: String, isSuperAdmin: Boolean) {
        this.username = username
        this.password = password
        this.name = name
        this.isSuperAdmin = isSuperAdmin
    }


    override fun execute() {
        val b = forgeExchangeHelper.createForgePostHttpExchangeBuilder(ENDPOINT)
        b.addPostParameter(PARAM_USERNAME, username)
        b.addPostParameter(PARAM_PASSWORD, password)
        b.addPostParameter(PARAM_NAME, name)
        b.addPostParameter(PARAM_SUPER_ADMIN, if (isSuperAdmin) "1" else "0")

        when (val out = sessionForgeExchangeExecutor.execute(b.build())) {
            is ForgeExchangeOutcomeOk -> {
                setTaskResult(RcTaskResult.createSuccessResult(null))
            }

            is ForgeExchangeOutcomeErrorCode -> {
                setTaskResult(RcTaskResult.createErrorResult(out.code))
            }

            else -> {
                setTaskResult(RcTaskResult.createErrorResult(-1))
            }
        }
    }

}