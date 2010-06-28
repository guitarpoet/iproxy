package com.sdstudio.iproxy;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.Session;

@Component("ProxyHandler")
@ChannelPipelineCoverage("all")
public class ProxyHandler extends SimpleChannelHandler {
	private static Logger logger = LoggerFactory.getLogger(ProxyHandler.class);
	private Session session;

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		logger.debug("Message received :- {}", e);
		ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new ChannelBufferInputStream(buffer.copy())));
		String s = "";
		while ((s = reader.readLine()) != null) {
			System.out.println(s);
		}
		e.getChannel().write(buffer).addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture future)
					throws Exception {
				future.getChannel().close();
			}
		});
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		logger.error("Caught an exception when proxying.", e);
		e.getChannel().close();
	}
}
