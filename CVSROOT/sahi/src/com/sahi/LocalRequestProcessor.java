package com.sahi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.sahi.command.CommandExecuter;
import com.sahi.playback.log.LogFileConsolidator;
import com.sahi.request.HttpRequest;
import com.sahi.response.HttpFileResponse;
import com.sahi.response.HttpResponse;
import com.sahi.response.NoCacheHttpResponse;
import com.sahi.response.SimpleHttpResponse;
import com.sahi.util.URLParser;

public class LocalRequestProcessor {
	public HttpResponse getLocalResponse(String uri, HttpRequest requestFromBrowser) throws UnsupportedEncodingException, IOException {
		HttpResponse httpResponse = new NoCacheHttpResponse("");
		if (uri.indexOf("/dyn/") != -1) {
			String command = URLParser.getCommandFromUri(uri);
//			System.out.println("----------- " + session.id() + " " + uri);
			if (uri.indexOf("/stopserver") != -1) {
				System.exit(1);
			} else if (command != null) {
				httpResponse = new CommandExecuter(command, requestFromBrowser).execute();				
			}

		} else if (uri.indexOf("/scripts/") != -1) {
			String fileName = URLParser.scriptFileNamefromURI(
					requestFromBrowser.uri(), "/scripts/");
			httpResponse = new HttpFileResponse(fileName);
		} else if (uri.indexOf("/logs/") != -1 || uri.endsWith("/logs")) {
			httpResponse = getLogResponse(URLParser.logFileNamefromURI(requestFromBrowser.uri()));
		} else if (uri.indexOf("/spr/") != -1) {
			String fileName = URLParser.fileNamefromURI(requestFromBrowser.uri());
			httpResponse = new HttpFileResponse(fileName);
		} else {
			httpResponse = new SimpleHttpResponse(
					"<html><h2>You have reached the Sahi proxy.</h2></html>");
		}
		return httpResponse;
	}


	private HttpResponse getLogResponse(String fileName) throws IOException {
		if ("".equals(fileName))
			return new NoCacheHttpResponse(getLogsList());
		else
			return new HttpFileResponse(fileName);
	}

	private String getLogsList() {
		return LogFileConsolidator.getLogsList();
	}





}
