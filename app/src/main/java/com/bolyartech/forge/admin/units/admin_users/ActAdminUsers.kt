package com.bolyartech.forge.admin.units.admin_users

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bolyartech.forge.admin.R
import com.bolyartech.forge.admin.base.SessionRctUnitActivity
import com.bolyartech.forge.admin.data.AdminUserExportedView
import com.bolyartech.forge.admin.dialogs.DfSessionExpired
import com.bolyartech.forge.admin.dialogs.hideGenericWaitDialog
import com.bolyartech.forge.admin.dialogs.showGenericWaitDialog
import com.bolyartech.forge.admin.dialogs.showSessionExpiredDialog
import com.bolyartech.forge.admin.units.admin_create_user.ActAdminCreateUser
import com.bolyartech.forge.admin.units.admin_user_manage.ActAdminUserManage
import com.bolyartech.forge.android.misc.ActivityResult
import com.google.gson.Gson
import kotlinx.android.synthetic.main.act__admin_users__content.*
import org.example.kforgepro.modules.admin.AdminResponseCodes
import javax.inject.Inject


class ActAdminUsers : SessionRctUnitActivity<ResAdminUsers>(), DfSessionExpired.Listener,
    AdminUsersAdapter.ClickListener {

    private val ACT_ADMIN_USER_MANAGE = 100
    private val ACT_CREATE_NEW = 101

    @Inject
    internal lateinit var resResAdminUsers: dagger.Lazy<ResAdminUsers>


    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        getDependencyInjector().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act__admin_users)

        val myToolbar: Toolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        myToolbar.setTitle(R.string.act__admin_users__title)
        setSupportActionBar(myToolbar)


        viewManager = LinearLayoutManager(this)
        rvAdminUsers.layoutManager = viewManager

    }

    override fun createResidentComponent(): ResAdminUsers {
        return resResAdminUsers.get()
    }

    override fun handleResidentEndedState() {
        hideGenericWaitDialog(supportFragmentManager)

        if (res.currentTask.isSuccess) {
            viewAdapter = AdminUsersAdapter(res.getAdminUsers(), this)
            rvAdminUsers.adapter = viewAdapter
        } else {
            if (res.getErrorCode() == AdminResponseCodes.NOT_LOGGED_IN.getCode()) {
                session.logout()
                showSessionExpiredDialog(supportFragmentManager)
            }
        }
    }

    override fun handleResidentBusyState() {
        showGenericWaitDialog(supportFragmentManager)
    }

    override fun handleResidentIdleState() {
        hideGenericWaitDialog(supportFragmentManager)
    }

    override fun onResumeJustCreated() {
        super.onResumeJustCreated()
        res.listAdminUsers()
    }

    override fun onDfSessionExpiredClosed() {
        goHome()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.act__admin_users_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.ab_refresh -> {
                res.listAdminUsers()
                true
            }

            R.id.ab_create_new -> {
                val intent = Intent(this, ActAdminCreateUser::class.java)
                startActivityForResult(intent, ACT_CREATE_NEW)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(item: AdminUserExportedView) {
        val intent = Intent(this, ActAdminUserManage::class.java)
        intent.putExtra(ActAdminUserManage.PARAM_USER, gson.toJson(item))
        startActivityForResult(intent, ACT_ADMIN_USER_MANAGE)
    }


    override fun handleActivityResult(activityResult: ActivityResult) {
        super.handleActivityResult(activityResult)
        res.listAdminUsers()
    }
}
