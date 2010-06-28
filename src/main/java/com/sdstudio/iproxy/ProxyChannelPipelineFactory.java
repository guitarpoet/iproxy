package com.sdstudio.iproxy;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.Session;

@Component("ProxyChannelPipelineFactory")
public class ProxyChannelPipelineFactory implements ChannelPipelineFactory {
	private Session session;

	public void init(Session session) {
		this.session = session;
	}

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipe = Channels.pipeline();
		ProxyHandler handler = new ProxyHandler();
		handler.setSession(session);
		pipe.addFirst("proxyHandler", handler);
		return pipe;
	}
}
