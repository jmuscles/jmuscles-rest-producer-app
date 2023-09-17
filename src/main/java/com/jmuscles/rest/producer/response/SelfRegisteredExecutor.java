/**
 * 
 */
package com.jmuscles.rest.producer.response;

/**
 * @author manish goel
 *
 */
public abstract class SelfRegisteredExecutor implements BaseExecutor {

	public SelfRegisteredExecutor(ExecutorRegistry executorRegistry) {
		executorRegistry.register(getExecutorRequestDataClass(), this);
	}

	public abstract Class<?> getExecutorRequestDataClass();
}
