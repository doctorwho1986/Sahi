package com.sahi.request;

import com.sahi.StreamHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.net.URLDecoder;

/**
 * User: nraman Date: May 13, 2005 Time: 10:01:13 PM
 */
public class HttpRequest extends StreamHandler {
	private String host;
	private int port;
	private String uri;
	private String queryString = null;
	private Map params = new HashMap();
	private Map cookies = null;
	private static final Logger logger = Logger.getLogger("com.sahi.request.HttpRequest");

	public HttpRequest(InputStream in) throws IOException {
		populateHeaders(in, true);
		if (isPost())
			populateData(in);
		if (isPost() || isGet() || isConnect()) {
			setHostAndPort();
			setUri();
		}
		logger.fine("\nFirst line:"+firstLine());
		logger.fine("isSSL="+isSSL());
	}

	public String host() {
		return host;
	}

	public int port() {
		return port;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean isPost() {
		boolean isPost = "post".equalsIgnoreCase(method());
		return isPost;
	}

	public boolean isGet() {
		boolean isPost = "get".equalsIgnoreCase(method());
		return isPost;
	}
	
	public boolean isConnect() {
		boolean isConnect = "connect".equalsIgnoreCase(method());
		return isConnect;
	}	

	public boolean isSSL() {
		boolean isSSL = firstLine().indexOf("HTTPS") != -1;
		return isSSL || isConnect();
	}

	public String method() {
		return firstLine().substring(0, firstLine().indexOf(" "));
	}

	private void setUri() {
		String withHost = firstLine().substring(firstLine().indexOf(" "),
			firstLine().lastIndexOf(" ")).trim();
		uri = withHost;
		int indexOfHost = withHost.indexOf(host);
		if (indexOfHost != -1) {
			int indexOfSlash = withHost.indexOf("/", indexOfHost + 3);
			if (indexOfSlash != -1) // will happen when the host is embedded in
				// the querystring too.
				uri = withHost.substring(indexOfSlash);
		}
	}

	public String uri() {
		return uri;
	}

	public String protocol() {
		return firstLine().substring(firstLine().lastIndexOf(" "));
	}

	private void setHostAndPort() {
		String hostWithPort = (String) headers().get("Host");
		host = hostWithPort;
		port = 80;
		if (isSSL()) port = 443;
		int indexOfColon = hostWithPort.indexOf(":");
		if (indexOfColon != -1) {
			host = hostWithPort.substring(0, indexOfColon);
			try {
				port = Integer.parseInt(hostWithPort
						.substring(indexOfColon + 1).trim());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setQueryString() {
		if (uri == null)
			return;
		int qIx = uri.indexOf("?");
		if (qIx != -1 && qIx + 1 < uri.length()) {
			queryString = uri.substring(qIx + 1);
			return;
		}
		queryString = "";
		return;
	}

	private void setGetParameters() {
		StringTokenizer tokenizer = new StringTokenizer(queryString(), "&");
		while (tokenizer.hasMoreTokens()) {
			String keyVal = tokenizer.nextToken();
			int eqIx = keyVal.indexOf('=');
			if (eqIx != -1) {
				String key = keyVal.substring(0, eqIx);
				String value = "";
				if (eqIx + 1 <= keyVal.length())
					value = keyVal.substring(eqIx + 1);
				try {
					params.put(key, URLDecoder.decode(value, "UTF8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String queryString() {
		if (queryString == null) {
			setQueryString();
		}
		return queryString;
	}

	public String getParameter(String key) {
		if (params.size() == 0) {
			setGetParameters();
		}
		return (String) params.get(key);
	}

	private void setCookies() {
		cookies = new HashMap();
		String cookieString = (String) headers().get("Cookie");
		if (cookieString == null)
			return;
		StringTokenizer tokenizer = new StringTokenizer(cookieString, ";");
		while (tokenizer.hasMoreTokens()) {
			String keyVal = tokenizer.nextToken();
			int eqIx = keyVal.indexOf('=');
			if (eqIx != -1) {
				String key = keyVal.substring(0, eqIx).trim();
				String value = "";
				if (eqIx + 1 <= keyVal.length())
					value = keyVal.substring(eqIx + 1).trim();
				cookies.put(key, value);
			}
		}
	}

	public String getCookie(String key) {
		if (cookies == null) {
			setCookies();
		}
		return (String) cookies.get(key);
	}
}