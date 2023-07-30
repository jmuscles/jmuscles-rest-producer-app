/**
 * 
 */
package com.jmuscles.async.producer.controller;

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

	// @RequestMapping(value={"/process", "/process/**"})
	@RequestMapping("/process")
	public ResponseEntity<?> queuePayload(Payload payload, TrackingDetail trackingDetail)
			throws JsonProcessingException {
		return jmusclesProducerHelper.queuePayload(payload, trackingDetail);
	}

	// @RequestMapping(value={"/process", "/process/**"})
	@RequestMapping("/rest/{configKey}/**")
	public ResponseEntity<?> processRest(HttpEntity<byte[]> requestEntity, HttpServletRequest request,
			@PathVariable(required = true) String configKey) {
		String uri = request.getRequestURI();
		String urlSuffix = uri.replaceFirst("/event/rest/" + configKey, "");
		return jmusclesProducerHelper.processRestRequest(requestEntity, request, configKey, urlSuffix);
	}

}
