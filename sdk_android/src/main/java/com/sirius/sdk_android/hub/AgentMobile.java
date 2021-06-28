package com.sirius.sdk_android.hub;

import android.util.Log;

import com.sirius.sdk.agent.AbstractAgent;
import com.sirius.sdk.agent.Agent;
import com.sirius.sdk.agent.RemoteParams;
import com.sirius.sdk.agent.connections.AgentEvents;
import com.sirius.sdk.agent.connections.AgentRPC;
import com.sirius.sdk.agent.connections.BaseAgentConnection;
import com.sirius.sdk.agent.connections.Endpoint;
import com.sirius.sdk.agent.connections.RemoteCallWrapper;
import com.sirius.sdk.agent.coprotocols.PairwiseCoProtocolTransport;
import com.sirius.sdk.agent.coprotocols.TheirEndpointCoProtocolTransport;
import com.sirius.sdk.agent.coprotocols.ThreadBasedCoProtocolTransport;
import com.sirius.sdk.agent.ledger.Ledger;
import com.sirius.sdk.agent.listener.Listener;
import com.sirius.sdk.agent.microledgers.AbstractMicroledgerList;
import com.sirius.sdk.agent.microledgers.MicroledgerList;
import com.sirius.sdk.agent.pairwise.Pairwise;
import com.sirius.sdk.agent.pairwise.TheirEndpoint;
import com.sirius.sdk.agent.pairwise.WalletPairwiseList;
import com.sirius.sdk.agent.storages.InWalletImmutableCollection;
import com.sirius.sdk.agent.wallet.AbstractWallet;
import com.sirius.sdk.encryption.P2PConnection;
import com.sirius.sdk.errors.sirius_exceptions.SiriusConnectionClosed;
import com.sirius.sdk.errors.sirius_exceptions.SiriusFieldValueError;
import com.sirius.sdk.errors.sirius_exceptions.SiriusInvalidPayloadStructure;
import com.sirius.sdk.errors.sirius_exceptions.SiriusRPCError;
import com.sirius.sdk.messaging.Message;
import com.sirius.sdk.storage.abstract_storage.AbstractImmutableCollection;
import com.sirius.sdk.utils.Pair;
import com.sirius.sdk_android.connections.AgentMobileConnection;
import com.sirius.sdk_android.wallet.MobileWallet;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentMobile extends AbstractAgent {

    AgentMobileConnection rpcMobile;
    String endpoint;

    public AgentMobile(String serverAddress, byte[] credentials, P2PConnection p2p, int timeout, AbstractImmutableCollection storage, String name) {
        super(serverAddress, credentials, p2p, timeout, storage, name);
    }

    public AgentMobile(String serverAddress, byte[] credentials, P2PConnection p2p, int timeout, AbstractImmutableCollection storage) {
        super(serverAddress, credentials, p2p, timeout, storage);
    }

    public AgentMobile(String serverAddress, byte[] credentials, P2PConnection p2p, int timeout) {
        super(serverAddress, credentials, p2p, timeout);
    }

    public Message createMessage(String endpoint) {
        String message = "{\"id\":\"d0286a56e30e43708832720228a0c82f\",\"@type\":\"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec\\/sirius_rpc\\/1.0\\/context\",\"~endpoints\":[{\"@type\":\"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec\\/sirius_rpc\\/1.0\\/context\",\"id\":\"router\",\"mime-type\":\"application\\/json\",\"data\":{\"json\":{\"address\":\"https:\\/\\/demo.socialsirius.com\\/endpoint\",\"frontend_routing_key\":\"2JqbvsnmrW8gJUQCdu9Gpavwo1m1MXz8QLbnjKN6aLLS\",\"routing_keys\":[{\"is_default\":true,\"routing_key\":\"GR8fGJWoSXo4abmgKuz7fJ5ZXgLyruMSLnEZxRuKT2Pi\"}]}}},{\"@type\":\"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec\\/sirius_rpc\\/1.0\\/context\",\"id\":\"default\",\"mime-type\":\"application\\/json\",\"data\":{\"json\":{\"address\":\"" + endpoint + "\"}}}],\"~networks\":[\"test_network\"],\"~proxy\":[{\"@type\":\"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec\\/sirius_rpc\\/1.0\\/context\",\"id\":\"reverse\",\"mime-type\":\"application\\/json\",\"data\":{\"json\":{\"address\":\"redis:\\/\\/redis\\/d3dad5a4b7e54c57bd091296cac80995\"}}},{\"@type\":\"did:sov:BzCbsNYhMrjHiqZDTUASHg;spec\\/sirius_rpc\\/1.0\\/context\",\"id\":\"sub-protocol\",\"mime-type\":\"application\\/json\",\"data\":{\"json\":{\"address\":\"redis:\\/\\/redis\\/2f43cbf3948349fe8ad25baa5e9a8a37\"}}}],\"@id\":\"696131cf-cc34-44e7-9b0f-62c300ca3d58\"}";
        return new Message(message);
    }

    @Override
    public void open() {
        // try {
        rpcMobile = new AgentMobileConnection(getServerAddress(), getCredentials(), getP2p(), getTimeout());
        rpcMobile.setup(createMessage(endpoint));
        Log.d("mylog299","agentMobileOpen endpoint="+endpoint);
        // rpcMobile.create();
        setEndpoints(rpcMobile.getEndpoints());
        setWallet(new MobileWallet(rpcMobile));
        if (getStorage() == null) {
            setStorage(new InWalletImmutableCollection(getWallet().getNonSecrets()));
        }
        for (String network : rpcMobile.getNetworks()) {
            getLedgers().put(network, new Ledger(network, getWallet().getLedger(), getWallet().getAnoncreds(), getWallet().getCache(), getStorage()));
        }
        setPairwiseList(new WalletPairwiseList(getWallet().getPairwise(), getWallet().getDid()));
        setMicroledgers(new MicroledgerList(rpcMobile));
      /*  } catch (SiriusFieldValueError siriusFieldValueError) {
            siriusFieldValueError.printStackTrace();
        }*/
    }


    public boolean isOpen() {
        return rpcMobile != null && rpcMobile.isOpen();
    }


    @Override
    public MobileWallet getWallet() {
        return (MobileWallet) super.getWallet();
    }

    public boolean ping() {
        try {
            Object response = rpcMobile.remoteCall("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/sirius_rpc/1.0/ping_agent", null);
            if (response instanceof Boolean) {
                return (boolean) response;
            }
            return false;
        } catch (Exception siriusConnectionClosed) {
            siriusConnectionClosed.printStackTrace();
        }
        return false;
    }

    /**
     * Implementation of basicmessage feature
     * See details:
     * - https://github.com/hyperledger/aries-rfcs/tree/master/features/0095-basic-message
     *
     * @param message      Message
     *                     See details:
     *                     - https://github.com/hyperledger/aries-rfcs/tree/master/concepts/0020-message-types
     * @param their_vk     Verkey of recipient
     * @param endpoint     Endpoint address of recipient
     * @param my_vk        VerKey of Sender (AuthCrypt mode)
     *                     See details:
     *                     - https://github.com/hyperledger/aries-rfcs/tree/master/features/0019-encryption-envelope#authcrypt-mode-vs-anoncrypt-mode
     * @param routing_keys Routing key of recipient
     * @return
     */
    public Pair<Boolean, Message> sendMessage(Message message, List<String> their_vk,
                                              String endpoint, String my_vk, List<String> routing_keys) throws SiriusRPCError {
        checkIsOpen();
        try {
            Message message1 = rpcMobile.sendMessage(message, their_vk, endpoint, my_vk, routing_keys, false);
            return new Pair<>(true, message1);
        } catch (SiriusConnectionClosed siriusConnectionClosed) {
            siriusConnectionClosed.printStackTrace();
        } catch (SiriusInvalidPayloadStructure siriusInvalidPayloadStructure) {
            siriusInvalidPayloadStructure.printStackTrace();
        }
        return new Pair<>(false, null);
    }

    public void sendTo(Message message, Pairwise to) throws SiriusRPCError {
        sendMessage(message, Collections.singletonList(to.getTheir().getVerkey()), to.getTheir().getEndpoint(), to.getMe().getVerkey(), to.getTheir().getRoutingKeys());
    }


    public void close() {
        if (rpcMobile != null) {
            rpcMobile.close();
        }
       /* if (events != null) {
            events.close();
        }
        wallet = null;*/
    }


    public List<Endpoint> checkIsOpen() {
        if (rpcMobile != null) {
            if (rpcMobile.isOpen()) {
                return rpcMobile.getEndpoints();
            }
        }
        throw new RuntimeException("Open Agent at first!");
    }


    public String generateQrCode(String value) {
        checkIsOpen();
        RemoteParams params = RemoteParams.RemoteParamsBuilder.create()
                .add("value", value)
                .build();
        try {
            Object response = rpcMobile.remoteCall("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/admin/1.0/generate_qr", params);
            if (response instanceof JSONObject) {
                JSONObject responseObject = (JSONObject) response;
                return responseObject.getString("url");
            }
        } catch (Exception siriusConnectionClosed) {
            siriusConnectionClosed.printStackTrace();
        }
        return null;
    }

    @Override
    public TheirEndpointCoProtocolTransport spawn(String myVerkey, TheirEndpoint endpoint) {
      /*  AgentRPC new_rpc = new AgentRPC(serverAddress, credentials, p2p, timeout);
        try {
            new_rpc.create();
            return new TheirEndpointCoProtocolTransport(myVerkey, endpoint, new_rpc);
        } catch (SiriusFieldValueError siriusFieldValueError) {
            siriusFieldValueError.printStackTrace();
        }*/
        return null;
    }

    @Override
    public PairwiseCoProtocolTransport spawn(Pairwise pairwise) {
     /*   AgentRPC newRpc = new AgentRPC(serverAddress, credentials, p2p, timeout);
        try {
            newRpc.create();
            return new PairwiseCoProtocolTransport(pairwise, newRpc);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return null;
    }

    @Override
    public ThreadBasedCoProtocolTransport spawn(String thid, Pairwise pairwise) {
       /* AgentRPC newRpc = new AgentRPC(serverAddress, credentials, p2p, timeout);
        try {
            newRpc.create();
            return new ThreadBasedCoProtocolTransport(thid, pairwise, newRpc, null);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return null;
    }

    @Override
    public ThreadBasedCoProtocolTransport spawn(String thid) {
      /*  AgentRPC newRpc = new AgentRPC(serverAddress, credentials, p2p, timeout);
        try {
            newRpc.create();
            return new ThreadBasedCoProtocolTransport(thid, null, newRpc, null);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return null;
    }

    @Override
    public ThreadBasedCoProtocolTransport spawn(String thid, Pairwise pairwise, String pthid) {
     /*   AgentRPC newRpc = new AgentRPC(serverAddress, credentials, p2p, timeout);
        try {
            newRpc.create();
            return new ThreadBasedCoProtocolTransport(thid, pairwise, newRpc, pthid);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return null;
    }

    @Override
    public ThreadBasedCoProtocolTransport spawn(String thid, String pthid) {
      /*  AgentRPC newRpc = new AgentRPC(serverAddress, credentials, p2p, timeout);
        try {
            newRpc.create();
            return new ThreadBasedCoProtocolTransport(thid, null, newRpc, pthid);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return null;
    }

    /**
     * Acquire N resources given by names
     *
     * @param resources       names of resources that you are going to lock
     * @param lockTimeoutSec  max timeout resources will be locked. Resources will be automatically unlocked on expire
     * @param enterTimeoutSec timeout to wait resources are released
     * @return
     */
    public Pair<Boolean, List<String>> acquire(List<String> resources, Double lockTimeoutSec, Double enterTimeoutSec) {
        checkIsOpen();
        return new RemoteCallWrapper<Pair<Boolean, List<String>>>(rpcMobile) {
        }.
                remoteCall("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/admin/1.0/acquire",
                        RemoteParams.RemoteParamsBuilder.create()
                                .add("names", resources)
                                .add("enter_timeout", enterTimeoutSec)
                                .add("lock_timeout", lockTimeoutSec));
    }

    public Pair<Boolean, List<String>> acquire(List<String> resources, double lockTimeoutSec) {
        return acquire(resources, lockTimeoutSec, 3.0);
    }

    public void release() {
        new RemoteCallWrapper<Void>(rpcMobile) {
        }.
                remoteCall("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/admin/1.0/release");
    }

}
