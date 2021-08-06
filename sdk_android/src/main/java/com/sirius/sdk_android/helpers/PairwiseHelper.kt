package com.sirius.sdk_android.helpers


import android.util.Log
import com.google.gson.Gson
import com.sirius.sdk.agent.pairwise.Pairwise

import com.sirius.sdk.agent.pairwise.WalletPairwiseList
import com.sirius.sdk.hub.MobileContext
import com.sirius.sdk.messaging.Message
import com.sirius.sdk_android.SiriusSDK
import com.sirius.sdk_android.models.CredentialsRecord
import org.hyperledger.indy.sdk.anoncreds.CredentialsSearch
import org.hyperledger.indy.sdk.anoncreds.CredentialsSearchForProofReq

import shadow.org.json.JSONObject


class PairwiseHelper {

    companion object {
        private var pairwiseHelper: PairwiseHelper? = null

        @JvmStatic
        fun getInstance(): PairwiseHelper {
            if (pairwiseHelper == null) {
                pairwiseHelper = PairwiseHelper()
            }
            return pairwiseHelper!!
        }
    }


    fun getAllPairwise(): List<Pairwise> {
        val list =
            (SiriusSDK.getInstance().context.currentHub.agent.wallet.pairwise.listPairwise() as? List<String>).orEmpty()
        val mutableList: MutableList<Pairwise> = mutableListOf()
        list.forEach {
            val pairwiseObj = JSONObject(it)
            val metadata = pairwiseObj.optString("metadata")
            metadata?.let { metadataObj ->
                val pairwise = WalletPairwiseList.restorePairwise(JSONObject(metadataObj))
                mutableList.add(pairwise)
            }

        }

        return mutableList
    }


    fun getAllCredentials(): List<CredentialsRecord> {
        val list =
            (SiriusSDK.getInstance().context.currentHub.agent.wallet.anoncreds.proverGetCredentials("{}"))
        return list.mapNotNull {
            Gson().fromJson(it, CredentialsRecord::class.java)
        }
    }


    fun getPairwise(theirDid: String? = null, theirVerkey: String? = null): Pairwise? {
        if (!theirDid.isNullOrEmpty()) {
            return SiriusSDK.getInstance().context.currentHub.pairwiseList.loadForDid(theirDid)
        }
        if (!theirVerkey.isNullOrEmpty()) {
            return SiriusSDK.getInstance().context.currentHub.pairwiseList.loadForVerkey(theirVerkey)
        }
        return null;
    }
}