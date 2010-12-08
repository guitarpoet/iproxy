package ch.ethz.ssh2.log;

import org.slf4j.LoggerFactory;

/**
 * Logger - a very simple logger, mainly used during development. Is not based
 * on log4j (to reduce external dependencies). However, if needed, something
 * like log4j could easily be hooked in.
 * 
 * @author Christian Plattner
 * @version 2.50, 03/15/10
 */

@SuppressWarnings("unchecked")
public class Logger {
	private org.slf4j.Logger logger;

	public final static Logger getLogger(Class x) {
		return new Logger(x);
	}

	public Logger(Class x) {
		logger = LoggerFactory.getLogger(x);
	}

	public final boolean isEnabled() {
		return logger.isInfoEnabled();
	}

	public final void log(int level, String message) {
		if (level == 50)
			logger.debug(message);
		else
			logger.info(message);
	}
}
