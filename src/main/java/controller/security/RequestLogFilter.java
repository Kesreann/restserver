package controller.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;


import lombok.extern.java.Log;

@Log
public class RequestLogFilter implements Filter {
	
		@Override
	public void init(FilterConfig filterConfig) throws ServletException { }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		chain.doFilter(httpRequest, httpResponse);
		
		StringBuilder fullUrl = new StringBuilder();
		fullUrl.append(httpRequest.getMethod());
		fullUrl.append(" ");
		fullUrl.append(httpRequest.getRequestURL());
		if (httpRequest.getQueryString() != null) {
			fullUrl.append('?').append(httpRequest.getQueryString());
		}
		
		log.log(Level.INFO, fullUrl.toString() );
		
		if (httpResponse instanceof ContentCachingResponseWrapper) {
			((ContentCachingResponseWrapper)httpResponse).copyBodyToResponse();
		}
	}

	@Override
	public void destroy() { }

}
