package com.bolyartech.forge.admin.units.admin_users

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bolyartech.forge.admin.R
import com.bolyartech.forge.admin.base.SessionRctUnitActivity
import com.bolyartech.forge.admin.dialogs.DfSessionExpired
import com.bolyartech.forge.admin.dialogs.hideGenericWaitDialog
import com.bolyartech.forge.admin.dialogs.showGenericWaitDialog
import com.bolyartech.forge.admin.dialogs.showSessionExpiredDialog
import kotlinx.android.synthetic.main.act__admin_users__content.*
import org.example.kforgepro.modules.admin.AdminResponseCodes
import javax.inject.Inject


class ActAdminUsers : SessionRctUnitActivity<ResAdminUsers>(), DfSessionExpired.Listener {
    @Inject
    internal lateinit var resResAdminUsers: dagger.Lazy<ResAdminUsers>


    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


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
            viewAdapter = AdminUsersAdapter(res.getAdminUsers())
            rvAdminUsers.adapter = viewAdapter
        } else {
            if (res.getErrorCode() == AdminResponseCodes.NOT_LOGGED_IN.getCode()) {
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
}
