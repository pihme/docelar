package com.github.pihme.docelar.frontend.zeebe;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProvider;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProviderBuilder;

public class ZeebeClientBuilder {

	public ZeebeClient build(final Credentials credentials) {
		final var zeebeAPI = credentials.getZeebeAddress().substring(0,credentials.getZeebeAddress().lastIndexOf(":")); //cut off port
;
		OAuthCredentialsProvider credentialsProvider = new OAuthCredentialsProviderBuilder()
				.authorizationServerUrl(credentials.getZeebeAuthorizationServerUrl()).audience(zeebeAPI)				
				.clientId(credentials.getZeebeClientId()).clientSecret(credentials.getZeebeClientSecret()).build();
		ZeebeClient client = ZeebeClient.newClientBuilder().gatewayAddress(zeebeAPI)
				.credentialsProvider(credentialsProvider).build();

		return client;
	}

}
