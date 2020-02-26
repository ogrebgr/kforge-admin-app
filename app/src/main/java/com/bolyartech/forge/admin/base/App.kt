package com.bolyartech.forge.admin.base

import com.bolyartech.forge.admin.dagger.DefaultMyAppDaggerComponentHelper
import com.bolyartech.forge.admin.dagger.DependencyInjector
import com.bolyartech.forge.android.app_unit.UnitApplication

class App : UnitApplication() {
    override fun onCreate() {
        super.onCreate()
        initInjector()
    }

    /**
     * Initializes the injector
     * Unit tests should use empty implementation of this method and return false in order to have a chance to
     * initialize the injector with test configuration
     *
     * @return true if dependency injector was initialized, false otherwise
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected fun initInjector(): Boolean {
        DependencyInjector.init(DefaultMyAppDaggerComponentHelper.create(this))

        return true
    }
}