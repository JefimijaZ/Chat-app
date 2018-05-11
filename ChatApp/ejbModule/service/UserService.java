package service;

import java.util.HashMap;

import javax.ejb.EJB;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import model.Host;
import model.User;

public class UserService {
	
	
	@EJB
	private ClusterService clusterService;
	
	public void login(User user) {
		HashMap<String, Host> hosts = clusterService.getHosts();
		for(String alias: hosts.keySet()) {
			((ResteasyWebTarget)ClientBuilder.newClient().
					target("http://"+hosts.get(alias).getAddress()+"/ChatAppWar/rest/users/login")).request().post(Entity.json(user));
			
		}
	}
	
	public void logout(User user) {
		HashMap<String, Host> hosts = clusterService.getHosts();
		for(String alias: hosts.keySet()) {
			((ResteasyWebTarget)ClientBuilder.newClient().
					target("http://"+hosts.get(alias).getAddress()+"/ChatAppWar/rest/users/logout")).request().post(Entity.json(user));
			
		}
	}

}
