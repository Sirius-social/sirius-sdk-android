package com.sirius.sample.models.ui.message

class OfferItemMessage : BaseItemMessage() {
    override fun getLayoutRes(): Int {
        return 0
    }

    override fun getType(): MessageType {
        return MessageType.Offer
    }

    override fun accept() {

    }

    override fun cancel() {

    }
}