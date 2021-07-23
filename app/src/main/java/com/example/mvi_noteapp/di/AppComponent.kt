package com.example.mvi_noteapp.di

import com.example.mvi_noteapp.di.ProductionModule
import com.example.mvi_noteapp.framework.presentation.BaseApplication
import com.example.mvi_noteapp.framework.presentation.MainActivity
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton


@FlowPreview
@ExperimentalCoroutinesApi
@Singleton
@Component(
    modules = [
        AppModule::class,
        ProductionModule::class,
        NoteViewModelModule::class
    ]
)
interface AppComponent{

    @Component.Factory
    interface Factory{

        fun create(@BindsInstance app: BaseApplication): AppComponent
    }

    fun inject(mainActivity: MainActivity)
}