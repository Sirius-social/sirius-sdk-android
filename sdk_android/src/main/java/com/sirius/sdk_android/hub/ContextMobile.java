package com.sirius.sdk_android.hub;

import com.sirius.sdk.agent.microledgers.AbstractMicroledgerList;
import com.sirius.sdk.agent.wallet.abstract_wallet.AbstractCrypto;
import com.sirius.sdk.encryption.P2PConnection;
import com.sirius.sdk.hub.Context;
import com.sirius.sdk.hub.Hub;

public class ContextMobile extends Context {

    public ContextMobile(HubMobile hub) {
        super(hub);
    }

    public static class Builder {
        HubMobile.Config config = new HubMobile.Config();
        String endpoint;
        //public AbstractCrypto crypto = null;
        //        public AbstractMicroledgerList microledgers = null;
        //        public AbstractPairwiseList pairwiseStorage = null;
        //        public AbstractDID did = null;
        //        public AbstractAnonCreds anoncreds = null;
        //        public AbstractNonSecrets nonSecrets = null;
        //        public String serverUri = null;
        //        public byte[] credentials;
        //        public P2PConnection p2p;
        //        public int ioTimeout = BaseAgentConnection.IO_TIMEOUT;
        //        public AbstractImmutableCollection storage = null;

        public ContextMobile.Builder setCrypto(AbstractCrypto crypto) {
            this.config.crypto = crypto;
            return this;
        }

        public ContextMobile.Builder setMicroledgers(AbstractMicroledgerList microledgers) {
            this.config.microledgers = microledgers;
            return this;
        }

        public ContextMobile.Builder setServerUri(String serverUri) {
            this.config.serverUri = serverUri;
            return this;
        }

        public ContextMobile.Builder setCredentials(byte[] credentials) {
            this.config.credentials = credentials;
            return this;
        }

        public ContextMobile.Builder setP2p(P2PConnection p2p) {
            this.config.p2p = p2p;
            return this;
        }

        public ContextMobile.Builder setTimeoutSec(int timeoutSec) {
            this.config.ioTimeout = timeoutSec;
            return this;
        }
        public ContextMobile.Builder setEndpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public ContextMobile build() {
            HubMobile hubMobile =  new HubMobile(this.config);
            hubMobile.endpoint = endpoint;
            return new ContextMobile(hubMobile);
        }
    }

    public static ContextMobile.Builder builderAndroid() {
        return new ContextMobile.Builder();
    }

}
