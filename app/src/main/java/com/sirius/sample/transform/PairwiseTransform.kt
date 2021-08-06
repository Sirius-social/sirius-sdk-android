package com.sirius.sample.transform

import com.sirius.sample.models.ui.ItemContacts
import com.sirius.sdk.agent.listener.Event
import com.sirius.sdk.agent.pairwise.Pairwise
import java.util.*

class PairwiseTransform {

    companion object {
        fun pairwiseToItemContacts(pairwise: Pairwise) : ItemContacts{
           return ItemContacts(pairwise.their.did,pairwise.their.label, Date())
        }
    }
}