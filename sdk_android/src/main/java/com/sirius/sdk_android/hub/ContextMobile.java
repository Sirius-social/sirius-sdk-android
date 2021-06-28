package com.sirius.sdk_android.hub;

import com.sirius.sdk.agent.microledgers.AbstractMicroledgerList;
import com.sirius.sdk.agent.wallet.abstract_wallet.AbstractCrypto;
import com.sirius.sdk.encryption.P2PConnection;
import com.sirius.sdk.hub.AbstractHub;
import com.sirius.sdk.hub.Context;
import com.sirius.sdk.hub.Hub;

public class ContextMobile extends Context {

    public ContextMobile(HubMobile hub) {
        super(hub);
    }

    @Override
    public HubMobile getCurrentHub() {
        return (HubMobile) super.getCurrentHub();
    }


    public static class MobileBuilder  {

        HubMobile.MobileConfig config = new HubMobile.MobileConfig();
        String endpoint;

        public MobileBuilder setCrypto(AbstractCrypto crypto) {
            this.config.crypto = crypto;
            return this;
        }

        public MobileBuilder setMicroledgers(AbstractMicroledgerList microledgers) {
            this.config.microledgers = microledgers;
            return this;
        }

        public MobileBuilder setServerUri(String serverUri) {
            this.config.serverUri = serverUri;
            return this;
        }

        public MobileBuilder setCredentials(byte[] credentials) {
            this.config.credentials = credentials;
            return this;
        }

        public MobileBuilder setP2p(P2PConnection p2p) {
            this.config.p2p = p2p;
            return this;
        }

        public MobileBuilder setTimeoutSec(int timeoutSec) {
            this.config.ioTimeout = timeoutSec;
            return this;
        }
        public MobileBuilder setEndpoint(String endpoint) {
            this.config.endpoint = endpoint;
            return this;
        }

        public ContextMobile build() {
            HubMobile hubMobile =  new HubMobile(this.config);
            System.out.println("build mylog299  endpoint" + endpoint);
            return new ContextMobile(hubMobile);
        }
    }

    public static ContextMobile.MobileBuilder builderMobile() {
        return new ContextMobile.MobileBuilder();
    }

}
