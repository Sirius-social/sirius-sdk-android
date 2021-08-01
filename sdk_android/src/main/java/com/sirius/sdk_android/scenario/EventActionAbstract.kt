package com.sirius.sdk_android.scenario

interface  EventActionAbstract {

    fun accept(id : String)

    fun cancel(id : String, cause: String)
}