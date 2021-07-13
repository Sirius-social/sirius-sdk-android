package com.sirius.sdk_android.walletUseCase



import com.sirius.sdk.agent.pairwise.Pairwise

import com.sirius.sdk.agent.pairwise.WalletPairwiseList
import com.sirius.sdk.hub.MobileContext

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

    lateinit var context: MobileContext


    fun getAllPairwise() : List<Pairwise>{
        val list = (context.currentHub.agent.wallet.pairwise.listPairwise() as? List<String>).orEmpty()
        val mutableList : MutableList<Pairwise> = mutableListOf()
        list.forEach {
           val pairwiseObj  =  JSONObject(it)
           val metadata =  pairwiseObj.optString("metadata")
            metadata?.let { metadataObj->
                val pairwise = WalletPairwiseList.restorePairwise(JSONObject(metadataObj))
                mutableList.add(pairwise)
            }

        }

       return mutableList
    }

    fun sendMessageTo(message : String,pairwise: Pairwise){
        val mess = com.sirius.sdk.agent.aries_rfc.feature_0095_basic_message.Message.builder().setContent(message).setLocale("ru").build()
        context.sendTo(mess,pairwise)
    }
}