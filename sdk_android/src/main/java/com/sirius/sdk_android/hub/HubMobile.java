package com.sirius.sdk_android.hub;




import com.sirius.sdk.hub.AbstractHub;


public class HubMobile extends AbstractHub {

    public HubMobile(MobileConfig config) {
        super(config);
    }

    public static class   MobileConfig extends AbstractHub.Config{
        public String endpoint;
    }

    @Override
    public AgentMobile getAgent() {
        return (AgentMobile)super.getAgent();
    }

    @Override
    public MobileConfig getConfig() {
        return (MobileConfig)super.getConfig();
    }

    @Override
    public void createAgentInstance() {
        System.out.println("HubMobile mylog299 createAgentInstance endpoint" + getConfig().endpoint);
        setAgent(new AgentMobile(getConfig().serverUri, getConfig().credentials, getConfig().p2p, getConfig().ioTimeout, getConfig().storage));
        getAgent().endpoint = getConfig().endpoint;
        System.out.println("mylog299 createAgentInstance getAgent().endpoint="+getAgent().endpoint);
        getAgent().open();
        System.out.println("mylog299 createAgentInstance open agentMobile="+getAgent().endpoint);
    }


    @Override
    public void close() {
        if (getAgent() != null)
            getAgent().close();
    }

}
