package com.example.mvi_noteapp.business.domain.state

/**NOTE: it's new version of Resource class which is from google,
a generic class  that holds a value with its status. */


data class DataState<T>(
    var stateMessage: StateMessage? = null,
    var data: T? = null,
    var stateEvent: StateEvent? = null
) {

    companion object {

        fun <T> error(
            response: Response,
            stateEvent: StateEvent?
        ): DataState<T> {
            return DataState(
                stateMessage = StateMessage(
                    response
                ),
                data = null,
                stateEvent = stateEvent
            )
        }

        fun <T> data(
            response: Response?,
            data: T? = null,
            stateEvent: StateEvent?
        ): DataState<T> {
            return DataState(
                stateMessage = response?.let {
                    StateMessage(
                        it
                    )
                },
                data = data,
                stateEvent = stateEvent
            )
        }
    }
}