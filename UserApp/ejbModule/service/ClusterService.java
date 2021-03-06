package service;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import model.Host;

@Singleton
public class ClusterService {

	private HashMap<String, Host> hosts;
	private int i;

	@PostConstruct
	private void init() {
		i = 0;
		hosts = new HashMap<>();
	}
	
	public void add(Host host) {		
		host.setAlias(++i+"");
		for(Host h: hosts.values()) {
			((ResteasyWebTarget)ClientBuilder.newClient().
					target("http://"+h.getAddress()+"/ChatAppWar/rest/cluster")).request().post(Entity.json(h));
		}
		
		hosts.put(host.getAlias(), host);
		System.out.println("registered");
	}

	public void remove(String alias) {
		hosts.remove(alias);
		for(Host h: hosts.values()) {
			((ResteasyWebTarget)ClientBuilder.newClient().
					target("http://"+h.getAddress()+"/ChatAppWar/rest/cluster/" +alias)).request().delete();
		}
	}
	
	public HashMap<String, Host> getHosts() {
		System.out.println("GET HOSTS: " + hosts);
		return hosts;
	}
	
	public String getHost(Host host) {
		System.out.println("Host: ======> " + host);
		for(String key: hosts.keySet()) {
			System.out.println("Key ====>" + key);
			if(hosts.get(key).getAddress().equals(host.getAddress()))
				return key;
		}
		return null;
	}
}
