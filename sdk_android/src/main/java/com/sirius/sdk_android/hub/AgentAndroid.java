package com.sirius.sdk_android.hub;

import com.sirius.sdk.agent.Agent;
import com.sirius.sdk.agent.connections.Endpoint;
import com.sirius.sdk.agent.pairwise.Pairwise;
import com.sirius.sdk.encryption.P2PConnection;
import com.sirius.sdk.errors.sirius_exceptions.SiriusRPCError;
import com.sirius.sdk.messaging.Message;
import com.sirius.sdk.storage.abstract_storage.AbstractImmutableCollection;
import com.sirius.sdk.utils.Pair;

import java.util.List;

public class AgentAndroid extends Agent {


    public AgentAndroid(String serverAddress, byte[] credentials, P2PConnection p2p, int timeout, AbstractImmutableCollection storage, String name) {
        super(serverAddress, credentials, p2p, timeout, storage, name);
    }

    public AgentAndroid(String serverAddress, byte[] credentials, P2PConnection p2p, int timeout, AbstractImmutableCollection storage) {
        super(serverAddress, credentials, p2p, timeout, storage);
    }

    public AgentAndroid(String serverAddress, byte[] credentials, P2PConnection p2p, int timeout) {
        super(serverAddress, credentials, p2p, timeout);
    }

    @Override
    public void open() {
        super.open();
    }

    @Override
    public boolean ping() {
        return super.ping();
    }

    @Override
    public Pair<Boolean, Message> sendMessage(Message message, List<String> their_vk, String endpoint, String my_vk, List<String> routing_keys) throws SiriusRPCError {
        return super.sendMessage(message, their_vk, endpoint, my_vk, routing_keys);
    }

    @Override
    public void sendTo(Message message, Pairwise to) throws SiriusRPCError {
        super.sendTo(message, to);
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public List<Endpoint> checkIsOpen() {
        return super.checkIsOpen();
    }
}
