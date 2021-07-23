package com.example.mvi_noteapp.framework.presentation

import com.example.mvi_noteapp.business.domain.state.DialogInputCaptureCallback
import com.example.mvi_noteapp.business.domain.state.Response
import com.example.mvi_noteapp.business.domain.state.StateMessageCallback


interface UIController {

    fun displayProgressBar(isDisplayed: Boolean)

    fun hideSoftKeyboard()

    fun displayInputCaptureDialog(title: String, callback: DialogInputCaptureCallback)

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )

}