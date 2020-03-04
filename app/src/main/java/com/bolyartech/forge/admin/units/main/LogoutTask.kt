package com.bolyartech.forge.admin.units.main

import com.bolyartech.forge.admin.misc.LoginPrefs
import com.bolyartech.forge.admin.misc.TaskIds.Companion.LOGOUT
import com.bolyartech.forge.base.exchange.forge.ForgeExchangeHelper
import com.bolyartech.forge.base.rc_task.simple.AbstractDeadSimpleRcTask
import com.bolyartech.forge.base.rc_task.simple.DeadSimpleRcTask
import com.bolyartech.forge.base.session.Session
import javax.inject.Inject

interface LogoutTask : DeadSimpleRcTask

class LogoutTaskImpl @Inject constructor(
    private val forgeExchangeHelper: ForgeExchangeHelper,
    private val session: Session,
    private val loginPrefs: LoginPrefs
) : LogoutTask, AbstractDeadSimpleRcTask(LOGOUT) {

    private val ENDPOINT = "logout"

    override fun execute() {
        val b = forgeExchangeHelper.createForgeGetHttpExchangeBuilder(ENDPOINT)
        b.build().execute()
        session.logout()
        loginPrefs.clearLoginCredential()
        setDeadSimpleTaskResult(null)
    }
}