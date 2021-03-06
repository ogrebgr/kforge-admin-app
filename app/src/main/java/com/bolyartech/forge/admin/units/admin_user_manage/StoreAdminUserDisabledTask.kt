package com.bolyartech.forge.admin.units.admin_user_manage

import com.bolyartech.forge.admin.misc.TaskIds
import com.bolyartech.forge.base.exchange.ForgeExchangeOutcomeErrorCode
import com.bolyartech.forge.base.exchange.ForgeExchangeOutcomeOk
import com.bolyartech.forge.base.exchange.SessionForgeExchangeExecutor
import com.bolyartech.forge.base.exchange.forge.ForgeExchangeHelper
import com.bolyartech.forge.base.rc_task.AbstractRcTask
import com.bolyartech.forge.base.rc_task.RcTask
import com.bolyartech.forge.base.rc_task.RcTaskResult
import javax.inject.Inject

interface StoreAdminUserDisabledTask : RcTask<RcTaskResult<Void, Int>> {
    fun init(userId: Int, isBoolean: Boolean)
}


class StoreAdminUserDisabledTaskImpl @Inject constructor(
    private val forgeExchangeHelper: ForgeExchangeHelper,
    private val sessionForgeExchangeExecutor: SessionForgeExchangeExecutor
) : StoreAdminUserDisabledTask, AbstractRcTask<RcTaskResult<Void, Int>>(TaskIds.ADMIN_USER_STORE_DISABLED) {

    private val ENDPOINT = "admin_user_disable"
    private val PARAM_USER_ID = "user_id"
    private val PARAM_SUPER_ADMIN = "super_admin"


    private var userId: Int = 0
    private var isDisabled: Boolean = false

    override fun init(userId: Int, isDisabled: Boolean) {
        this.userId = userId
        this.isDisabled = isDisabled
    }

    override fun execute() {
        val b = forgeExchangeHelper.createForgePostHttpExchangeBuilder(ENDPOINT)
        b.addPostParameter(PARAM_USER_ID, userId.toString())
        b.addPostParameter(PARAM_SUPER_ADMIN, if (isDisabled) "1" else "0")

        return when (val out = sessionForgeExchangeExecutor.execute(b.build())) {
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
