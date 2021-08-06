package com.sirius.sdk_android.models

import com.google.gson.JsonObject
import com.sirius.sdk.agent.aries_rfc.feature_0036_issue_credential.messages.ProposedAttrib
import com.sirius.sdk.base.JsonSerializable
import org.json.JSONObject


class CredentialsRecord {
    var cred_rev_id: String? = null
    var rev_reg_id: String? = null
    var referent: String? = null
    var schema_id: String? = null
    var cred_def_id: String? = null
    var attrs: Map<String, String>? = null


    fun getAttributes(): List<ProposedAttrib> {
        return attrs?.map {
            ProposedAttrib(it.key, it.value, null)
        }.orEmpty()
    }


}