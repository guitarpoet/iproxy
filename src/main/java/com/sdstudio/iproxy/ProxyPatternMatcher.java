package com.sdstudio.iproxy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.XStream;

@Component("ProxyPatternMatcher")
public class ProxyPatternMatcher {
	private XStream xstream;
	private HashMap<Integer, Set<ProxyInfo>> infos;

	public XStream getXstream() {
		return xstream;
	}

	@Autowired
	public void setXstream(XStream xstream) {
		this.xstream = xstream;
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void load() throws FileNotFoundException, IOException {
		infos = (HashMap<Integer, Set<ProxyInfo>>) getXstream().fromXML(
				getPatternFile());
	}

	protected Reader getPatternFile() throws FileNotFoundException {
		if (new File(getPatternFilePath()).exists()) {
			return new FileReader(new File(getPatternFilePath()));
		}
		return new InputStreamReader(Thread.currentThread()
				.getContextClassLoader().getResourceAsStream(
						"pattern.default.xml"));
	}

	protected String getPatternFilePath() {
		return Utils.combinePaths(Utils.getUserHomeDir(), ".iproxy.pattern");
	}

	@PreDestroy
	public void save() throws IOException {
		getXstream().toXML(infos,
				new FileWriter(Utils.getOrCreate(getPatternFilePath(), false)));
	}

	public void addProxyInfo(String name, String host, int port, boolean enabled) {
		Set<ProxyInfo> pinfos = infos.get(port);
		ProxyInfo p = new ProxyInfo(host, port, name, enabled);
		if (pinfos == null) {
			pinfos = new HashSet<ProxyInfo>();
			infos.put(port, pinfos);
		}
		if (!pinfos.contains(p))
			pinfos.add(p);
	}

	public void removeProxyInfo(ProxyInfo p) {
		Set<ProxyInfo> pinfos = infos.get(p.getPort());
		if (pinfos != null)
			pinfos.remove(p);
	}

	public boolean match(ProxyInfo p) {
		Set<ProxyInfo> pinfos = infos.get(p.getPort());
		if (pinfos == null)
			return false;
		for (ProxyInfo proxyInfo : pinfos) {
			if (p.getHost().contains(proxyInfo.getHost())
					&& proxyInfo.isEnabled())
				return true;
		}
		return false;
	}
}
