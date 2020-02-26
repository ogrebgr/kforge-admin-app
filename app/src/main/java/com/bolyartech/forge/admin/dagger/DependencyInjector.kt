package com.bolyartech.forge.admin.dagger

object DependencyInjector {
    lateinit var injector: AppDaggerComponent

    fun init(inj: AppDaggerComponent) {
        injector = inj
    }

}