package com.jmuscles.rest.controller;

import java.io.Serializable;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jmuscles.processing.schema.PayloadRequest;
import com.jmuscles.rest.producer.helper.JmusclesRestControllerBean;

/**
 * 
 */

@RestController
@RequestMapping("/async/producer/")
@DependsOn("jmusclesRestControllerBean")
public class JmusclesRestProducerController {

	private static final Logger logger = LoggerFactory.getLogger(JmusclesRestProducerController.class);

	@Autowired
	private JmusclesRestControllerBean jmusclesRestControllerBean;

	@PostMapping("/process/{tenantId}/{configKey}")
	public ResponseEntity<?> queuePayload(@PathVariable String tenantId, @PathVariable String configKey,
			@RequestBody PayloadRequest request) throws JsonProcessingException {
		return jmusclesRestControllerBean.queuePayload(request.getPayload(), request.getTrackingDetail());
	}

	@RequestMapping("/rest/{configKey}/**")
	public ResponseEntity<?> processRestStringPayload(@RequestHeader Map<String, String> headers,
			@RequestBody(required = false) String requestBody, HttpServletRequest request,
			@PathVariable(required = true) String configKey) {
		return jmusclesRestControllerBean.processRestStringPayload(headers, requestBody, request, configKey);
	}

	@RequestMapping("/restByteArrayPayload/{configKey}/**")
	public ResponseEntity<?> processRestByteArrayPayload(@RequestHeader Map<String, String> headers,
			@RequestBody(required = false) Serializable requestBody, HttpServletRequest request,
			@PathVariable(required = true) String configKey) {
		return jmusclesRestControllerBean.processRestByteArrayPayload(headers, requestBody, request, configKey);
	}

}
