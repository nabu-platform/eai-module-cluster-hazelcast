package be.nabu.eai.module.cluster.hazelcast;

import be.nabu.eai.api.EnvironmentSpecific;

public class HazelcastClusterConfiguration {
	private String amazonTagKey, amazonTagValue, amazonRegion;
	private Integer port;

	@EnvironmentSpecific	
	public String getAmazonTagKey() {
		return amazonTagKey;
	}
	public void setAmazonTagKey(String amazonTagKey) {
		this.amazonTagKey = amazonTagKey;
	}
	@EnvironmentSpecific
	public String getAmazonTagValue() {
		return amazonTagValue;
	}
	public void setAmazonTagValue(String amazonTagValue) {
		this.amazonTagValue = amazonTagValue;
	}
	@EnvironmentSpecific
	public String getAmazonRegion() {
		return amazonRegion;
	}
	public void setAmazonRegion(String amazonRegion) {
		this.amazonRegion = amazonRegion;
	}
	// the port you want to expose
	@EnvironmentSpecific
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	
}
