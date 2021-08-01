package com.sirius.sample.service

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.neovisionaries.ws.client.*
import com.sirius.sample.service.ChannelMessageWrapper.WIRED_CONTENT_TYPE
import com.sirius.sdk_android.helpers.ChanelHelper


class SiriusWebSocketListener() : WebSocketListener {
    val TAG = "CHANNEL_SOCKET"

    @Throws(Exception::class)
    override fun onTextMessage(websocket: WebSocket, data: ByteArray) {
        Log.d(TAG, "onTextMessage websocket=$websocket data=$data")
    }

    @Throws(Exception::class)
    override fun onStateChanged(websocket: WebSocket, newState: WebSocketState) {
    }

    @Throws(Exception::class)
    override fun onConnected(websocket: WebSocket, headers: Map<String, List<String>>) {
        Log.d(TAG, "onConnected websocket=$websocket headers=$headers")
    }

    @Throws(Exception::class)
    override fun onConnectError(websocket: WebSocket, exception: WebSocketException) {
        Log.d(TAG, "onConnectError websocket=$websocket exception=$exception")
    }

    @Throws(Exception::class)
    override fun onDisconnected(
        websocket: WebSocket,
        serverCloseFrame: WebSocketFrame,
        clientCloseFrame: WebSocketFrame,
        closedByServer: Boolean
    ) {
        Log.d(TAG, "onDisconnected websocket=$websocket closedByServer=$closedByServer")
    }

    @Throws(Exception::class)
    override fun onFrame(websocket: WebSocket, frame: WebSocketFrame) {
        Log.d(TAG, "onFrame websocket=$websocket frame=$frame")
    }

    @Throws(Exception::class)
    override fun onContinuationFrame(websocket: WebSocket, frame: WebSocketFrame) {
        Log.d(TAG, "onContinuationFrame websocket=$websocket frame=$frame")
    }

    @Throws(Exception::class)
    override fun onError(websocket: WebSocket, cause: WebSocketException) {
        Log.d("mylog500", "onError websocket=$websocket cause=$cause")
    }

    @Throws(Exception::class)
    override fun onFrameError(
        websocket: WebSocket,
        cause: WebSocketException,
        frame: WebSocketFrame
    ) {
        Log.d(TAG, "onFrameError websocket=$websocket frame=$frame")
    }

    @Throws(Exception::class)
    override fun onMessageError(
        websocket: WebSocket,
        cause: WebSocketException,
        frames: List<WebSocketFrame>
    ) {
    }

    @Throws(Exception::class)
    override fun onMessageDecompressionError(
        websocket: WebSocket,
        cause: WebSocketException,
        compressed: ByteArray
    ) {
    }

    @Throws(Exception::class)
    override fun onBinaryMessage(websocket: WebSocket, binary: ByteArray) {
       val payloadText =  String(binary)
        Log.d(
            TAG,
            "onBinaryMessage websocket=$websocket frame=$payloadText"
        )
        val messageWrapper = parseSocketMessage(payloadText)
        Log.d("mylog2090","messageWrapper?.contentType="+messageWrapper?.contentType);
        if(messageWrapper?.topic == "indy.transport"){
          // WebSocketService.agent.receiveMsg(binary)
            Log.d("mylog2090","messageWrapper?.messageString="+messageWrapper?.messageString);
            ChanelHelper.getInstance().parseMessage(messageWrapper?.messageFromMessageString ?: "")
        }
    }

    @Throws(Exception::class)
    override fun onSendingFrame(websocket: WebSocket, frame: WebSocketFrame) {
    }

    @Throws(Exception::class)
    override fun onFrameSent(websocket: WebSocket, frame: WebSocketFrame) {
    }

    @Throws(Exception::class)
    override fun onFrameUnsent(websocket: WebSocket, frame: WebSocketFrame) {
    }

    @Throws(Exception::class)
    override fun onThreadCreated(websocket: WebSocket, threadType: ThreadType, thread: Thread) {
    }

    @Throws(Exception::class)
    override fun onThreadStarted(websocket: WebSocket, threadType: ThreadType, thread: Thread) {
    }

