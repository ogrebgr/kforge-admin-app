package com.bolyartech.forge.admin.dagger

import com.bolyartech.forge.admin.units.admin_user_manage.ResAdminUserManage
import com.bolyartech.forge.admin.units.admin_user_manage.ResAdminUserManageImpl
import com.bolyartech.forge.admin.units.admin_users.ResAdminUsers
import com.bolyartech.forge.admin.units.admin_users.ResAdminUsersImpl
import com.bolyartech.forge.admin.units.login.ResLogin
import com.bolyartech.forge.admin.units.login.ResLoginImpl
import com.bolyartech.forge.android.app_unit.UnitManager
import com.bolyartech.forge.android.app_unit.UnitManagerImpl
import com.bolyartech.forge.android.app_unit.rc_task.executor.RcTaskExecutor
import com.bolyartech.forge.android.app_unit.rc_task.executor.ThreadRcTaskExecutor
import com.bolyartech.forge.android.misc.RunOnUiThreadHelper
import com.bolyartech.forge.android.misc.RunOnUiThreadHelperDefault
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class UnitDaggerModule {
    @Binds
    @Singleton
    internal abstract fun provideUnitManager(impl: UnitManagerImpl): UnitManager


    @Binds
    internal abstract fun provideRcTaskExecutor(impl: ThreadRcTaskExecutor): RcTaskExecutor

    @Binds
    internal abstract fun provideRunOnUiThreadHelper(impl: RunOnUiThreadHelperDefault): RunOnUiThreadHelper

    @Binds
    internal abstract fun provideResLogin(impl: ResLoginImpl): ResLogin

    @Binds
    internal abstract fun provideResAdminUsers(impl: ResAdminUsersImpl): ResAdminUsers

    @Binds
    internal abstract fun provideResAdminUserManage(impl: ResAdminUserManageImpl): ResAdminUserManage
}