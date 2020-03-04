package com.bolyartech.forge.admin.dagger

import com.bolyartech.forge.admin.units.admin_change_password.AdminChangePasswordTask
import com.bolyartech.forge.admin.units.admin_change_password.AdminChangePasswordTaskImpl
import com.bolyartech.forge.admin.units.admin_create_user.AdminCreateUserTask
import com.bolyartech.forge.admin.units.admin_create_user.AdminCreateUserTaskImpl
import com.bolyartech.forge.admin.units.admin_user_manage.*
import com.bolyartech.forge.admin.units.admin_users.ListAdminUsersTask
import com.bolyartech.forge.admin.units.admin_users.ListAdminUsersTaskImpl
import com.bolyartech.forge.admin.units.login.LoginTask
import com.bolyartech.forge.admin.units.login.LoginTaskImpl
import dagger.Binds
import dagger.Module


@Module
abstract class TaskDaggerModule {

    @Binds
    internal abstract fun provideLoginTask1(impl: LoginTaskImpl): LoginTask

    @Binds
    internal abstract fun provideListAdminUsersTask(impl: ListAdminUsersTaskImpl): ListAdminUsersTask

    @Binds
    internal abstract fun provideLoadAdminUserTask(impl: LoadAdminUserTaskImpl): LoadAdminUserTask

    @Binds
    internal abstract fun provideAdminCreateUserTask(impl: AdminCreateUserTaskImpl): AdminCreateUserTask

    @Binds
    internal abstract fun provideStoreSuperAdminTask(impl: StoreSuperAdminTaskImpl): StoreSuperAdminTask

    @Binds
    internal abstract fun provideStoreAdminUserDisabledTask(impl: StoreAdminUserDisabledTaskImpl): StoreAdminUserDisabledTask

    @Binds
    internal abstract fun provideAdminChangePasswordTask(impl: AdminChangePasswordTaskImpl): AdminChangePasswordTask
}