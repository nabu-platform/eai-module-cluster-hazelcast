package nabu.misc.hazelcast.types;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "member")
public class Member {
	private String host;
	private Integer port;
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
}
