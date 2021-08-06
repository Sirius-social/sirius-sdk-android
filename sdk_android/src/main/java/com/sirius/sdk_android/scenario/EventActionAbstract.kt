package com.sirius.sdk_android.scenario

interface  EventActionAbstract {

    fun accept(id : String, comment : String?)

    fun cancel(id : String, cause: String?)
}