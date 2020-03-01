package com.bolyartech.forge.admin.units.admin_user_manage

import com.bolyartech.forge.admin.data.AdminUserExportedView
import com.bolyartech.forge.admin.misc.TaskIds.Companion.LOAD_ADMIN_USER_TASK
import com.bolyartech.forge.base.exchange.ForgeExchangeOutcomeErrorCode
import com.bolyartech.forge.base.exchange.ForgeExchangeOutcomeOk
import com.bolyartech.forge.base.exchange.SessionForgeExchangeExecutor
import com.bolyartech.forge.base.exchange.forge.ForgeExchangeHelper
import com.bolyartech.forge.base.rc_task.AbstractRcTask
import com.bolyartech.forge.base.rc_task.RcTask
import com.bolyartech.forge.base.rc_task.RcTaskResult
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.slf4j.LoggerFactory
import javax.inject.Inject

interface LoadAdminUserTask : RcTask<RcTaskResult<AdminUserExportedView, Int>> {
    fun init(userId: Int)
}


class LoadAdminUserTaskImpl @Inject constructor(
    private val forgeExchangeHelper: ForgeExchangeHelper,
    private val sessionForgeExchangeExecutor: SessionForgeExchangeExecutor
) : LoadAdminUserTask,
    AbstractRcTask<RcTaskResult<AdminUserExportedView, Int>>(LOAD_ADMIN_USER_TASK) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    private val ENDPOINT_LOAD = "user"
    private val PARAM_ID = "id"

    private var userId: Int = 0

    private val gson = Gson()

    override fun init(userId: Int) {
        this.userId = userId
    }

    override fun execute() {
        if (userId == 0) {
            throw IllegalStateException("Did you forgot to call init()")
        }

        val b = forgeExchangeHelper.createForgeGetHttpExchangeBuilder(ENDPOINT_LOAD)
        b.addGetParameter(PARAM_ID, userId.toString())

        when (val outcome = sessionForgeExchangeExecutor.execute(b.build())) {
            is ForgeExchangeOutcomeOk -> {
                val user = try {
                    gson.fromJson(outcome.payload, AdminUserExportedView::class.java)
                } catch (e: JsonSyntaxException) {
                    logger.error("Cannot parse JSON")
                    setTaskResult(RcTaskResult.createErrorResult(-1))
                    return
                }

                setTaskResult(RcTaskResult.createSuccessResult(user))
            }

            is ForgeExchangeOutcomeErrorCode -> {
                setTaskResult(RcTaskResult.createErrorResult(outcome.code))
            }

            else -> {
                setTaskResult(RcTaskResult.createErrorResult(-1))
            }
        }

    }
}

