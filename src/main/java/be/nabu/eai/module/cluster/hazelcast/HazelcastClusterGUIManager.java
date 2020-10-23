package be.nabu.eai.module.cluster.hazelcast;

import java.io.IOException;
import java.util.List;

import be.nabu.eai.developer.MainController;
import be.nabu.eai.developer.managers.base.BaseJAXBGUIManager;
import be.nabu.eai.repository.resources.RepositoryEntry;
import be.nabu.libs.property.api.Property;
import be.nabu.libs.property.api.Value;

public class HazelcastClusterGUIManager extends BaseJAXBGUIManager<HazelcastClusterConfiguration, HazelcastCluster> {

	public HazelcastClusterGUIManager() {
		super("Hazelcast Cluster", HazelcastCluster.class, new HazelcastClusterManager(), HazelcastClusterConfiguration.class);
	}

	@Override
	protected List<Property<?>> getCreateProperties() {
		return null;
	}

	@Override
	protected HazelcastCluster newInstance(MainController controller, RepositoryEntry entry, Value<?>... values) throws IOException {
		return new HazelcastCluster(entry.getId(), entry.getContainer(), entry.getRepository());
	}

	@Override
	public String getCategory() {
		return "Environments";
	}
	
}
