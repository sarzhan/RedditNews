package com.ysar.redditnews

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.ysar.redditnews.di.component.AppComponent
import com.ysar.redditnews.di.component.AppComponentProvider
import com.ysar.redditnews.di.component.DaggerAppComponent


class MainApplication : Application(), AppComponentProvider {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)

        appComponent = DaggerAppComponent.factory().create(this).also {
            it.inject(this) }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun provideAppComponent(): AppComponent = appComponent

}