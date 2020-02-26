package com.bolyartech.forge.admin.dagger

import android.content.Context
import com.bolyartech.forge.admin.units.login.ActLogin
import com.bolyartech.forge.admin.units.main.ActMain
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Component(
    modules = [CoreModule::class,
        MyAppDaggerModuleProvides::class,
        MyAppDaggerModuleBinds::class,
        UnitDaggerModule::class,
        TaskDaggerModule::class,
        HttpDaggerModule::class,
        ExchangeDaggerModule::class,
        SessionDaggerModule::class]
)
@Singleton
interface AppDaggerComponent {
    fun inject(act: ActMain)
    fun inject(act: ActLogin)
}


@Module
class CoreModule(private val ctx: Context) {
    @Provides
    @ForApplication
    fun provideAppContext(): Context {
        return ctx
    }
}