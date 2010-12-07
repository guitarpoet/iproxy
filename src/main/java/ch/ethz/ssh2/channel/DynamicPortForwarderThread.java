package ch.ethz.ssh2.channel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sdstudio.iproxy.ProxyInfo;

public class DynamicPortForwarderThread extends Thread implements
		IChannelWorkerThread {
	private static final Logger logger = LoggerFactory
			.getLogger(DynamicPortForwarderThread.class);

	private ChannelManager channelManager;
	private final ServerSocket proxySock;
	private static final ProxyInfo info = new ProxyInfo();

	public DynamicPortForwarderThread(ChannelManager channelManager,
			int localPort) throws IOException {
		super();
		this.channelManager = channelManager;
		proxySock = new ServerSocket(localPort);
	}

	public void stopWorking() {
		try {
			proxySock.close();
		} catch (IOException e) {
			logger.error("Error in closing the forward port", e);
		}
	}

	@Override
	public void run() {
		super.run();
		try {
			channelManager.registerThread(this);
		} catch (IOException e) {
			stopWorking();
			return;
		}
		while (true) {
			Socket s = null;
			try {
				s = proxySock.accept();
			} catch (IOException e) {
				stopWorking();
				return;
			}
			PushbackInputStream in;
			try {
				in = new PushbackInputStream(s.getInputStream());
				OutputStream out = s.getOutputStream();
				if ((in.read()) == 4) {
					if (in.read() == 1) {
						int i = in.read();
						int j = in.read();
						info.setPort(((int) i << 8) + j);
						byte[] ip = new byte[4];
						ip[0] = (byte) in.read();
						ip[1] = (byte) in.read();
						ip[2] = (byte) in.read();
						ip[3] = (byte) in.read();
						if (ip[0] != 0) {
							info.setHost(InetAddress.getByAddress(ip)
									.getHostAddress());
						} else {
							while ((i = in.read()) != 0) {
								// Skip name
							}
							ByteArrayOutputStream ba = new ByteArrayOutputStream();
							while ((i = in.read()) != 0) {
								ba.write(i);
							}
							ba.close();
							info.setHost(new String(ba.toByteArray()));
						}
						out.write(0);
						out.write(0x5a);
						Channel cn = null;
						StreamForwarder r2l = null;
						StreamForwarder l2r = null;
						try {
							/*
							 * This may fail, e.g., if the remote port is closed
							 * (in optimistic terms: not open yet)
							 */

							cn = channelManager.openDirectTCPIPChannel(info
									.getHost(), info.getPort(), s
									.getInetAddress().getHostAddress(), s
									.getPort());

						} catch (IOException e) {
							/*
							 * Simply close the local socket and wait for the
							 * next incoming connection
							 */
							try {
								s.close();
							} catch (IOException ignore) {
							}

							continue;
						}
						out.write(new byte[] { 0, 0, 0, 0, 0, 0 });
						try {
							r2l = new StreamForwarder(cn, null, null,
									cn.stdoutStream, out, "RemoteToLocal");
							l2r = new StreamForwarder(cn, r2l, s, in,
									cn.stdinStream, "LocalToRemote");
						} catch (IOException e) {
							try {
								/*
								 * This message is only visible during
								 * debugging, since we discard the channel
								 * immediatelly
								 */
								cn.cm.closeChannel(cn,
										"Weird error during creation of StreamForwarder ("
												+ e.getMessage() + ")", true);
							} catch (IOException ignore) {
							}

							continue;
						}

						r2l.setDaemon(true);
						l2r.setDaemon(true);
						r2l.start();
						l2r.start();
					}
				}
			} catch (IOException e1) {
			}
		}

	}
}
