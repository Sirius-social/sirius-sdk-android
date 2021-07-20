package com.sirius.sdk_android.helpers


import com.sirius.sdk.agent.pairwise.Pairwise

import com.sirius.sdk.agent.pairwise.WalletPairwiseList
import com.sirius.sdk.hub.MobileContext
import com.sirius.sdk_android.SiriusSDK

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

    fun getPairwise(theirDid: String? = null, theirVerkey: String? = null): Pairwise? {
        if (theirDid != null) {
            return SiriusSDK.getInstance().context.currentHub.pairwiseList.loadForDid(theirDid)
        }
        if (theirVerkey != null) {
            return SiriusSDK.getInstance().context.currentHub.pairwiseList.loadForVerkey(theirVerkey)
        }
        return null;
    }



    fun sendMessageTo(message: String, pairwise: Pairwise) {
        val mess = com.sirius.sdk.agent.aries_rfc.feature_0095_basic_message.Message.builder()
            .setContent(message).setLocale("ru").build()
        SiriusSDK.getInstance().context.sendTo(mess, pairwise)
    }
}