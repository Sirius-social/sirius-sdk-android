package com.sirius.sample.models.ui.message

abstract class BaseItemMessage {

    enum class MessageType{
        Base,
        Text,
        Connect,
        Offer
    }

    companion object {
        @JvmStatic
        fun getLayoutFromType(type : MessageType) :Int{
            if(type == MessageType.Text ){
                return TextItemMessage().getLayoutRes()
            }
            if(type == MessageType.Connect ){
                return ConnectItemMessage().getLayoutRes()
            }
            if(type == MessageType.Offer ){
                return OfferItemMessage().getLayoutRes()
            }
            return 0
        }
    }
    abstract fun  getLayoutRes() : Int
  //  abstract fun  getAcceptedLayoutRes() : Int
    abstract fun getType() : MessageType

    abstract fun accept()

    abstract fun cancel()

}