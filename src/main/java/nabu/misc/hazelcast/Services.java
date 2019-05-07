package nabu.misc.hazelcast;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

import be.nabu.eai.module.cluster.hazelcast.HazelcastCluster;
import be.nabu.eai.repository.EAIResourceRepository;
import be.nabu.eai.repository.api.cluster.ClusterMember;
import be.nabu.libs.artifacts.api.Artifact;
import nabu.misc.hazelcast.types.Member;

@WebService
public class Services {
	
	public List<Member> members(@WebParam(name = "clusterId") String clusterId) {
		List<Member> members = new ArrayList<Member>();
		Artifact resolve = EAIResourceRepository.getInstance().resolve(clusterId);
		if (resolve instanceof HazelcastCluster) {
			for (ClusterMember clusterMember : ((HazelcastCluster) resolve).getMembers()) {
				Member member = new Member();
				member.setHost(clusterMember.getAddress().getHostName());
				member.setPort(clusterMember.getAddress().getPort());
				members.add(member);
			}
		}
		return members;
	}
}
