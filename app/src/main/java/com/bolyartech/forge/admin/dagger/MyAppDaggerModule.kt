package com.bolyartech.forge.admin.dagger

import android.content.Context
import android.content.pm.PackageManager
import com.bolyartech.forge.admin.base.AppPrefs
import com.bolyartech.forge.admin.base.AppPrefsImpl
import com.bolyartech.forge.admin.misc.LoginPrefs
import com.bolyartech.forge.admin.misc.LoginPrefsImpl
import com.bolyartech.forge.admin.misc.UserInfoHolder
import com.bolyartech.forge.admin.misc.UserInfoHolderImpl
import com.bolyartech.forge.base.misc.TimeProvider
import com.bolyartech.forge.base.misc.TimeProviderImpl
import com.bolyartech.scram_sasl.client.ScramClientFunctionality
import com.bolyartech.scram_sasl.client.ScramClientFunctionalityImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class MyAppDaggerModuleProvides(private val ctx: Context) {
    private val versionCode: String

    private val DIGEST = "SHA-512"
    private val HMAC = "HmacSHA512"

    init {
        try {
            val pInfo = ctx.packageManager.getPackageInfo(ctx.packageName, 0)
            versionCode = pInfo.versionCode.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException(e)
        }

    }


    @Provides
    internal fun provideTimeProvider(): TimeProvider {
        return TimeProviderImpl()
    }

    @Provides
    internal fun provideAppPrefs(): AppPrefs {
        return AppPrefsImpl(ctx)
    }


    @Provides
    @AppVersion
    internal fun provideAppVersion(): String {
        return versionCode
    }

    @Provides
    internal fun provideScramClientFunctionality(): ScramClientFunctionality {
        return ScramClientFunctionalityImpl(DIGEST, HMAC)
    }
}


@Module
abstract class MyAppDaggerModuleBinds {
    @Binds
    internal abstract fun provideLoginPrefs(impl: LoginPrefsImpl): LoginPrefs

    @Binds
    @Singleton
    internal abstract fun bindsUserInfoHolder(impl: UserInfoHolderImpl): UserInfoHolder
}