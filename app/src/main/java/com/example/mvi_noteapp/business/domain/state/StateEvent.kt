package com.example.mvi_noteapp.business.domain.state

interface StateEvent {/**this interface has some properties which every StateEvent should have it*/

    fun errorInfo(): String

    fun eventName(): String

    fun shouldDisplayProgressBar(): Boolean
}