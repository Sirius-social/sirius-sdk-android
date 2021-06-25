package com.sirius.sdk_android.hub;

import com.sirius.sdk.agent.microledgers.AbstractMicroledgerList;
import com.sirius.sdk.agent.wallet.abstract_wallet.AbstractCrypto;
import com.sirius.sdk.encryption.P2PConnection;
import com.sirius.sdk.hub.Context;

public class ContextAndroid  extends Context {
    public ContextAndroid(HubAndroid.Config config) {
        super(config);
    }

    public static class Builder {
        HubAndroid.Config config = new HubAndroid.Config();

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

        public ContextAndroid.Builder setCrypto(AbstractCrypto crypto) {
            this.config.crypto = crypto;
            return this;
        }

        public ContextAndroid.Builder setMicroledgers(AbstractMicroledgerList microledgers) {
            this.config.microledgers = microledgers;
            return this;
        }

        public ContextAndroid.Builder setServerUri(String serverUri) {
            this.config.serverUri = serverUri;
            return this;
        }

        public ContextAndroid.Builder setCredentials(byte[] credentials) {
            this.config.credentials = credentials;
            return this;
        }

        public ContextAndroid.Builder setP2p(P2PConnection p2p) {
            this.config.p2p = p2p;
            return this;
        }

        public ContextAndroid.Builder setTimeoutSec(int timeoutSec) {
            this.config.ioTimeout = timeoutSec;
            return this;
        }

        public ContextAndroid build() {
            return new ContextAndroid(this.config);
        }
    }

    public static ContextAndroid.Builder builderAndroid() {
        return new ContextAndroid.Builder();
    }

}
