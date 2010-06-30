package com.sdstudio.iproxy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.jboss.netty.buffer.BigEndianHeapChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Session;

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
		if (getSession() == null) {
			logger.error("No session attached!");
			return;
		}
		logger.debug("Message received :- {}", e);
		ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new ChannelBufferInputStream(buffer.copy())));
		String s = "";
		String host = "";
		int port = 0;
		while ((s = reader.readLine()) != null) {
			if (s.trim().startsWith("Host")) {
				String[] data = s.split(":");
				host = data[1].trim();
				if (data.length == 2)
					port = 80;
				else
					port = Integer.parseInt(data[2].trim());
			}
			System.out.println(s);
		}
		// Rewind the buffer.
		ChannelBuffer outbuffer = new BigEndianHeapChannelBuffer(1024);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new ChannelBufferOutputStream(outbuffer)));
		writer.write("Hello");
		writer.flush();
		e.getChannel().write(outbuffer)
				.addListener(new ChannelFutureListener() {
					public void operationComplete(ChannelFuture future)
							throws Exception {
						future.getChannel().close();
					}
				});
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		e.getCause().printStackTrace();
		logger.error("Caught an exception when proxying.", e);
		e.getChannel().close();
	}
}
