/**
 * 
 */
package com.jmuscles.rest.producer.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.jmuscles.async.producer.AsyncPayloadDeliverer;
import com.jmuscles.async.producer.properties.ProducerConfigProperties;
import com.jmuscles.processing.schema.Payload;
import com.jmuscles.processing.schema.TrackingDetail;
import com.jmuscles.processing.schema.requestdata.RequestData;
import com.jmuscles.processing.schema.requestdata.RestRequestData;
import com.jmuscles.rest.producer.config.RestProducerConfigProperties;
import com.jmuscles.rest.producer.config.RestResponseConfig;

/**
 * 
 */
@Component
public class JmusclesProducerHelper {

	private static final Logger logger = LoggerFactory.getLogger(JmusclesProducerHelper.class);

	@Autowired
	private AsyncPayloadDeliverer asyncPayloadDeliverer;

	@Autowired
	private Map<String, RestProducerConfigProperties> restProducerConfigPropertiesMap;

	public ResponseEntity<?> queuePayload(Payload payload, TrackingDetail trackingDetail) {
		boolean queued = false;
		try {
			if (asyncPayloadDeliverer.send(payload, trackingDetail) == null) {
				queued = true;
			}
		} catch (Exception e) {
			logger.error("issue in queuePayload: " + trackingDetail.toString(), e);
		}
		return queued ? ResponseEntity.ok("Success")
				: new ResponseEntity<>(null, null, HttpStatus.SC_EXPECTATION_FAILED);
	}

	public ResponseEntity<?> processRestRequest(HttpEntity<byte[]> requestEntity, HttpServletRequest request,
			String configKey, String urlSuffix) {

		RestProducerConfigProperties restProducerConfigProperties = restProducerConfigPropertiesMap.get(configKey);
		boolean queued = queueRestRequest(requestEntity, request, configKey, urlSuffix,
				restProducerConfigProperties.getProcessingConfig());
		return buildResponse(queued, restProducerConfigProperties.getResponseConfig());
	}

	public boolean queueRestRequest(HttpEntity<byte[]> requestEntity, HttpServletRequest request, String configKey,
			String urlSuffix, ProducerConfigProperties producerConfigProperties) {

		Payload payload = buildPayload(request.getMethod(), configKey, urlSuffix, requestEntity.getBody(),
				requestEntity.getHeaders().toSingleValueMap());

		boolean queued = false;
		try {
			Payload remainingPayload = null;
			if (producerConfigProperties == null) {
				remainingPayload = asyncPayloadDeliverer.send(payload, TrackingDetail.of());
			} else {
				remainingPayload = asyncPayloadDeliverer.send(payload, TrackingDetail.of(),
						producerConfigProperties.getActiveProducersInOrder(), producerConfigProperties.getRabbitmq());
			}
			if (remainingPayload == null) {
				queued = true;
			}
		} catch (Exception e) {
			logger.error("issue in queueRestRequest: " + configKey, e);
		}

		return queued;
	}

	private ResponseEntity<?> buildResponse(boolean queued, Map<String, RestResponseConfig> restResponseConfigMap) {
		RestResponseConfig response = restResponseConfigMap.get(queued ? "success" : "failure");
		return new ResponseEntity<>(response.getBody(), response.getHeaders(), response.getStatus());
	}

	private Payload buildPayload(String method, String configKey, String urlSuffix, Serializable body,
			Map<String, String> httpHeader) {
		RestRequestData restRequestData = new RestRequestData(method, configKey, urlSuffix, body, httpHeader);

		ArrayList<RequestData> list = new ArrayList<RequestData>();
		list.add(restRequestData);
		return new Payload(list);
	}

}
