/**
 * 
 */
package com.jmuscles.rest.producer;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 */
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

public class HttpReqResLoggingIntercetpor implements Filter {

	private static final String JMUSCLE_TRACE_ID = "jmuscle-trace-id";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Initialization code (if needed)
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		try {
			// Generate a unique request ID and add it to the MDC
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			String jmuscleTraceId = httpRequest.getHeader(JMUSCLE_TRACE_ID);
			if (!StringUtils.hasText(jmuscleTraceId)) {
				jmuscleTraceId = UUID.randomUUID().toString();
			}
			MDC.put(JMUSCLE_TRACE_ID, jmuscleTraceId);

			// Continue the filter chain
			chain.doFilter(request, response);
		} finally {
			// Clean up the MDC after the request is processed
			MDC.remove(JMUSCLE_TRACE_ID);
		}
	}

	@Override
	public void destroy() {
		// Cleanup code (if needed)
	}
}
