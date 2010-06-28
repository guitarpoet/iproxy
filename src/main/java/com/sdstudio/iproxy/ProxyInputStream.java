package com.sdstudio.iproxy;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

public class ProxyInputStream extends InputStream {

	private BufferedInputStream proxy;

	private String host;

	private int port;

	private byte[] buffer;

	public ProxyInputStream(Socket socket) throws IOException {
		this.proxy = new BufferedInputStream(socket.getInputStream());
		this.proxy.mark(this.proxy.available());
		buffer = new byte[1024];
		ByteOutputStream out = new ByteOutputStream();
		proxy.read(buffer);
		out.write(buffer);
		String s = new String(buffer);
		for (String line : s.split("\n")) {
			if (line.trim().startsWith("Host")) {
				String[] data = line.split(":");
				host = data[1].trim();
				if (data.length > 2)
					port = Integer.parseInt(data[2].trim());
				else
					port = 80;
			}
		}
		this.proxy.reset();
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	@Override
	public int read() throws IOException {
		return proxy.read();
	}

	@Override
	public int hashCode() {
		return proxy.hashCode();
	}

	@Override
	public int read(byte[] b) throws IOException {
		return proxy.read(b);
	}

	@Override
	public boolean equals(Object obj) {
		return proxy.equals(obj);
	}

	@Override
	public String toString() {
		return proxy.toString();
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return proxy.read(b, off, len);
	}

	@Override
	public long skip(long n) throws IOException {
		return proxy.skip(n);
	}

	@Override
	public int available() throws IOException {
		return proxy.available();
	}

	@Override
	public void mark(int readlimit) {
		proxy.mark(readlimit);
	}

	@Override
	public void reset() throws IOException {
		proxy.reset();
	}

	@Override
	public boolean markSupported() {
		return proxy.markSupported();
	}

	@Override
	public void close() throws IOException {
		proxy.close();
	}

}
