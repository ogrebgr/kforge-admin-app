package com.bolyartech.forge.admin.units.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.bolyartech.forge.admin.R
import com.bolyartech.forge.admin.base.RctUnitActivity
import com.bolyartech.forge.admin.dialogs.hideGenericWaitDialog
import com.bolyartech.forge.admin.dialogs.showGenericWaitDialog
import com.bolyartech.forge.admin.misc.LoginPrefs
import com.bolyartech.forge.admin.units.admin_users.ActAdminUsers
import com.bolyartech.forge.admin.units.login.ActLogin
import com.bolyartech.forge.base.session.Session
import kotlinx.android.synthetic.main.act__main__content.*
import org.slf4j.LoggerFactory
import javax.inject.Inject


class ActMain : RctUnitActivity<ResMain>() {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    private val ACT_LOGIN = 1

    @Inject
    internal lateinit var loginPrefs: LoginPrefs

    @Inject
    internal lateinit var resLazy: dagger.Lazy<ResMain>

    @Inject
    internal lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        getDependencyInjector().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act__main)

        val myToolbar: Toolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        myToolbar.setTitle(R.string.act__main__title)
        setSupportActionBar(myToolbar)
        myToolbar.setNavigationIcon(R.mipmap.ic_launcher)
//        myToolbar.setNavigationOnClickListener {  }


        btnAdminUsers.setOnClickListener {
            val intent = Intent(this, ActAdminUsers::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val intent = Intent(this, ActLogin::class.java)
            if (loginPrefs.areLoginCredentialsAvailable()) {
                intent.putExtra(ActLogin.PARAM_AUTOLOGIN, 1)
            }

            startActivityForResult(intent, ACT_LOGIN)
        }
    }

    override fun onResume() {
        super.onResume()

        handleLoginState()
    }

    private fun handleLoginState() {
        if (!session.isLoggedIn) {
            logger.debug("NOT logged in")
            vLoggedIn.visibility = View.GONE
            btnLogin.visibility = View.VISIBLE
        } else {
            logger.debug("Still logged in")
            vLoggedIn.visibility = View.VISIBLE
            btnLogin.visibility = View.GONE
        }

        invalidateOptionsMenu()
    }

    override fun onResumeJustCreated() {
        super.onResumeJustCreated()

        if (loginPrefs.areLoginCredentialsAvailable()) {
            val intent = Intent(this, ActLogin::class.java)
            intent.putExtra(ActLogin.PARAM_AUTOLOGIN, 1)
            startActivityForResult(intent, ACT_LOGIN)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.act__main__menu, menu)

        val mi = menu!!.findItem(R.id.ab_logout)

        mi.isEnabled = session.isLoggedIn

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.ab_logout -> {
                res.logout()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun createResidentComponent(): ResMain {
        return resLazy.get()
    }

    override fun handleResidentEndedState() {
        hideGenericWaitDialog(supportFragmentManager)
        handleLoginState()
    }

    override fun handleResidentBusyState() {
        showGenericWaitDialog(supportFragmentManager)
    }

    override fun handleResidentIdleState() {
        hideGenericWaitDialog(supportFragmentManager)
    }
}
