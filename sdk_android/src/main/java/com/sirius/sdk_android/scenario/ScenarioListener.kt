package com.sirius.sdk_android.scenario


interface ScenarioListener{
    fun  onScenarioStart()
    fun onScenarioEnd(success: Boolean, error: String?)
}