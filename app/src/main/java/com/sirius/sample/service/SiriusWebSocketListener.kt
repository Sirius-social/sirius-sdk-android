package com.sirius.sample.service

import android.util.Log
import com.neovisionaries.ws.client.*



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
    override fun onDisconnected(websocket: WebSocket, serverCloseFrame: WebSocketFrame, clientCloseFrame: WebSocketFrame, closedByServer: Boolean) {
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
    override fun onFrameError(websocket: WebSocket, cause: WebSocketException, frame: WebSocketFrame) {
        Log.d(TAG, "onFrameError websocket=$websocket frame=$frame")
    }

    @Throws(Exception::class)
    override fun onMessageError(websocket: WebSocket, cause: WebSocketException, frames: List<WebSocketFrame>) {
    }

    @Throws(Exception::class)
    override fun onMessageDecompressionError(websocket: WebSocket, cause: WebSocketException, compressed: ByteArray) {
    }

    @Throws(Exception::class)
    override fun onBinaryMessage(websocket: WebSocket, binary: ByteArray) {
        Log.d(TAG, "onBinaryMessage websocket=$websocket binary=$binary")
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
    override fun onTextMessageError(websocket: WebSocket, cause: WebSocketException, data: ByteArray) {
        Log.d(TAG, "onTextMessageError websocket=$cause cause=$data data")
    }

    @Throws(Exception::class)
    override fun onSendError(websocket: WebSocket, cause: WebSocketException, frame: WebSocketFrame) {
    }

    @Throws(Exception::class)
    override fun onUnexpectedError(websocket: WebSocket, cause: WebSocketException) {
        Log.d(TAG, "onUnexpectedError websocket=$websocket cause=$cause")
    }

    @Throws(Exception::class)
    override fun handleCallbackError(websocket: WebSocket, cause: Throwable) {
    }

    @Throws(Exception::class)
    override fun onSendingHandshake(websocket: WebSocket, requestLine: String, headers: List<Array<String>>) {
    }

    @Throws(Exception::class)
    override fun onTextFrame(websocket: WebSocket, frame: WebSocketFrame) {
        Log.d(TAG, "onTextFrame websocket=$websocket frame=$frame frame.payloadText=${frame.payloadText}")
        val payloadText = frame.payloadText

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