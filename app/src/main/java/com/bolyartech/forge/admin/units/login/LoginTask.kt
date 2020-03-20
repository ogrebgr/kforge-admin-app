package com.bolyartech.forge.admin.units.login

import com.bolyartech.forge.admin.misc.LoginPrefs
import com.bolyartech.forge.admin.misc.TaskIds
import com.bolyartech.forge.admin.misc.UserInfoHolder
import com.bolyartech.forge.base.exchange.ForgeExchangeOutcomeError
import com.bolyartech.forge.base.exchange.ForgeExchangeOutcomeErrorCode
import com.bolyartech.forge.base.exchange.ForgeExchangeOutcomeOk
import com.bolyartech.forge.base.exchange.SessionForgeExchangeExecutor
import com.bolyartech.forge.base.exchange.forge.ForgeExchangeHelper
import com.bolyartech.forge.base.rc_task.AbstractRcTask
import com.bolyartech.forge.base.rc_task.RcTask
import com.bolyartech.forge.base.rc_task.RcTaskResult
import com.bolyartech.forge.base.session.Session
import com.bolyartech.scram_sasl.client.ScramClientFunctionality
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import org.example.kforgepro.modules.admin.data.SessionInfoAdmin
import org.slf4j.LoggerFactory
import javax.inject.Inject

interface LoginTask : RcTask<RcTaskResult<String, Int>> {
    fun init(username: String, password: String)
}


class LoginTaskImpl @Inject constructor(
    private val forgeExchangeHelper: ForgeExchangeHelper,
    private val sessionForgeExchangeExecutor: SessionForgeExchangeExecutor,
    private val scramClientFunctionality: ScramClientFunctionality,
    private val loginPrefs: LoginPrefs,
    private val session: Session,
    private val userInfoHolder: UserInfoHolder
) :
    LoginTask, AbstractRcTask<RcTaskResult<String, Int>>(TaskIds.LOGIN_TASK) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    private val LOGIN_ENDPOINT = "login"
    private val PARAM_STEP = "step"
    private val PARAM_DATA = "data"

    private lateinit var username: String
    private lateinit var password: String

    private val gson: Gson = Gson()


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
                val rok: RokLogin = try {
                    gson.fromJson(outcome2.payload, RokLogin::class.java)
                } catch (e: JsonSyntaxException) {
                    logger.error("Cannot parse JSON")
                    setTaskResult(RcTaskResult.createErrorResult(-1))
                    return
                }

                session.startSession(rok.sessionTtl)
                userInfoHolder.setSuperAdmin(rok.isSuperAdmin)
                loginPrefs.storeLoginCredentials(username, password)
                setTaskResult(RcTaskResult.createSuccessResult(null))
            }
            is ForgeExchangeOutcomeErrorCode -> setTaskResult(RcTaskResult.createErrorResult(outcome2.code))
            else -> setTaskResult(RcTaskResult.createErrorResult(-1))
        }
    }

    data class RokLogin(
        @SerializedName("session_ttl") val sessionTtl: Int,
        @SerializedName("session_info") val sessionInfo: SessionInfoAdmin,
        @SerializedName("final_message") val finalMessage: String,
        @SerializedName("is_super_admin") val isSuperAdmin: Boolean
    )
}


