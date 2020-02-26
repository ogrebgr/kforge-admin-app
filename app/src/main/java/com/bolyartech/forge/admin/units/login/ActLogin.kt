package com.bolyartech.forge.admin.units.login

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.bolyartech.forge.admin.R
import com.bolyartech.forge.admin.base.PerformsLogin
import com.bolyartech.forge.admin.base.RctUnitActivity
import com.bolyartech.forge.admin.dialogs.hideGenericWaitDialog
import com.bolyartech.forge.admin.dialogs.showGenericWaitDialog
import com.bolyartech.forge.admin.misc.LoginPrefs
import kotlinx.android.synthetic.main.act__login__content.*
import javax.inject.Inject
import javax.inject.Provider

class ActLogin : PerformsLogin, RctUnitActivity<ResLogin>() {

    @Inject
    internal lateinit var resProvider: Provider<ResLogin>

    @Inject
    internal lateinit var loginPrefs: LoginPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        getDependencyInjector().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act__login)

        val myToolbar: Toolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        myToolbar.setTitle(R.string.act__login__title)
        setSupportActionBar(myToolbar)

        btnLogin.setOnClickListener {
            if (etUsername.text.toString().isEmpty()) {
                etUsername.error = getString(R.string.act__login__error_username_empty)
            }

            if (etPassword.text.toString().isEmpty()) {
                etPassword.error = getString(R.string.act__login__error_pasword_empty)
            }

            if (etUsername.text.toString().isNotEmpty() && etPassword.text.toString().isNotEmpty()) {
                res.login(etUsername.text.toString(), etPassword.text.toString())
            }
        }
    }

    override fun onResumeJustCreated() {
        super.onResumeJustCreated()

        val autologin = intent.getIntExtra(PARAM_AUTOLOGIN, -1)
        if (autologin != -1) {
            res.login(loginPrefs.getUsername()!!, loginPrefs.getPassword()!!)
        }
    }

    override fun createResidentComponent(): ResLogin {
        return resProvider.get()
    }

    override fun handleResidentEndedState() {
        hideGenericWaitDialog(supportFragmentManager)

        if (res.currentTask.isSuccess) {
            setResult(Activity.RESULT_OK)
        }
        finish()
    }

    override fun handleResidentBusyState() {
        showGenericWaitDialog(supportFragmentManager)
    }

    override fun handleResidentIdleState() {
        hideGenericWaitDialog(supportFragmentManager)
    }


    companion object {
        const val PARAM_AUTOLOGIN = "autologin"
    }
}
