package com.bolyartech.forge.admin.dagger

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
}