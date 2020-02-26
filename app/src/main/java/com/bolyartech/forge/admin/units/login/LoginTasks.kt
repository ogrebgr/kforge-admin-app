package com.bolyartech.forge.admin.units.login

import com.bolyartech.forge.admin.misc.LoginPrefs
import com.bolyartech.forge.admin.misc.TaskIds
import com.bolyartech.forge.base.exchange.ForgeExchangeOutcomeError
import com.bolyartech.forge.base.exchange.ForgeExchangeOutcomeErrorCode
import com.bolyartech.forge.base.exchange.ForgeExchangeOutcomeOk
import com.bolyartech.forge.base.exchange.SessionForgeExchangeExecutor
import com.bolyartech.forge.base.exchange.forge.ForgeExchangeHelper
import com.bolyartech.forge.base.rc_task.AbstractRcTask
import com.bolyartech.forge.base.rc_task.RcTask
import com.bolyartech.forge.base.rc_task.RcTaskResult
import com.bolyartech.scram_sasl.client.ScramClientFunctionality
import javax.inject.Inject

interface LoginTask : RcTask<RcTaskResult<String, Int>> {
    fun init(username: String, password: String)
}


class LoginTaskImpl @Inject constructor(
    private val forgeExchangeHelper: ForgeExchangeHelper,
    private val sessionForgeExchangeExecutor: SessionForgeExchangeExecutor,
    private val scramClientFunctionality: ScramClientFunctionality,
    private val loginPrefs: LoginPrefs
) :
    LoginTask, AbstractRcTask<RcTaskResult<String, Int>>(TaskIds.LOGIN1_TASK) {

    private val LOGIN_ENDPOINT = "login"
    private val PARAM_STEP = "step"
    private val PARAM_DATA = "data"

    private lateinit var username: String
    private lateinit var password: String


    override fun init(username: String, password: String) {
        this.username = username
        this.password = password
    }

    override fun execute() {
        val b = forgeExchangeHelper.createForgePostHttpExchangeBuilder(LOGIN_ENDPOINT)
        b.addPostParameter(PARAM_STEP, "1")
        b.addPostParameter(PARAM_DATA, scramClientFunctionality.prepareFirstMessage(username))

        val outcome = sessionForgeExchangeExecutor.execute(b.build())
        if (outcome is ForgeExchangeOutcomeErrorCode) {
            setTaskResult(RcTaskResult.createErrorResult(outcome.code))
            return
        } else if (outcome is ForgeExchangeOutcomeError) {
            setTaskResult(RcTaskResult.createErrorResult(-1))
            return
        }

        if (isCancelled) {
            setTaskResult(RcTaskResult.createErrorResult(-1))
            return
        }

        val success = outcome as ForgeExchangeOutcomeOk

        val b2 = forgeExchangeHelper.createForgePostHttpExchangeBuilder(LOGIN_ENDPOINT)
        b2.addPostParameter(PARAM_STEP, "2")
        b2.addPostParameter(PARAM_DATA, scramClientFunctionality.prepareFinalMessage(password, success.payload))
        when (val outcome2 = sessionForgeExchangeExecutor.execute(b2.build())) {
            is ForgeExchangeOutcomeOk -> {
                loginPrefs.storeLoginCredentials(username, password)
                setTaskResult(RcTaskResult.createSuccessResult(null))
            }
            is ForgeExchangeOutcomeErrorCode -> setTaskResult(RcTaskResult.createErrorResult(outcome2.code))
            else -> setTaskResult(RcTaskResult.createErrorResult(-1))
        }
    }
}


