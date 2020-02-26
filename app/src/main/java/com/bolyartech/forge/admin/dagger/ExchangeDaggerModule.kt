package com.bolyartech.forge.admin.dagger

import com.bolyartech.forge.base.exchange.ResultProducer
import com.bolyartech.forge.base.exchange.forge.ForgeExchangeHelper
import com.bolyartech.forge.base.exchange.forge.ForgeExchangeHelperImpl
import com.bolyartech.forge.base.exchange.forge.ForgeExchangeResult
import com.bolyartech.forge.base.exchange.forge.ForgeHeaderResultProducer
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class ExchangeDaggerModule(private val baseUrl: String) {
    @Provides
    @Named("base url")
    internal fun provideBaseUrl(): String {
        return baseUrl
    }

    @Provides
    @Singleton
    @Named("forge result producer")
    fun provideForgeResultProducer(rp: ForgeHeaderResultProducer):
            ResultProducer<ForgeExchangeResult> {

        return rp
    }


    @Provides
    @Singleton
    internal fun provideForgeExchangeHelper(impl: ForgeExchangeHelperImpl): ForgeExchangeHelper {
        return impl
    }
}