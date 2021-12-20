package com.github.pihme.docelar.frontend.zeebe;

import java.util.regex.Pattern;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Credentials {
	static final String REGEX_ZEEBE_ADRESS = "export ZEEBE_ADDRESS='(.*)'";
	static final String REGEX_ZEEBE_CLIENT_ID = "export ZEEBE_CLIENT_ID='(.*)'";
	static final String REGEX_ZEEBE_CLIENT_SECRET = "export ZEEBE_CLIENT_SECRET='(.*)'";
	static final String REGEX_ZEEBE_AUTHORIZATION_SERVER_URL = "export ZEEBE_AUTHORIZATION_SERVER_URL='(.*)'";

	private static final Pattern PATTERN_ZEEBE_ADDRESS = Pattern.compile(REGEX_ZEEBE_ADRESS);
	private static final Pattern PATTERN_ZEEBE_CLIENT_ID = Pattern.compile(REGEX_ZEEBE_CLIENT_ID);
	private static final Pattern PATTERN_ZEEBE_CLIENT_SECRET = Pattern.compile(REGEX_ZEEBE_CLIENT_SECRET);
	private static final Pattern PATTERN_ZEEBE_AUTHORIZATION_SERVER_URL = Pattern
			.compile(REGEX_ZEEBE_AUTHORIZATION_SERVER_URL);

	private String credentialsRaw = null;
	private String zeebeAddress = null;
	private String zeebeClientId = null;
	private String zeebeClientSecret = null;
	private String zeebeAuthorizationServerUrl = null;

	public String getCredentialsRaw() {
		return credentialsRaw;
	}

	public void setCredentialsRaw(String credentials) {
		this.credentialsRaw = credentials;
		parseCredentials();
	}

	public String getZeebeAddress() {
		return zeebeAddress;
	}

	public String getZeebeClientId() {
		return zeebeClientId;
	}

	public String getZeebeClientSecret() {
		return zeebeClientSecret;
	}

	public String getZeebeAuthorizationServerUrl() {
		return zeebeAuthorizationServerUrl;
	}

	private void parseCredentials() {
		zeebeAddress = match(PATTERN_ZEEBE_ADDRESS, credentialsRaw);
		zeebeClientId = match(PATTERN_ZEEBE_CLIENT_ID, credentialsRaw);
		zeebeClientSecret = match(PATTERN_ZEEBE_CLIENT_SECRET, credentialsRaw);
		zeebeAuthorizationServerUrl = match(PATTERN_ZEEBE_AUTHORIZATION_SERVER_URL, credentialsRaw);		
	}

	private String match(Pattern pattern, String input) {
		if (input == null) {
			return null;
		}
		final var matcher = pattern.matcher(input);
		if (matcher.find()) {
			return matcher.group(1);
		} else
			return null;
	}
	
	@Override
	public String toString() {
	  return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
}
