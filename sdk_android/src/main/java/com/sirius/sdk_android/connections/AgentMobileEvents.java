package com.sirius.sdk_android.connections;

import com.sirius.sdk.agent.RemoteParams;
import com.sirius.sdk.agent.connections.BaseAgentConnection;
import com.sirius.sdk.agent.connections.RoutingBatch;

import com.sirius.sdk.base.BaseConnector;
import com.sirius.sdk.base.ListenerConnector;
import com.sirius.sdk.encryption.P2PConnection;
import com.sirius.sdk.errors.sirius_exceptions.SiriusConnectionClosed;
import com.sirius.sdk.errors.sirius_exceptions.SiriusFieldValueError;
import com.sirius.sdk.errors.sirius_exceptions.SiriusInvalidPayloadStructure;
import com.sirius.sdk.errors.sirius_exceptions.SiriusRPCError;
import com.sirius.sdk.messaging.Message;
import com.sirius.sdk.utils.Pair;

import shadow.org.json.JSONArray;
import shadow.org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

/**
 * RPC service.
 * <p>
 * Reactive nature of Smart-Contract design
 */
public class AgentMobileEvents extends BaseAgentConnection {
    Logger log = Logger.getLogger(AgentMobileEvents.class.getName());
    String tunnel;

    public String getBalancingGroup() {
        return balancingGroup;
    }

    String balancingGroup;

    public AgentMobileEvents(String serverAddress, byte[] credentials, P2PConnection p2p, int timeout) {
        super(serverAddress, credentials, p2p, timeout);
    }

    @Override
    public ListenerConnector getConnector() {
        return (ListenerConnector)super.getConnector();
    }

    @Override
    public BaseConnector createConnector() {
        return  new ListenerConnector(this.getTimeout(), StandardCharsets.UTF_8, getServerAddress(), path(), getCredentials());
    }

    @Override
    public Object remoteCall(String msgType, RemoteParams params, boolean waitResponse) throws Exception {
        return null;
    }

    @Override
    public void startProtocolWithThreads(List<String> threads, int timeToLiveSec) {

    }

    @Override
    public void stopProtocolWithThreads(List<String> threads, boolean offResponse) {

    }

    @Override
    public void startProtocolWithThreading(String thid, int timeToLiveSec) {

    }

    @Override
    public void stopProtocolWithThreading(String thid, boolean offResponse) {

    }

    @Override
    public void startProtocolForP2P(String senderVerkey, String recipientVerkey, List<String> protocols, int timeToLiveSec) {

    }

    @Override
    public void stopProtocolForP2P(String senderVerkey, String recipientVerkey, List<String> protocols, boolean offResponse) {

    }

    @Override
    public Message readProtocolMessage() throws SiriusInvalidPayloadStructure {
        return null;
    }

    @Override
    public Message sendMessage(Message message, List<String> their_vk, String endpoint, String myVk, List<String> routingKeys, boolean coprotocol) throws SiriusConnectionClosed, SiriusRPCError, SiriusInvalidPayloadStructure {
        return null;
    }

    @Override
    public List<Pair<Boolean, String>> sendMessageBatched(Message message, List<RoutingBatch> batches) throws SiriusConnectionClosed {
        return null;
    }

    @Override
    public String path() {
        return "events";
    }

    @Override
    public void setup(Message context) {
        super.setup(context);
        // Extract load balancing info
        JSONArray balancing = context.getJSONArrayFromJSON("~balancing", new JSONArray());
        for (int i = 0; i < balancing.length(); i++) {
            JSONObject balance = balancing.getJSONObject(i);
            if ("kafka".equals(balance.getString("id"))) {
                JSONObject jsonObject = balance.getJSONObject("data").getJSONObject("json");
                if(!jsonObject.isNull("group_id")){
                    balancingGroup = jsonObject.getString("group_id");
                }

            }
        }


    }

    public void create() throws SiriusFieldValueError {
    /*    CompletableFuture<byte[]> feat = getConnector().read();
        getConnector().open();
        byte[] payload = new byte[0];
        try {
            payload = feat.get(getTimeout(), TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        String msgString = new String(payload, StandardCharsets.UTF_8);
        //log.log(Level.INFO, "Received message: " + msgString);
        Message context = new Message(msgString);
        if (context.getType()==null){
            throw new SiriusFieldValueError("message @type is empty");
        }
        if(!BaseAgentConnection.MSG_TYPE_CONTEXT.equals(context.getType())){
            throw new SiriusFieldValueError("message @type not equal "+MSG_TYPE_CONTEXT);
        }
        setup(context);*/
    }

    public CompletableFuture<Message> pull() throws SiriusConnectionClosed, SiriusInvalidPayloadStructure {
        if (!getConnector().isOpen()) {
            throw new SiriusConnectionClosed("Open agent connection at first");
        }
        return getConnector().read().thenApply(data -> {
            try {
                JSONObject payload = new JSONObject(new String(data, StandardCharsets.US_ASCII));
                if (payload.has("protected")) {
                    String message = getP2p().unpack(payload.toString());
                    //log.log(Level.INFO, "Received protected message. Unpacked: " + message);
                    return new Message(message);
                } else {
                    //log.log(Level.INFO, "Received message: " + payload);
                    return new Message(payload.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
                //throw new SiriusInvalidPayloadStructure(e.getMessage());
            }
        });
    }
}
