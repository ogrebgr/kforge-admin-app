package com.bolyartech.forge.admin.base

import com.bolyartech.forge.android.app_unit.ResidentComponent
import com.bolyartech.forge.android.app_unit.UnitActivity
import com.bolyartech.forge.android.app_unit.UnitActivityDelegate

abstract class UnitBaseActivity<T : ResidentComponent> :
    BaseActivity(), UnitActivity<T> {

    private val delegate = UnitActivityDelegate<T>()

    override fun getResident(): T {
        return delegate.resident
    }


    override fun setResident(resident: T) {
        delegate.resident = resident
    }


    override fun getRes(): T {
        return delegate.res
    }
}
