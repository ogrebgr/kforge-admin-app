package com.bolyartech.forge.admin.base

import android.content.Context
import android.content.SharedPreferences

interface AppPrefs {
    fun getAppId(): String?
    fun setAppId(id: String)
}


class AppPrefsImpl(ctx: Context) : AppPrefs {
    private val PREFS_FILENAME = "prefs"

    private val KEY_APP_ID = "app id"

    private val prefs: SharedPreferences

    private var _appId: String?

    init {
        prefs = ctx.getSharedPreferences(
            PREFS_FILENAME,
            Context.MODE_PRIVATE
        )
        _appId = prefs.getString(KEY_APP_ID, null)
    }

    override fun getAppId(): String? {
        return _appId
    }

    override fun setAppId(id: String) {
        _appId = id
        val ed = prefs.edit()
        ed.putString(KEY_APP_ID, id)
        ed.apply()
    }
}