package com.example.mvi_noteapp.framework.presentation

import android.app.Application
import com.example.mvi_noteapp.di.AppComponent
import com.example.mvi_noteapp.di.DaggerAppComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@ExperimentalCoroutinesApi
open class BaseApplication : Application(){

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
    }

    open fun initAppComponent(){
        appComponent = DaggerAppComponent
            .factory()
            .create(this)
    }


}