/*
Copyright (c) 2012, Runnable
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
 * Neither the name of Runnable nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package nl.runnable.alfresco.osgi;

import java.util.Arrays;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

/**
 * Represents configuration for registering a bean from an {@link ApplicationContext} as a service in a
 * {@link BundleContext}.
 * 
 * @author Laurens Fridael
 * 
 */
public class ServiceDefinition {
	private final List<String> beanNames;

	private final List<String> serviceNames;

	private String serviceType;

	private String platformVersion;

	public ServiceDefinition(final String[] beanNames, final String[] serviceNames, final String serviceType,
			final String platformVersion) {
		Assert.notEmpty(beanNames, "Bean names cannot be empty.");
		Assert.notEmpty(serviceNames, "Service names cannot be empty.");

		this.beanNames = Arrays.asList(beanNames);
		this.serviceNames = Arrays.asList(serviceNames);
		this.serviceType = serviceType;
		this.platformVersion = platformVersion;
	}

	public ServiceDefinition(final String[] beanNames, final String[] serviceNames, final String serviceType) {
		Assert.notEmpty(beanNames, "Bean names cannot be empty.");
		Assert.notEmpty(serviceNames, "Service names cannot be empty.");

		this.beanNames = Arrays.asList(beanNames);
		this.serviceNames = Arrays.asList(serviceNames);
		this.serviceType = serviceType;
	}

	public ServiceDefinition(final String beanName, final String... serviceNames) {
		Assert.hasText(beanName, "Bean names cannot be empty.");
		Assert.notEmpty(serviceNames, "Service names cannot be empty.");

		this.beanNames = Arrays.asList(beanName);
		this.serviceNames = Arrays.asList(serviceNames);
	}

	public ServiceDefinition(final String[] beanNames, final String... serviceNames) {
		Assert.notEmpty(beanNames, "Bean names cannot be empty.");
		Assert.notEmpty(serviceNames, "Service names cannot be empty.");
		this.beanNames = Arrays.asList(beanNames);
		this.serviceNames = Arrays.asList(serviceNames);
	}

	public List<String> getBeanNames() {
		return beanNames;
	}

	public List<String> getServiceNames() {
		return serviceNames;
	}

	public String getServiceType() {
		return serviceType;
	}

	public String getPlatformVersion() {
		return platformVersion;
	}

}