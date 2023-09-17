/**
 * 
 */
package com.jmuscles.rest.producer.response;

import com.jmuscles.processing.schema.requestdata.RequestData;

/**
 * @author manish goel
 *
 */
@FunctionalInterface
public interface BaseExecutor {

	public RequestData execute(RequestData requestData);
}
