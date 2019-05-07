package be.nabu.eai.module.cluster.hazelcast;

import javax.xml.bind.annotation.XmlRootElement;

import be.nabu.eai.api.EnvironmentSpecific;

@XmlRootElement(name = "hazelcast")
public class HazelcastClusterConfiguration {
	private String amazonTagKey, amazonTagValue, amazonRegion, hazelcastPort;
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
	
	// the hazelcast clustering port
	@EnvironmentSpecific
	public String getHazelcastPort() {
		return hazelcastPort;
	}
	public void setHazelcastPort(String hazelcastPort) {
		this.hazelcastPort = hazelcastPort;
	}

	
}
