package be.nabu.eai.module.cluster.hazelcast;

import be.nabu.eai.repository.api.Repository;
import be.nabu.eai.repository.managers.base.JAXBArtifactManager;
import be.nabu.libs.resources.api.ResourceContainer;

public class HazelcastClusterManager extends JAXBArtifactManager<HazelcastClusterConfiguration, HazelcastCluster> {

	public HazelcastClusterManager() {
		super(HazelcastCluster.class);
	}

	@Override
	protected HazelcastCluster newInstance(String id, ResourceContainer<?> container, Repository repository) {
		return new HazelcastCluster(id, container, repository);
	}

}
