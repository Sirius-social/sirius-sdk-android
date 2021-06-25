package com.sirius.sdk_android.hub;

import com.sirius.sdk.agent.Agent;
import com.sirius.sdk.hub.Hub;

public class HubAndroid extends Hub {

    public HubAndroid(Config config) {
        super(config);
    }

    AgentAndroid agent = null;

    @Override
    public AgentAndroid getAgent() {
        return agent;
    }

    @Override
    public AgentAndroid getAgentConnectionLazy() {
        if (!agent.isOpen()) {
            agent.open();
        }
        return agent;
    }

    @Override
    public void createAgentInstance() {
        super.createAgentInstance();
    }


}
