package com.example.mvi_noteapp.framework.presentation.notedetail.state


sealed class CollapsingToolbarState{

    class Collapsed: CollapsingToolbarState(){

        override fun toString(): String {
            return "Collapsed"
        }
    }

    class Expanded: CollapsingToolbarState(){

        override fun toString(): String {
            return "Expanded"
        }
    }
}
