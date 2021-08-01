package com.sirius.sample.models.ui.message

class TextItemMessage : BaseItemMessage() {
    override fun getLayoutRes(): Int {
        return 0
    }

    override fun getType(): MessageType {
        return MessageType.Text
    }

    override fun accept() {

    }

    override fun cancel() {

    }
}