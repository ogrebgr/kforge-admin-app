package com.bolyartech.forge.admin.units.admin_create_user

import android.app.Activity
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
import kotlinx.android.synthetic.main.act__admin_create_user__content.*
import kotlinx.android.synthetic.main.act__login__content.etPassword
import kotlinx.android.synthetic.main.act__login__content.etUsername
import org.example.kforgepro.modules.admin.AdminResponseCodes
import javax.inject.Inject


class ActAdminCreateUser : SessionRctUnitActivity<ResAdminCreateUser>() {

    @Inject
    internal lateinit var resLazy: dagger.Lazy<ResAdminCreateUser>


    override fun onCreate(savedInstanceState: Bundle?) {
        getDependencyInjector().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act__admin_create_user)

        val myToolbar: Toolbar = findViewById<View>(R.id.my_toolbar) as Toolbar
        myToolbar.setTitle(R.string.act__admin_user_create__title)
        setSupportActionBar(myToolbar)
    }

    override fun createResidentComponent(): ResAdminCreateUser {
        return resLazy.get()
    }

    override fun handleResidentEndedState() {
        hideGenericWaitDialog(supportFragmentManager)

        if (res.currentTask.isSuccess) {
            setResult(Activity.RESULT_OK)
            finish()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.act__admin_user_create__menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.ab_save -> {
                if (checkForm()) {
                    res.createUser(
                        etUsername.text.toString(),
                        etPassword.text.toString(),
                        etName.text.toString(),
                        cbSuperAdmin.isChecked
                    )
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


    }

    private fun checkForm(): Boolean {
        if (!AdminUserExportedView.isValidUsername(etUsername.text.toString())) {
            etUsername.error = getString(R.string.act__admin_user_create__et_username_error)
        }

        if (!AdminUserExportedView.isValidPasswordLength(etPassword.text.toString().length)) {
            etPassword.error = getString(
                R.string.act__admin_user_create__et_password_error_short,
                AdminUserExportedView.PASSWORD_MIN_LENGTH.toString()
            )
        }

        if (!etPassword.text.toString().equals(etPassword2.text.toString())) {
            etPassword.error = getString(R.string.act__admin_user_create__et_password_error_mismatch)
            etPassword.setText("")
            etPassword2.setText("")
        }


        return true
    }
}
