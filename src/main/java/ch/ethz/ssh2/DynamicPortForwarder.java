package ch.ethz.ssh2;

import java.io.IOException;

import ch.ethz.ssh2.channel.ChannelManager;
import ch.ethz.ssh2.channel.DynamicPortForwarderThread;

public class DynamicPortForwarder {
	private DynamicPortForwarderThread dftread;

	public DynamicPortForwarder(ChannelManager channelManager, int localPort)
			throws IOException {
		super();
		dftread = new DynamicPortForwarderThread(channelManager, localPort);
		dftread.setDaemon(true);
		dftread.start();
	}

	public void stop() {
		if (dftread != null)
			dftread.stopWorking();
	}
}
