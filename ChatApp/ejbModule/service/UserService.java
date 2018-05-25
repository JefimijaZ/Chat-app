package service;

import java.util.HashMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.client.ClientBuilder;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import cluster.ClusterInit;
import model.Configuration;
import model.Host;

@Stateless
public class UserService {

	@EJB
	private ClusterService clusterService;

	public void login(String user) {
		HashMap<String, Host> hosts = clusterService.getHosts();
		System.out.println("host kada treba da se javi da se loginovo novi user. size " + hosts.size());
		if (hosts !=null && hosts.size() > 0) {
			for (Host alias : hosts.values()) {
//				System.out.println("Javila sam hostu : " + hosts.get(alias).getAddress());
				((ResteasyWebTarget) ClientBuilder.newClient()
						.target("http://" +alias.getAddress() + "/ChatAppWar/rest/users/login/" + user)).request()
								.get();
				System.out.println("Javljam da se loginovo novi user: " + user);

			}
		}
	}

	public void logout(String username) {
		HashMap<String, Host> hosts = clusterService.getHosts();
		if (hosts !=null && hosts.size() > 0 ) {
			for (String alias : hosts.keySet()) {
				((ResteasyWebTarget) ClientBuilder.newClient()
						.target("http://" + hosts.get(alias).getAddress() + "/ChatAppWar/rest/users/logout/" + username))
								.request().get();
				System.out.println("Javljam da se logoutovao novi user: " + username);
				System.out.println("Javila sam hostu za logout : " + hosts.get(alias).getAddress());

			}
		}
	}

}
