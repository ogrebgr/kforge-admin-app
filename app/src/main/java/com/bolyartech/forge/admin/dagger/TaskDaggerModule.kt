package com.bolyartech.forge.admin.dagger

import com.bolyartech.forge.admin.units.login.LoginTask
import com.bolyartech.forge.admin.units.login.LoginTaskImpl
import dagger.Binds
import dagger.Module


@Module
abstract class TaskDaggerModule {

    @Binds
    internal abstract fun provideLoginTask1(impl: LoginTaskImpl): LoginTask
}