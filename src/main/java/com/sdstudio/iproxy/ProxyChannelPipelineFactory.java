package com.sdstudio.iproxy;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.Session;

@Component("ProxyChannelPipelineFactory")
public class ProxyChannelPipelineFactory implements ChannelPipelineFactory {
	private ProxyHandler proxyHandler;

	public void init(Session session) {
		getProxyHandler().setSession(session);
	}

	public ProxyHandler getProxyHandler() {
		return proxyHandler;
	}

	@Autowired
	public void setProxyHandler(ProxyHandler proxyHandler) {
		this.proxyHandler = proxyHandler;
	}

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipe = Channels.pipeline();
		pipe.addFirst("proxyHandler", getProxyHandler());
		return pipe;
	}
}
