package com.bolyartech.forge.admin.dagger

import com.bolyartech.forge.admin.units.admin_user_manage.LoadAdminUserTask
import com.bolyartech.forge.admin.units.admin_user_manage.LoadAdminUserTaskImpl
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
}