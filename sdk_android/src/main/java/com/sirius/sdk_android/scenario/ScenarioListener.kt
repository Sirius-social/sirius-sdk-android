package com.sirius.sdk_android.scenario


interface ScenarioListener{
    fun  onScenarioStart(id : String)
    fun onScenarioEnd(id : String,success: Boolean, error: String?)
}