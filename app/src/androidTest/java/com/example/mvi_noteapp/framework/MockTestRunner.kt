package com.example.mvi_noteapp.framework

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.example.mvi_noteapp.framework.presentation.TestBaseApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class MockTestRunner: AndroidJUnitRunner(){

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ) :  Application {
        return super.newApplication(cl, TestBaseApplication::class.java.name, context)
    }
}