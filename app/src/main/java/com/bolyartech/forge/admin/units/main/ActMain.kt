package com.bolyartech.forge.admin.units.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.bolyartech.forge.admin.R
import com.bolyartech.forge.admin.base.SessionActivity
import com.bolyartech.forge.admin.misc.LoginPrefs
import com.bolyartech.forge.admin.units.login.ActLogin
import kotlinx.android.synthetic.main.act__main__content.*
import org.slf4j.LoggerFactory
import javax.inject.Inject


class ActMain : SessionActivity() {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    private val ACT_LOGIN = 1

    @Inject
    internal lateinit var loginPrefs: LoginPrefs


    override fun onCreate(savedInstanceState: Bundle?) {
        getDependencyInjector().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act__main)

        val myToolbar: Toolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        myToolbar.setTitle(R.string.act__main__title)
        setSupportActionBar(myToolbar)


        btnAdminUsers.setOnClickListener {
            //TODO
        }
    }

    override fun onResume() {
        super.onResume()

        if (!session.isLoggedIn) {
            logger.debug("NOT logged in")
            val intent = Intent(this, ActLogin::class.java)
            if (loginPrefs.areLoginCredentialsAvailable()) {
                intent.putExtra(ActLogin.PARAM_AUTOLOGIN, 1)
            }

            startActivityForResult(intent, ACT_LOGIN)
        } else {
            logger.debug("Still logged in")
        }
    }
}