    @Throws(Exception::class)
    override fun onThreadStopping(websocket: WebSocket, threadType: ThreadType, thread: Thread) {
    }

    @Throws(Exception::class)
    override fun onTextMessageError(
        websocket: WebSocket,
        cause: WebSocketException,
        data: ByteArray
    ) {
        Log.d(TAG, "onTextMessageError websocket=$cause cause=$data data")
    }

    @Throws(Exception::class)
    override fun onSendError(
        websocket: WebSocket,
        cause: WebSocketException,
        frame: WebSocketFrame
    ) {
    }

    @Throws(Exception::class)
    override fun onUnexpectedError(websocket: WebSocket, cause: WebSocketException) {
        Log.d(TAG, "onUnexpectedError websocket=$websocket cause=$cause")
    }

    @Throws(Exception::class)
    override fun handleCallbackError(websocket: WebSocket, cause: Throwable) {
    }

    @Throws(Exception::class)
    override fun onSendingHandshake(
        websocket: WebSocket,
        requestLine: String,
        headers: List<Array<String>>
    ) {
    }

    @Throws(Exception::class)
    override fun onTextFrame(websocket: WebSocket, frame: WebSocketFrame) {
        Log.d(
            TAG,
            "onTextFrame websocket=$websocket frame=$frame frame.payloadText=${frame.payloadText}"
        )
        val payloadText = frame.payloadText
        val messageWrapper = parseSocketMessage(payloadText)
        Log.d("mylog2090","messageWrapper?.contentType="+messageWrapper?.contentType);
        if(messageWrapper?.topic == "indy.transport"){

            Log.d("mylog2090","messageWrapper?.messageString="+messageWrapper?.messageString);
            ChanelHelper.getInstance().parseMessage(messageWrapper?.messageFromMessageString ?: "")
        }
    }

    fun parseSocketMessage(messagePayload: String): ChannelMessageWrapper? {
        try {
            val gson = GsonBuilder().create()
            val jelem: JsonElement = gson.fromJson(messagePayload, JsonElement::class.java)
            val jobj = jelem.asJsonObject
            if(jobj.has("protected")){
                val meta = ChannelMessageWrapper.MessageWrapperMeta()
                return ChannelMessageWrapper("indy.transport", messagePayload, WIRED_CONTENT_TYPE, null, meta)
            }else{
                val topic = jobj["topic"].asString ?: ""
                val messageStrig = jobj["event"].toString()
                val did: String? = if (jobj.has("did")) jobj["did"].asString else null
                val contentType: String =
                    if (jobj.has("content_type")) jobj["content_type"].asString else ""
                val meta = ChannelMessageWrapper.MessageWrapperMeta()
                try {
                    val metadata = if (jobj.has("meta")) {
                        if (!jobj["meta"].isJsonNull) {
                            jobj["meta"].asJsonObject
                        } else {
                            JsonObject()
                        }
                    } else JsonObject()
                    meta.uid = if (metadata.has("uid")) metadata["uid"].asString else null
                    meta.utc = if (metadata.has("utc")) metadata["utc"].asDouble else null
                    meta.content_type =
                        if (metadata.has("content_type")) metadata["content_type"].asString else null
                    meta.session_id =
                        if (metadata.has("session_id")) metadata["session_id"].asString else null
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return ChannelMessageWrapper(topic, messageStrig, contentType, did, meta)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @Throws(Exception::class)
    override fun onBinaryFrame(websocket: WebSocket, frame: WebSocketFrame) {
    }

    @Throws(Exception::class)
    override fun onCloseFrame(websocket: WebSocket, frame: WebSocketFrame) {
    }

    @Throws(Exception::class)
    override fun onPingFrame(websocket: WebSocket, frame: WebSocketFrame) {
    }

    @Throws(Exception::class)
    override fun onPongFrame(websocket: WebSocket, frame: WebSocketFrame) {
    }

    @Throws(Exception::class)
    override fun onTextMessage(websocket: WebSocket, text: String) {
    }
}