package com.sirius.sdk_android.hub;



import com.sirius.sdk.agent.microledgers.AbstractMicroledgerList;
import com.sirius.sdk.agent.pairwise.AbstractPairwiseList;
import com.sirius.sdk.agent.wallet.abstract_wallet.AbstractAnonCreds;
import com.sirius.sdk.agent.wallet.abstract_wallet.AbstractCache;
import com.sirius.sdk.agent.wallet.abstract_wallet.AbstractCrypto;
import com.sirius.sdk.agent.wallet.abstract_wallet.AbstractDID;
import com.sirius.sdk.agent.wallet.abstract_wallet.AbstractNonSecrets;
import com.sirius.sdk.encryption.P2PConnection;
import com.sirius.sdk.hub.Hub;
import com.sirius.sdk.storage.abstract_storage.AbstractImmutableCollection;

public class HubMobile extends Hub {

    public HubMobile(Config config) {
        super(config);
        System.out.println("mylog299 HubMobile super");
    }

    AgentMobile agentMobile = null;
    String endpoint;
    @Override
    public AgentMobile getAgent() {
        return agentMobile;
    }

    @Override
    public AgentMobile getAgentConnectionLazy() {
        if (!agentMobile.isOpen()) {
            agentMobile.open();
        }
        return agentMobile;
    }

    @Override
    public void createAgentInstance() {
        System.out.println("mylog299 createAgentInstance");
        agentMobile = new AgentMobile(getConfig().serverUri, getConfig().credentials, getConfig().p2p, getConfig().ioTimeout, getConfig().storage);
        agentMobile.endpoint = endpoint;
        agentMobile.open();
        System.out.println("mylog299 createAgentInstance open agentMobile="+agentMobile);
    }





    public AbstractNonSecrets getNonSecrets() {
        if (this.getConfig().nonSecrets != null) {
            return this.getConfig().nonSecrets;
        } else {
            return agentMobile.getMobileWallet().getNonSecrets();
        }
    }

    public AbstractCrypto getCrypto() {
        System.out.println("mylog2090 agentMobile="+agentMobile);
        System.out.println("mylog2090 this.getConfig().crypto="+this.getConfig().crypto);
        if (this.getConfig().crypto != null) {
            return this.getConfig().crypto;
        } else {
            if(agentMobile==null){
                createAgentInstance();
            }
            return agentMobile.getMobileWallet().getCrypto();
        }
    }

    public AbstractDID getDid() {
        if (this.getConfig().did != null) {
            return this.getConfig().did;
        } else {
            return getAgentConnectionLazy().getMobileWallet().getDid();
        }
    }

    public AbstractPairwiseList getPairwiseList() {
        if (this.getConfig().pairwiseStorage != null) {
            return this.getConfig().pairwiseStorage;
        } else {
            return getAgentConnectionLazy().getPairwiseList();
        }
    }

    public String getServerUri() {
        return getConfig().serverUri;
    }

    public Hub setServerUri(String serverUri) {
        this.getConfig().serverUri = serverUri;
        return this;
    }

    public byte[] getCredentials() {
        return getConfig().credentials;
    }

    public Hub setCredentials(byte[] credentials) {
        this.getConfig().credentials = credentials;
        return this;
    }

    public P2PConnection getConnection() {
        return getConfig().p2p;
    }

    public Hub setConnection(P2PConnection connection) {
        this.getConfig().p2p = connection;
        return this;
    }

    public AbstractAnonCreds getAnonCreds() {
        if (getConfig().anoncreds != null) {
            return getConfig().anoncreds;
        } else {
            return getAgentConnectionLazy().getMobileWallet().getAnoncreds();
        }
    }

    public AbstractCache getCache() {
        if (getConfig().cache != null) {
            return getConfig().cache;
        } else {
            return getAgentConnectionLazy().getMobileWallet().getCache();
        }
    }


    public int getTimeout() {
        return getConfig().ioTimeout;
    }

    public Hub setTimeout(int timeout) {
        this.getConfig().ioTimeout = timeout;
        return this;
    }

    public AbstractImmutableCollection getStorage() {
        return getConfig().storage;
    }

    public AbstractMicroledgerList getMicroledgers() {
        if (getConfig().microledgers != null) {
            return getConfig().microledgers;
        } else {
            return getAgentConnectionLazy().getMicroledgers();
        }
    }

    public Hub setStorage(AbstractImmutableCollection storage) {
        this.getConfig().storage = storage;
        return this;
    }




    @Override
    public void close() {
        if (agentMobile != null)
            agentMobile.close();
    }

}
