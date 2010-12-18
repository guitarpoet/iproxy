package ch.ethz.ssh2.channel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sdstudio.iproxy.ProxyInfo;
import com.sdstudio.iproxy.ProxyPatternMatcher;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

public class DynamicPortForwarderThread extends Thread implements
		IChannelWorkerThread {
	private static final Logger logger = LoggerFactory
			.getLogger(DynamicPortForwarderThread.class);

	private ChannelManager channelManager;
	private final ServerSocket proxySock;
	private static final ProxyInfo info = new ProxyInfo();
	private ProxyPatternMatcher matcher;

	public DynamicPortForwarderThread(ChannelManager channelManager,
			int localPort) throws IOException {
		super();
		this.channelManager = channelManager;
		proxySock = new ServerSocket(localPort);
	}

	public ProxyPatternMatcher getMatcher() {
		return matcher;
	}

	public void setMatcher(ProxyPatternMatcher matcher) {
		this.matcher = matcher;
	}

	public void stopWorking() {
		try {
			proxySock.close();
		} catch (IOException e) {
			logger.error("Error in closing the forward port", e);
		}
	}

	protected void startSession(Socket s, InputStream in, OutputStream out) {
		Channel cn = null;
		StreamForwarder r2l = null;
		StreamForwarder l2r = null;
		try {
			cn = channelManager.openDirectTCPIPChannel(info.getHost(), info
					.getPort(), s.getInetAddress().getHostAddress(), s
					.getPort());

		} catch (IOException e) {
			try {
				s.close();
			} catch (IOException ignore) {
			}
			return;
		}
		try {
			r2l = new StreamForwarder(cn, null, null, cn.stdoutStream, out,
					"RemoteToLocal");
			l2r = new StreamForwarder(cn, r2l, s, in, cn.stdinStream,
					"LocalToRemote");
		} catch (IOException e) {
			try {
				cn.cm.closeChannel(cn,
						"Weird error during creation of StreamForwarder ("
								+ e.getMessage() + ")", true);
			} catch (IOException ignore) {
				logger.error("", ignore);
			}
			return;
		}
		r2l.setDaemon(true);
		l2r.setDaemon(true);
		r2l.start();
		l2r.start();
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
			try {
				byte[] ip = new byte[4];
				InputStream in = s.getInputStream();
				OutputStream out = s.getOutputStream();
				int v = in.read();
				if (v == 4 || v == 5) {
					switch (v) {
					case 4:
						if (in.read() == 1) {
							int i = in.read();
							int j = in.read();
							info.setPort(((int) i << 8) + j);
							ip = new byte[4];
							in.read(ip);
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
							out.write(new byte[] { 0, 0x5a, 0, 0, 0, 0, 0, 0 });
						}
						handleProxy(s, in, out);
						break;
					case 5:
						int count = in.read();
						// Read out all the methods
						for (int k = 0; k < count; k++) {
							in.read();
						}
						// Tell the client, no authentication needed.
						out.write(new byte[] { 0x05, 0x00 });
						out.flush();
						in.read(); // Must be 5 - socks version
						in.read(); // Must be 1 - connection
						in.read(); // Must be 0 - reserved
						int type = in.read();
						switch (type) {
						case 1: // ipv4
							in.read(ip);
							info.setHost(InetAddress.getByAddress(ip)
									.getHostAddress());
							break;
						case 3: // domain
							int length = in.read();
							ByteOutputStream buffer = new ByteOutputStream();
							for (int k = 0; k < length; k++) {
								buffer.write(in.read());
							}
							info.setHost(new String(buffer.getBytes()));
							break;
						case 4: // ipv6
							in.read(ip);
							break;
						}
						int i = in.read();
						int j = in.read();
						info.setPort(((int) i << 8) + j);
						ByteArrayOutputStream bs = new ByteArrayOutputStream();
						bs.write(new byte[] { 0x05, // Version 5
								0, // Request granted
								0 }); // Reserved
						bs.write(1); // Type for ipv4
						bs.write(InetAddress.getByName("localhost")
								.getAddress());
						bs.write(proxySock.getLocalPort() & 0xFF);
						bs.write((proxySock.getLocalPort() >> 8) & 0xFF);
						out.write(bs.toByteArray());
						out.flush();
						handleProxy(s, in, out);
						break;
					}
				}
			} catch (IOException e1) {
				logger.error("", e1);
			}
		}
	}

	private void handleProxy(Socket s, InputStream in, OutputStream out)
			throws UnknownHostException, IOException {
		if (getMatcher().match(info)) {
			startSession(s, in, out);
		} else {
			Socket sock = new Socket(info.getHost(), info.getPort());
			InputStream sin = sock.getInputStream();
			OutputStream sout = sock.getOutputStream();
			SocketStreamFowarder r2l = new SocketStreamFowarder(null, null, in,
					sout, null);
			SocketStreamFowarder l2r = new SocketStreamFowarder(sock, s, sin,
					out, r2l);
			r2l.setDaemon(true);
			l2r.setDaemon(true);
			r2l.start();
			l2r.start();
		}
	}
}
