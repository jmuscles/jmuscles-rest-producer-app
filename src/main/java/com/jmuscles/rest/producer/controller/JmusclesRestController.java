/**
 * 
 */
package com.jmuscles.rest.producer.controller;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jmuscles.processing.schema.Payload;
import com.jmuscles.processing.schema.TrackingDetail;

/**
 * 
 */

@RestController
@RequestMapping("/event/")
public class JmusclesRestController {

	@Autowired
	private JmusclesProducerHelper jmusclesProducerHelper;

	@RequestMapping("/process")
	public ResponseEntity<?> queuePayload(Payload payload, TrackingDetail trackingDetail)
			throws JsonProcessingException {
		return jmusclesProducerHelper.queuePayload(payload, trackingDetail);
	}

	@RequestMapping("/rest/{configKey}/**")
	public ResponseEntity<?> processRestStringPayload(@RequestHeader Map<String, String> headers,
			@RequestBody String requestBody, HttpServletRequest request,
			@PathVariable(required = true) String configKey) {
		String contextPath = request.getServletContext().getContextPath();
		String uri = request.getRequestURI();
		String urlSuffix = uri.replaceFirst(contextPath + "/event/rest/" + configKey, "");
		return jmusclesProducerHelper.processRestRequest(requestBody, headers, request, configKey, urlSuffix);
	}

	@RequestMapping("/restByteArrayPayload/{configKey}/**")
	public ResponseEntity<?> processRestByteArrayPayload(@RequestHeader Map<String, String> headers,
			@RequestBody Serializable requestBody, HttpServletRequest request,
			@PathVariable(required = true) String configKey) {
		String contextPath = request.getServletContext().getContextPath();
		String uri = request.getRequestURI();
		String urlSuffix = uri.replaceFirst(contextPath + "/event/restByteArrayPayload/" + configKey, "");
		return jmusclesProducerHelper.processRestRequest(requestBody, headers, request, configKey, urlSuffix);
	}

}
