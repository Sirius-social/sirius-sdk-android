package com.sirius.sdk_android.wallet;

import com.sirius.sdk.agent.RemoteParams;
import com.sirius.sdk.agent.connections.BaseAgentConnection;
import com.sirius.sdk_android.wallet.impl.MobileAnonCredsProxy;
import com.sirius.sdk_android.wallet.impl.MobileCacheProxy;
import com.sirius.sdk_android.wallet.impl.MobileCryptoProxy;
import com.sirius.sdk_android.wallet.impl.MobileDIDProxy;
import com.sirius.sdk_android.wallet.impl.MobileLedgerProxy;
import com.sirius.sdk_android.wallet.impl.MobileNonSecretsProxy;
import com.sirius.sdk_android.wallet.impl.MobilePairwiseProxy;

public class MobileWallet {
    MobileAnonCredsProxy anoncreds;
    MobileDIDProxy did;
    MobileCryptoProxy crypto;
    MobileCacheProxy cache;
    MobileLedgerProxy ledger;
    BaseAgentConnection rpc;
    MobilePairwiseProxy pairwise;
    MobileNonSecretsProxy nonSecrets;

    public MobileWallet(BaseAgentConnection agentRPC) {

        did = new MobileDIDProxy(rpc);
        crypto = new MobileCryptoProxy(rpc);
        cache = new MobileCacheProxy(rpc);
        pairwise = new MobilePairwiseProxy(rpc);
        nonSecrets = new MobileNonSecretsProxy(rpc);
        ledger = new MobileLedgerProxy(rpc);
        anoncreds = new MobileAnonCredsProxy(rpc);
    }



    public MobileDIDProxy getDid() {
        return did;
    }

    public MobileCryptoProxy getCrypto() {
        return crypto;
    }

    public MobileCacheProxy getCache() {
        return cache;
    }

    public MobileLedgerProxy getLedger() {
        return ledger;
    }


    public MobilePairwiseProxy getPairwise() {
        return pairwise;
    }

    public MobileAnonCredsProxy getAnoncreds() {
        return anoncreds;
    }

    public MobileNonSecretsProxy getNonSecrets() {
        return nonSecrets;
    }

    public Object generateWalletKey(String seed){
        RemoteParams params = RemoteParams.RemoteParamsBuilder.create()
                .add("seed",seed)
                  .build();
        try {
            return rpc.remoteCall("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/sirius_rpc/1.0/generate_wallet_key",params);
        } catch (Exception siriusConnectionClosed) {
            siriusConnectionClosed.printStackTrace();
        }
        return null;
    }
}

