/**
 * 
 */
package com.jmuscles.async.producer.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.jmuscles.async.producer.AsyncPayloadDeliverer;
import com.jmuscles.processing.schema.Payload;
import com.jmuscles.processing.schema.TrackingDetail;
import com.jmuscles.processing.schema.requestdata.RequestData;
import com.jmuscles.processing.schema.requestdata.RestRequestData;

/**
 * 
 */
@Component
public class JmusclesProducerHelper {

	@Autowired
	private AsyncPayloadDeliverer asyncPayloadDeliverer;

	public ResponseEntity<?> queuePayload(Payload payload, TrackingDetail trackingDetail) {
		boolean queued = false;
		try {
			if (asyncPayloadDeliverer.send(payload, trackingDetail) == null) {
				queued = true;
			}
		} catch (Exception e) {
			// TODO log the error
		}
		return queued ? ResponseEntity.ok("Success")
				: new ResponseEntity<>(null, null, HttpStatus.SC_EXPECTATION_FAILED);
	}

	public ResponseEntity<?> processRestRequest(HttpEntity<byte[]> requestEntity, HttpServletRequest request,
			String configKey, String urlSuffix) {
		return buildResponse(queueRestRequest(requestEntity, request, configKey, urlSuffix), configKey);
	}

	public boolean queueRestRequest(HttpEntity<byte[]> requestEntity, HttpServletRequest request, String configKey,
			String urlSuffix) {
		Payload payload = buildPayload(request.getMethod(), configKey, urlSuffix, requestEntity.getBody(),
				requestEntity.getHeaders().toSingleValueMap());
		boolean queued = false;

		try {
			if (asyncPayloadDeliverer.send(payload, TrackingDetail.of()) == null) {
				queued = true;
			}
		} catch (Exception e) {
			// TODO log the error
		}
		return queued;
	}

	private ResponseEntity<?> buildResponse(boolean queued, String configKey) {
		return queued ? buildSuccessResponse(configKey) : buildFailureResponse(configKey);
	}

	private ResponseEntity<?> buildSuccessResponse(String configKey) {
		return new ResponseEntity<>(null, null, HttpStatus.SC_OK);
	}

	private ResponseEntity<?> buildFailureResponse(String configKey) {
		return new ResponseEntity<>(null, null, HttpStatus.SC_EXPECTATION_FAILED);
	}

	private Payload buildPayload(String method, String configKey, String urlSuffix, Serializable body,
			Map<String, String> httpHeader) {
		RestRequestData restRequestData = new RestRequestData(method, configKey, urlSuffix, body, httpHeader);

		ArrayList<RequestData> list = new ArrayList<RequestData>();
		list.add(restRequestData);
		return new Payload(list);
	}

}
