package com.bolyartech.forge.admin.units.admin_users

import com.bolyartech.forge.admin.data.AdminUserExportedView
import com.bolyartech.forge.admin.misc.TaskIds
import com.bolyartech.forge.base.exchange.ForgeExchangeOutcomeErrorCode
import com.bolyartech.forge.base.exchange.ForgeExchangeOutcomeOk
import com.bolyartech.forge.base.exchange.SessionForgeExchangeExecutor
import com.bolyartech.forge.base.exchange.forge.ForgeExchangeHelper
import com.bolyartech.forge.base.rc_task.AbstractRcTask
import com.bolyartech.forge.base.rc_task.RcTask
import com.bolyartech.forge.base.rc_task.RcTaskResult
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import org.slf4j.LoggerFactory
import javax.inject.Inject

interface ListAdminUsersTask : RcTask<RcTaskResult<List<AdminUserExportedView>, Int>>

class ListAdminUsersTaskImpl @Inject constructor(
    private val forgeExchangeHelper: ForgeExchangeHelper,
    private val sessionForgeExchangeExecutor: SessionForgeExchangeExecutor
) : ListAdminUsersTask, AbstractRcTask<RcTaskResult<List<AdminUserExportedView>, Int>>(TaskIds.ADMIN_USERS_LIST) {

    private val logger = LoggerFactory.getLogger(this.javaClass)


    private val ENDPOINT = "user_list"

    private val gson = Gson()

    override fun execute() {
        val b = forgeExchangeHelper.createForgeGetHttpExchangeBuilder(ENDPOINT)

        when (val outcome = sessionForgeExchangeExecutor.execute(b.build())) {
            is ForgeExchangeOutcomeOk -> {
                val users: List<AdminUserExportedView> = try {
                    gson.fromJson<List<AdminUserExportedView>>(
                        outcome.payload,
                        object : TypeToken<List<AdminUserExportedView>>() {}.type
                    )
                } catch (e: JsonParseException) {
                    logger.error("Cannot parse JSON")
                    setTaskResult(RcTaskResult.createErrorResult(-1))
                    return
                }

                setTaskResult(RcTaskResult.createSuccessResult(users))
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