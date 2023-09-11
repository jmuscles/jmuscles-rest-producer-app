/**
 * 
 */
package com.jmuscles.rest.producer.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
	public ResponseEntity<?> processRest(HttpEntity<byte[]> requestEntity, HttpServletRequest request,
			@PathVariable(required = true) String configKey) {
		String contextPath = request.getServletContext().getContextPath();
		String uri = request.getRequestURI();
		String urlSuffix = uri.replaceFirst(contextPath + "/event/rest/" + configKey, "");
		return jmusclesProducerHelper.processRestRequest(requestEntity, request, configKey, urlSuffix);
	}

}
