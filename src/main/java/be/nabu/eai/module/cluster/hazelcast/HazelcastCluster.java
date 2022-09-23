package be.nabu.eai.module.cluster.hazelcast;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.nabu.eai.repository.api.Repository;
import be.nabu.eai.repository.api.cluster.ClusterArtifact;
import be.nabu.eai.repository.api.cluster.ClusterMember;
import be.nabu.eai.repository.api.cluster.ClusterMemberSubscriber;
import be.nabu.eai.repository.api.cluster.ClusterMemberSubscription;
import be.nabu.eai.repository.artifacts.jaxb.JAXBArtifact;
import be.nabu.libs.resources.api.ResourceContainer;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
// hazelcast 4.2.4
//import com.hazelcast.cluster.Member;
//import com.hazelcast.cluster.MembershipEvent;
//import com.hazelcast.cluster.MembershipListener;
import com.hazelcast.core.HazelcastInstance;
// hazelcast 3.12
import com.hazelcast.core.Member;
import com.hazelcast.core.MemberAttributeEvent;
import com.hazelcast.core.MembershipEvent;
import com.hazelcast.core.MembershipListener;

public class HazelcastCluster extends JAXBArtifact<HazelcastClusterConfiguration> implements ClusterArtifact {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final class ClusterMemberImpl implements ClusterMember {
		private final InetSocketAddress address;

		private ClusterMemberImpl(InetSocketAddress address) {
			this.address = address;
		}

		@Override
		public InetSocketAddress getAddress() {
			return address;
		}

		@Override
		public boolean equals(Object object) {
			return object instanceof ClusterMember
				&& ((ClusterMember) object).getAddress().equals(address);
		}

		@Override
		public int hashCode() {
			return address.hashCode();
		}
	}

	private HazelcastInstance client;
	
	public HazelcastCluster(String id, ResourceContainer<?> directory, Repository repository) {
		super(id, directory, repository, "hazelcast-cluster.xml", HazelcastClusterConfiguration.class);
	}

	private HazelcastInstance getClient() {
		// if the client exists but no longer running, make sure we reinstantiate it
		if (client == null || !client.getLifecycleService().isRunning()) {
			synchronized(this) {
				if (client == null || !client.getLifecycleService().isRunning()) {
					ClientConfig config = new ClientConfig();
					// 3.10.2
//					ClientAwsConfig awsConfig = new ClientAwsConfig();
//					awsConfig.setEnabled(true);
//					awsConfig.setTagKey(getConfig().getAmazonTagKey());
//					awsConfig.setTagValue(getConfig().getAmazonTagValue());
//					awsConfig.setRegion(getConfig().getAmazonRegion());
//					config.getNetworkConfig().setAwsConfig(awsConfig);
					// newer
					// example: https://github.com/hazelcast/hazelcast-aws
					config.getNetworkConfig().getAwsConfig()
						.setEnabled(true)
						.setProperty("region", getConfig().getAmazonRegion())
						.setProperty("tag-key", getConfig().getAmazonTagKey())
						.setProperty("tag-value", getConfig().getAmazonTagValue())
						.setProperty("hz-port", getConfig().getHazelcastPort() == null ? "5701" : getConfig().getHazelcastPort());
					client = HazelcastClient.newHazelcastClient(config);
				}
			}
		}
		return client;
	}
	
	public List<ClusterMember> getLatestMembers() {
		List<ClusterMember> members = new ArrayList<ClusterMember>();
		try {
			for (Member member : getClient().getCluster().getMembers()) {
				final InetSocketAddress address = getAddress(member);
				members.add(new ClusterMemberImpl(address));
			}
		}
		catch (Exception e) {
			logger.warn("Could not get cluster members", e);
		}
		return members;
	}
	
	private Map<String, ClusterMember> cachedMembers = null;
	
	@Override
	public List<ClusterMember> getMembers() {
		if (cachedMembers == null) {
			synchronized(this) {
				if (cachedMembers == null) {
					Map<String, ClusterMember> calculateCached = new HashMap<String, ClusterMember>();
					// load initial members
					for (ClusterMember member : getLatestMembers()) {
						calculateCached.put(member.getAddress().toString(), member);
					}
					this.cachedMembers = calculateCached;
					// listen to events
					addMembershipListener(new ClusterMemberSubscriber() {
						@Override
						public void memberRemoved(ClusterMember member) {
							synchronized(cachedMembers) {
								cachedMembers.remove(member.getAddress().toString());
							}
						}
						@Override
						public void memberAdded(ClusterMember member) {
							synchronized(cachedMembers) {
								cachedMembers.put(member.getAddress().toString(), member);
							}
						}
					});
				}
			}
		}
		// if we have no members yet (or anymore), keep adding the latest
		if (cachedMembers.isEmpty()) {
			synchronized(cachedMembers) {
				if (cachedMembers.isEmpty()) {
					for (ClusterMember member : getLatestMembers()) {
						cachedMembers.put(member.getAddress().toString(), member);
					}
				}
			}
		}
		return new ArrayList<ClusterMember>(cachedMembers.values());
	}

	private InetSocketAddress getAddress(Member member) {
		return new InetSocketAddress(member.getSocketAddress().getAddress(), getConfig().getPort() == null ? 80 : getConfig().getPort());
	}

	@Override
	public ClusterMemberSubscription addMembershipListener(final ClusterMemberSubscriber subscriber) {
		// in 3.12 this returned a string
//		final String subscriptionId = getClient().getCluster().addMembershipListener(new MembershipListener() {
		final String subscriptionId = getClient().getCluster().addMembershipListener(new MembershipListener() {
			@Override
			public void memberRemoved(MembershipEvent arg0) {
				subscriber.memberRemoved(new ClusterMemberImpl(arg0.getMember().getSocketAddress()));
			}
			// available in 3.12
			// no longer available in 4.2.4
			@Override
			public void memberAttributeChanged(MemberAttributeEvent arg0) {
				// do nothing
			}
			@Override
			public void memberAdded(MembershipEvent arg0) {
				subscriber.memberAdded(new ClusterMemberImpl(arg0.getMember().getSocketAddress()));
			}
		});
		return new ClusterMemberSubscription() {
			@Override
			public void unsubscribe() {
				getClient().getCluster().removeMembershipListener(subscriptionId);
			}
		};
	}


}
