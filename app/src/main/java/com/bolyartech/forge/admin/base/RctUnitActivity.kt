package com.bolyartech.forge.admin.base

import android.content.Intent
import android.os.Bundle
import com.bolyartech.forge.android.app_unit.rc_task.RctResidentComponent
import com.bolyartech.forge.android.app_unit.rc_task.TaskExecutionStateful
import com.bolyartech.forge.android.app_unit.rc_task.activity.RctActivity
import com.bolyartech.forge.android.app_unit.rc_task.activity.RctActivityDelegate
import com.bolyartech.forge.android.app_unit.rc_task.activity.RctActivityDelegateImpl
import com.bolyartech.forge.android.misc.ActivityResult
import com.bolyartech.forge.android.misc.RunOnUiThreadHelper
import javax.inject.Inject


abstract class RctUnitActivity<T> : UnitBaseActivity<T>(),
    RctResidentComponent.Listener,
    RctActivity where T : RctResidentComponent, T : TaskExecutionStateful {


    @Inject
    lateinit var runOnUiThreadHelper: RunOnUiThreadHelper

    private lateinit var delegate: RctActivityDelegate

    private var isActivityJustCreated = true


    override fun onResidentTaskExecutionStateChanged() {
        delegate.onResidentTaskExecutionStateChanged()
    }


    override fun handleActivityResult(activityResult: ActivityResult) {
        // empty to free the user of declaring it in every activity even when he does not care about the ActivityResult
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegate = RctActivityDelegateImpl(this, runOnUiThreadHelper)
    }


    override fun onResume() {
        super.onResume()
        delegate.onResume()

        if (isActivityJustCreated) {
            isActivityJustCreated = false
            onResumeJustCreated()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        delegate.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Convenience method which is called when activity is just created. This is the place to start
     * a task (and not in onCreate() or onStarted() because you may miss task ending)
     */
    override fun onResumeJustCreated() {
        // empty
    }
}