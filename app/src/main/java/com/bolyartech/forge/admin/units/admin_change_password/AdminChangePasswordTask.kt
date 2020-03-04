package com.bolyartech.forge.admin.units.admin_change_password

import com.bolyartech.forge.admin.misc.TaskIds.Companion.ADMIN_CHANGE_PASSWORD
import com.bolyartech.forge.base.exchange.ForgeExchangeOutcomeErrorCode
import com.bolyartech.forge.base.exchange.ForgeExchangeOutcomeOk
import com.bolyartech.forge.base.exchange.SessionForgeExchangeExecutor
import com.bolyartech.forge.base.exchange.forge.ForgeExchangeHelper
import com.bolyartech.forge.base.rc_task.AbstractRcTask
import com.bolyartech.forge.base.rc_task.RcTask
import com.bolyartech.forge.base.rc_task.RcTaskResult
import javax.inject.Inject


interface AdminChangePasswordTask : RcTask<RcTaskResult<Void, Int>> {
    fun init(userId: Int, password: String)
}

class AdminChangePasswordTaskImpl @Inject constructor(
    private val forgeExchangeHelper: ForgeExchangeHelper,
    private val sessionForgeExchangeExecutor: SessionForgeExchangeExecutor
) : AdminChangePasswordTask, AbstractRcTask<RcTaskResult<Void, Int>>(ADMIN_CHANGE_PASSWORD) {

    private val ENDPOINT = "admin_change_password"
    private val PARAM_USER_ID = "user_id"
    private val PARAM_PASSWORD = "password"

    private var userId: Int = 0
    private lateinit var password: String

    override fun init(userId: Int, password: String) {
        this.userId = userId
        this.password = password
    }

    override fun execute() {
        val b = forgeExchangeHelper.createForgePostHttpExchangeBuilder(ENDPOINT)
        b.addPostParameter(PARAM_USER_ID, userId.toString())
        b.addPostParameter(PARAM_PASSWORD, password)

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