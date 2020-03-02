package com.bolyartech.forge.admin.units.admin_user_manage

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.bolyartech.forge.admin.R
import com.bolyartech.forge.admin.base.SessionRctUnitActivity
import com.bolyartech.forge.admin.data.AdminUserExportedView
import com.bolyartech.forge.admin.dialogs.hideGenericWaitDialog
import com.bolyartech.forge.admin.dialogs.showGenericWaitDialog
import com.bolyartech.forge.admin.dialogs.showSessionExpiredDialog
import com.bolyartech.forge.admin.misc.TaskIds
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.android.synthetic.main.act__admin_user_manage__content.*
import kotlinx.android.synthetic.main.rvr__admin_users.tvName
import kotlinx.android.synthetic.main.rvr__admin_users.tvUsername
import org.example.kforgepro.modules.admin.AdminResponseCodes
import javax.inject.Inject

class ActAdminUserManage : SessionRctUnitActivity<ResAdminUserManage>() {
    @Inject
    internal lateinit var resLazy: dagger.Lazy<ResAdminUserManage>

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

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.ab_refresh -> {
                res.loadUser()
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

        when (res.currentTask.id) {
            TaskIds.LOAD_ADMIN_USER_TASK -> {
                if (res.currentTask.isSuccess) {
                    showUser(res.getUser())
                } else {
                    if (res.getErrorCode() == AdminResponseCodes.NOT_LOGGED_IN.getCode()) {
                        session.logout()
                        showSessionExpiredDialog(supportFragmentManager)
                    }
                }
            }
        }
    }

    override fun handleResidentBusyState() {
        showGenericWaitDialog(supportFragmentManager)
    }

    override fun handleResidentIdleState() {
        hideGenericWaitDialog(supportFragmentManager)
    }


}
