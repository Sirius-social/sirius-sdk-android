package com.sirius.sdk_android.helpers

import com.sirius.sdk.hub.MobileContext
import com.sirius.sdk_android.scenario.BaseScenario
import com.sirius.sdk_android.scenario.EventAction
import com.sirius.sdk_android.scenario.EventActionAbstract
import com.sirius.sdk_android.scenario.EventActionListener

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

    fun stopScenario(name : String, id : String, cause : String, eventActionListener: EventActionListener? = null){
        val scenario = getScenarioBy(name)
        scenario?.let {
            if(it is EventActionAbstract){
                Thread(Runnable {
                    it.actionStart(EventAction.cancel,id, cause,eventActionListener)
                }).start()
            }
        }
    }

    fun acceptScenario(name: String, id: String, comment: String? = null, eventActionListener: EventActionListener? = null){
        val scenario = getScenarioBy(name)
        scenario?.let {
            if(it is EventActionAbstract){
                Thread(Runnable {
                    it.actionStart(EventAction.accept,id, comment,eventActionListener)
                }).start()
            }
        }


    }

}