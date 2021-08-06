package com.sirius.sample.ui.chats.message


import android.util.Log
import com.sirius.sample.models.ui.ItemCredentialsDetails
import com.sirius.sdk.agent.aries_rfc.concept_0017_attachments.Attach
import com.sirius.sdk.agent.aries_rfc.feature_0036_issue_credential.messages.ProposedAttrib
import com.sirius.sdk.agent.aries_rfc.feature_0037_present_proof.messages.RequestPresentationMessage
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk_android.helpers.ScenarioHelper
import org.json.JSONObject
import java.util.*

class ProverItemMessage(event: Event) : BaseItemMessage(event) {


    override fun getType(): MessageType {
        return if (isAccepted) {
            MessageType.ProverAccepted
        } else {
            MessageType.Prover
        }
    }

    var hint: String? = null
    var expiresTime: Date? = null
    var detailList: List<ItemCredentialsDetails>? = null
    var name: String? = null
    override fun setupFromEvent(event: Event?) {
        super.setupFromEvent(event)
        val requestPresentationMessage = event?.message() as? RequestPresentationMessage
        expiresTime = requestPresentationMessage?.expiresTime()
        val proofRequest = requestPresentationMessage?.proofRequest().toString()
        val message  = JSONObject(requestPresentationMessage?.serialize())
        val attches = message.optJSONArray("~attach")
        val list = mutableListOf<ProposedAttrib>()
        for (i in 0 until attches.length()) {

            val attachObject = attches.getJSONObject(i)
           val type =  attachObject.optString("@type")

            if(type.contains("credential-translation")){
                val langObject = attachObject.optJSONObject("~l10n")
                val dataObj = attachObject.optJSONObject("data")
               val  dataJson = dataObj.optJSONArray("json")
                for (z in 0 until dataJson.length()) {
                    val attrObj = dataJson.getJSONObject(z)
                    val attrName =  attrObj.optString("attrib_name")
                    val translation = attrObj.optString("translation")
                    list.add(ProposedAttrib(attrName,translation))
                }
            }
        }
        Log.d("mylog200", "proofRequest=" + proofRequest)
        Log.d("mylog200", "event=" + event?.messageObj?.toString())
        hint = requestPresentationMessage?.comment
        val reqObject = JSONObject(proofRequest)
        name = reqObject.optString("name")
        val names = mutableListOf<String>()
        val reqAttr = reqObject.optJSONObject("requested_attributes")
        val iteraror = reqAttr.keys()
        while (iteraror.hasNext()) {
            val key = iteraror.next()
            val attrObject = reqAttr.optJSONObject(key)
            val name = attrObject?.optString("name")
            name?.let {
                var existTranslation = false
                list.forEach {
                    if(it.name == name){
                        if(it.value.isNotEmpty()){
                            names.add(it.value)
                            existTranslation = true
                        }
                    }
                }
                if(!existTranslation){
                    names.add(name)
                }
            }
        }

        detailList = names?.map {
            ItemCredentialsDetails(it, "")
        }
    }


    override fun accept() {
        ScenarioHelper.getInstance().acceptScenario("Prover", message?.id ?: "")
    }

    override fun cancel() {
        ScenarioHelper.getInstance().stopScenario("Prover", message?.id ?: "", "Canceled By Me")
    }

    override fun getText(): String {
        return "offer sample"
    }

    override fun getTitle(): String {
        return name ?: ""
    }
}