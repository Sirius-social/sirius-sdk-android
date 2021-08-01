package com.sirius.sdk_android.helpers

import com.sirius.sdk.hub.MobileContext
import com.sirius.sdk_android.scenario.BaseScenario
import com.sirius.sdk_android.scenario.EventActionAbstract

class ScenarioHelper {


    companion object {
        private var scenarioHelper: ScenarioHelper? = null

        @JvmStatic
        fun getInstance(): ScenarioHelper {
            if (scenarioHelper == null) {
                scenarioHelper = ScenarioHelper()
            }
            return scenarioHelper!!
        }
    }

    val scenarioMap : MutableMap<String,BaseScenario> = HashMap()

    lateinit var context: MobileContext


    fun addScenario(name : String,scenario : BaseScenario){
        scenarioMap[name] = scenario
    }

    fun getScenarioBy(name : String) : BaseScenario?{
        return scenarioMap[name]
    }

    fun removeScenario(name : String){
        scenarioMap.remove(name)
    }

    fun stopScenario(name : String, cause : String){
        val scenario = getScenarioBy(name)
        scenario?.stop(cause)
    }

    fun acceptScenario(name : String, id : String){
        val scenario = getScenarioBy(name)
        scenario?.let {
            if(it is EventActionAbstract){
                it.accept(id)
            }
        }


    }

}