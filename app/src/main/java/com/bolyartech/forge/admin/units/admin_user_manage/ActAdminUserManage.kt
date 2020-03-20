package com.bolyartech.forge.admin.units.admin_user_manage

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.bolyartech.forge.admin.R
import com.bolyartech.forge.admin.base.SessionRctUnitActivity
import com.bolyartech.forge.admin.data.AdminUserExportedView
import com.bolyartech.forge.admin.dialogs.*
import com.bolyartech.forge.admin.misc.LoginPrefs
import com.bolyartech.forge.admin.misc.UserInfoHolder
import com.bolyartech.forge.admin.units.admin_change_password.ActAdminChangePassword
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.android.synthetic.main.act__admin_user_manage__content.*
import kotlinx.android.synthetic.main.rvr__admin_users.tvName
import kotlinx.android.synthetic.main.rvr__admin_users.tvUsername
import org.example.kforgepro.modules.admin.AdminResponseCodes
import javax.inject.Inject

class ActAdminUserManage : SessionRctUnitActivity<ResAdminUserManage>(), DfSessionExpired.Listener {
    @Inject
    internal lateinit var resLazy: dagger.Lazy<ResAdminUserManage>

    @Inject
    internal lateinit var userInfoHolder: UserInfoHolder

    @Inject
    internal lateinit var loginPrefs: LoginPrefs

    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        getDependencyInjector().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act__admin_user_manage)

        val myToolbar: Toolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        myToolbar.setTitle(R.string.act__admin_user_manage__title)
        setSupportActionBar(myToolbar)

        val userJson = intent.getStringExtra(PARAM_USER)
        if (userJson == null || userJson.isEmpty()) {
            throw IllegalArgumentException("Missing PARAM_USER")
        }

        val user = try {
            gson.fromJson(userJson, AdminUserExportedView::class.java)
        } catch (e: JsonSyntaxException) {
            throw IllegalArgumentException("Cannot parse json")
        }

        res.init(user)

        showUser(user)
    }


    private fun showUser(user: AdminUserExportedView) {
        tvUsername.text = user.username
        tvName.text = user.name
        if (user.isSuperUser) {
            tvSuperAdmin.visibility = View.VISIBLE
        } else {
            tvSuperAdmin.visibility = View.GONE
        }

        if (user.isDisabled) {
            tvDisabled.visibility = View.VISIBLE
        } else {
            tvDisabled.visibility = View.GONE
        }

        invalidateOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.act__admin_user_manage__menu, menu)

        if (!userInfoHolder.isSuperAdmin()) {
            val miDisable = menu!!.findItem(R.id.ab_disable)
            miDisable.isVisible = false

            val miSuperAdmin = menu.findItem(R.id.ab_superadmin)
            miSuperAdmin.isVisible = false

            if (res.getUser().username != loginPrefs.getUsername()) {
                val miChPwd = menu.findItem(R.id.ab_change_password)
                miChPwd.isVisible = false
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.ab_refresh -> {
                res.loadUser()
                true
            }

            R.id.ab_superadmin -> {
                res.toggleSuperAdmin()
                true
            }

            R.id.ab_disable -> {
                res.toggleDisable()
                true
            }

            R.id.ab_change_password -> {
                val intent = Intent(this, ActAdminChangePassword::class.java)
                intent.putExtra(ActAdminChangePassword.PARAM_USER_ID, res.getUser().id)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val PARAM_USER = "user"
    }

    override fun createResidentComponent(): ResAdminUserManage {
        return resLazy.get()
    }

    override fun handleResidentEndedState() {
        hideGenericWaitDialog(supportFragmentManager)

        if (res.currentTask.isSuccess) {
            showUser(res.getUser())
        } else {
            if (res.getErrorCode() == AdminResponseCodes.NOT_LOGGED_IN.getCode()) {
                session.logout()
                showSessionExpiredDialog(supportFragmentManager)
            } else {
                showCommErrorDialog(supportFragmentManager)
            }
        }
    }

    override fun handleResidentBusyState() {
        showGenericWaitDialog(supportFragmentManager)
    }

    override fun handleResidentIdleState() {
        hideGenericWaitDialog(supportFragmentManager)
    }

    override fun onDfSessionExpiredClosed() {
        goHome()
    }
}
//