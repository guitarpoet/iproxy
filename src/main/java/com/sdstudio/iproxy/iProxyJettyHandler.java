package com.sdstudio.iproxy;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.handler.AbstractHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("iProxyJettyHandler")
public class iProxyJettyHandler extends AbstractHandler {
	private iProxy iproxy;

	public iProxy getIproxy() {
		return iproxy;
	}

	@Autowired
	public void setIproxy(iProxy iproxy) {
		this.iproxy = iproxy;
	}

	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, int dispatch) throws IOException,
			ServletException {
		String method = request.getParameter("method");
		if (method == null) {
			response.setStatus(ERROR);
			return;
		}
		try {
			Method m = iProxy.class.getMethod(method);
			Object o = m.invoke(getIproxy(), new Object[0]);
			if (method.equals("getPac") && o != null) {
				response.setContentType("application/x-ns-proxy-autoconfig");
				PrintWriter out = response.getWriter();
				out.println(o);
				out.close();
			}
		} catch (Exception e) {
			response.setStatus(ERROR);
		}
	}
}
