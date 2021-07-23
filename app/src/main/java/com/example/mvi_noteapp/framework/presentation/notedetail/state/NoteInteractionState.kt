package com.example.mvi_noteapp.framework.presentation.notedetail.state



sealed class NoteInteractionState {

    class EditState: NoteInteractionState() {

        override fun toString(): String {
            return "EditState"
        }
    }

    class DefaultState: NoteInteractionState(){

        override fun toString(): String {
            return "DefaultState"
        }
    }
}