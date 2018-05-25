package service;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;

import model.Host;

@Singleton
public class ClusterService {

	private HashMap<String, Host> clusters;

	@PostConstruct
	private void init() {
		clusters = new HashMap<>();
	}
	
	public void add(Host host) {
		System.out.println("registered host in cluster service is......................" + host);
		clusters.put(host.getAlias(), host);
	}

	public void remove(String alias) {
		clusters.remove(alias);
	}

	public HashMap<String, Host> getHosts() {		
		return clusters;
	}

	
	public void setHosts(HashMap<String, Host> clusters) {
		this.clusters = clusters;
	}
	
	
}
