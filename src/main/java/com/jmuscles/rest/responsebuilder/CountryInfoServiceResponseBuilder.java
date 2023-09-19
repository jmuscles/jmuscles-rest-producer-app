/**
 * 
 */
package com.jmuscles.rest.responsebuilder;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.jmuscles.rest.producer.config.RestConfPropsForConfigKey;
import com.jmuscles.rest.producer.config.RestResponseConfig;
import com.jmuscles.rest.producer.response.BaseResponseBuilder;
import com.jmuscles.rest.producer.response.ResponseBuilderMapKeys;

/**
 * This is the sample implementation for the response builder below key values
 * are available in the map to help build the response: {CONFIG_KEY,
 * HTTP_METHOD, HTTP_REQUEST, HTTP_HEADERS, REQUEST_BODY, IS_MESSAGE_QUEUED}
 * 
 */
@Component
public class CountryInfoServiceResponseBuilder extends BaseResponseBuilder {

	private static final Logger logger = LoggerFactory.getLogger(CountryInfoServiceResponseBuilder.class);

	@Autowired
	private Map<String, RestConfPropsForConfigKey> restProducerConfigPropertiesMap;

	@Override
	public ResponseEntity<?> buildResponse(Map<String, Object> map) {

		logger.debug("Start building response...");

		String configKey = (String) map.get(ResponseBuilderMapKeys.CONFIG_KEY);
		boolean queued = (boolean) map.get(ResponseBuilderMapKeys.IS_MESSAGE_QUEUED);
		String method = (String) map.get(ResponseBuilderMapKeys.HTTP_METHOD);

		RestResponseConfig response = restProducerConfigPropertiesMap.get(configKey).getConfigByHttpMethods()
				.get(method).getResponseConfig().get(queued ? "success" : "failure");
		return new ResponseEntity<>(response.getBody(), response.getHeaders(), response.getStatus());

	}

}
