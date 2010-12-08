package ch.ethz.ssh2;

import java.io.IOException;

import com.sdstudio.iproxy.ProxyPatternMatcher;

import ch.ethz.ssh2.channel.ChannelManager;
import ch.ethz.ssh2.channel.DynamicPortForwarderThread;

public class DynamicPortForwarder {
	private DynamicPortForwarderThread dftread;

	public void setMatcher(ProxyPatternMatcher matcher) {
		dftread.setMatcher(matcher);
		dftread.start();
	}

	public DynamicPortForwarder(ChannelManager channelManager, int localPort)
			throws IOException {
		super();
		dftread = new DynamicPortForwarderThread(channelManager, localPort);
		dftread.setDaemon(true);
	}

	public void stop() {
		if (dftread != null)
			dftread.stopWorking();
	}
}
