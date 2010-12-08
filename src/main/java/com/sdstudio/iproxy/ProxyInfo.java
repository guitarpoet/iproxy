package com.sdstudio.iproxy;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ProxyInfo {
	private String host;
	private int port;
	private String name;
	private boolean enabled;

	public ProxyInfo() {
	}

	public ProxyInfo(String host, int port, String name, boolean enabled) {
		super();
		this.host = host;
		this.port = port;
		this.name = name;
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getName()).append(getHost())
				.append(getPort()).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ProxyInfo) {
			ProxyInfo info = (ProxyInfo) obj;
			return new EqualsBuilder().append(getName(), info.getName())
					.append(getHost(), info.getHost()).append(getPort(),
							info.getPort()).isEquals();
		}
		return false;
	}
}
