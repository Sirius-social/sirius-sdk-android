package com.sirius.sdk_android.wallet.impl;

import com.sirius.sdk.agent.RemoteParams;
import com.sirius.sdk.agent.connections.BaseAgentConnection;
import com.sirius.sdk.agent.connections.RemoteCallWrapper;
import com.sirius.sdk.agent.wallet.abstract_wallet.AbstractCache;
import com.sirius.sdk.agent.wallet.abstract_wallet.model.CacheOptions;
import com.sirius.sdk.agent.wallet.abstract_wallet.model.PurgeOptions;

public class MobileCacheProxy extends AbstractCache {

    BaseAgentConnection rpc;

    public MobileCacheProxy(BaseAgentConnection rpc) {
        this.rpc = rpc;
    }



    @Override
    public String getSchema(String poolName, String submitter_did, String id, CacheOptions options) {
        return new RemoteCallWrapper<String>(rpc){}.
                remoteCall("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/sirius_rpc/1.0/get_schema",
                        RemoteParams.RemoteParamsBuilder.create()
                                .add("pool_name", poolName).
                                add("submitter_did", submitter_did).add("id_", id).
                                add("options",options));
    }

    @Override
    public String getCredDef(String poolName, String submitter_did, String id, CacheOptions options) {
        return new RemoteCallWrapper<String>(rpc){}.
                remoteCall("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/sirius_rpc/1.0/get_cred_def",
                        RemoteParams.RemoteParamsBuilder.create()
                                .add("pool_name", poolName).
                                add("submitter_did", submitter_did).add("id_", id).
                                add("options",options));
    }

    @Override
    public void purgeSchemaCache(PurgeOptions options) {
        new RemoteCallWrapper(rpc){}.
                remoteCall("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/sirius_rpc/1.0/purge_schema_cache",
                        RemoteParams.RemoteParamsBuilder.create().
                                add("options",options));
    }

    @Override
    public void purgeCredDefCache(PurgeOptions options) {
        new RemoteCallWrapper(rpc){}.
                remoteCall("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/sirius_rpc/1.0/purge_cred_def_cache",
                        RemoteParams.RemoteParamsBuilder.create().
                                add("options",options));
    }
}
