/*
* Copyright (C) 2019 Alexander Verbruggen
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <https://www.gnu.org/licenses/>.
*/

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
