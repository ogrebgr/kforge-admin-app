package com.bolyartech.forge.admin.misc

import javax.inject.Inject

interface UserInfoHolder {
    fun setSuperAdmin(isSuperAdmin: Boolean)
    fun isSuperAdmin(): Boolean
}

class UserInfoHolderImpl @Inject constructor() : UserInfoHolder {

    private var isSuperAdmin: Boolean = false

    override fun setSuperAdmin(isSuperAdmin: Boolean) {
        this.isSuperAdmin = isSuperAdmin
    }

    override fun isSuperAdmin(): Boolean {
        return isSuperAdmin
    }
}