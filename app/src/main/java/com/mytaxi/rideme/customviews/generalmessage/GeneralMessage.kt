package com.mytaxi.rideme.customviews.generalmessage

class GeneralMessage(
    var visible: Boolean = false,
    var type: Type = Type.NONE,
    var text: String = "") {

    enum class Type {
        NONE,
        SUCCESS,
        ERROR
    }

    fun setErrorMessage(text: String) {
        setMessage(text, Type.ERROR)
    }

    fun setSuccessMessage(text: String) {
        setMessage(text, Type.SUCCESS)
    }

    private fun setMessage(text: String, type: Type) {
        visible = true
        this.text = text
        this.type = type
    }

    fun getMessage(): String {
         return text
    }
}