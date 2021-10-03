package com.ysar.redditnews.di.component

import android.app.Application
import com.ysar.redditnews.MainApplication
import com.ysar.redditnews.di.AppScope
import com.ysar.redditnews.di.module.DatabaseModule
import com.ysar.redditnews.di.module.NetworkModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@AppScope
@Singleton
@Component(modules = [
    DatabaseModule::class,
    NetworkModule::class,
])
interface AppComponent {

    fun inject(app: MainApplication)

    fun mainComponent(): MainComponent.Factory


    @Component.Factory
    interface Factory {

        fun create(@BindsInstance app: Application): AppComponent
    }
}