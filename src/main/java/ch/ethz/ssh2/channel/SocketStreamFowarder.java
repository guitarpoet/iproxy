package ch.ethz.ssh2.channel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketStreamFowarder extends Thread {
	private Socket source;
	private Socket target;
	private InputStream sourceInput;
	private OutputStream targetOutput;
	private SocketStreamFowarder sibling;
	private static Logger logger = LoggerFactory
			.getLogger(SocketStreamFowarder.class);

	public SocketStreamFowarder(Socket source, Socket target,
			InputStream sourceInput, OutputStream targetOutput,
			SocketStreamFowarder sibling) {
		super();
		this.source = source;
		this.target = target;
		this.sourceInput = sourceInput;
		this.targetOutput = targetOutput;
		this.sibling = sibling;
	}

	@Override
	public void run() {
		super.run();
		try {
			if (source != null && target != null)
				logger.debug(MessageFormat.format(
						"Transfering data from {0} to {1}", source
								.getInetAddress(), target.getInetAddress()));
			int i;
			while ((i = sourceInput.read()) != -1) {
				targetOutput.write(i);
				targetOutput.flush();
			}
		} catch (IOException e) {
			if (source != null && target != null)
				logger.error(MessageFormat.format(
						"Exception when transfer data from {0} to {1}", source
								.getInetAddress(), target.getInetAddress()), e);
			else
				logger.error("", e);
		} finally {
			if (sibling != null) {
				try {
					sibling.join();
				} catch (InterruptedException e) {
					logger.error("", e);
				}
			}
			try {
				if (source != null) {
					source.close();
				}
				if (target != null) {
					target.close();
				}
			} catch (IOException e) {
				logger.error("", e);
			}
		}
	}
}
