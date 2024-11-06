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
