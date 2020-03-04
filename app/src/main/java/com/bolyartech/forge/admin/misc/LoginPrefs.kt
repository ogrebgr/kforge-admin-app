package com.bolyartech.forge.admin.misc

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.bolyartech.forge.admin.dagger.ForApplication
import javax.inject.Inject

interface LoginPrefs {
    fun getUsername(): String?

    fun storeLoginCredentials(username: String, password: String)

    fun getPassword(): String?

    fun areLoginCredentialsAvailable(): Boolean

    fun clearLoginCredential()
}


class LoginPrefsImpl @Inject constructor(@ForApplication ctx: Context) : LoginPrefs {
    private val PREFS_FILENAME = "enc_prefs"
    private val KEY_USERNAME = "Username"
    private val KEY_PASSWORD = "Password"

    private val prefs: SharedPreferences

    init {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        prefs = EncryptedSharedPreferences.create(
            PREFS_FILENAME,
            masterKeyAlias,
            ctx,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )


    }

    override fun getUsername(): String? {
        return prefs.getString(KEY_USERNAME, null)
    }

    override fun storeLoginCredentials(username: String, password: String) {
        val ed = prefs.edit()
        ed.putString(KEY_USERNAME, username)
        ed.putString(KEY_PASSWORD, password)
        ed.apply()
    }

    override fun getPassword(): String? {
        return prefs.getString(KEY_PASSWORD, null)
    }

    override fun areLoginCredentialsAvailable(): Boolean {
        return getUsername() != null
    }

    override fun clearLoginCredential() {
        val ed = prefs.edit()
        ed.putString(KEY_USERNAME, null)
        ed.putString(KEY_PASSWORD, null)
        ed.apply()
    }
}