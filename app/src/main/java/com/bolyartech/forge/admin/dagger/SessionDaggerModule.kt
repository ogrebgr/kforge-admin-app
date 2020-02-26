package com.bolyartech.forge.admin.dagger

import com.bolyartech.forge.base.exchange.SessionForgeExchangeExecutor
import com.bolyartech.forge.base.exchange.SessionForgeExchangeExecutorImpl
import com.bolyartech.forge.base.exchange.SessionSimpleExchangeExecutor
import com.bolyartech.forge.base.exchange.SessionSimpleExchangeExecutorImpl
import com.bolyartech.forge.base.session.Session
import com.bolyartech.forge.base.session.SessionImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class SessionDaggerModule {
    @Binds
    @Singleton
    internal abstract fun provideSession(session: SessionImpl): Session


    @Binds
    @Singleton
    internal abstract fun provideSessionForgeExchangeExecutor(impl: SessionForgeExchangeExecutorImpl):
            SessionForgeExchangeExecutor

    @Binds
    @Singleton
    internal abstract fun provideSessionSimpleExchangeExecutor(impl: SessionSimpleExchangeExecutorImpl): SessionSimpleExchangeExecutor
}