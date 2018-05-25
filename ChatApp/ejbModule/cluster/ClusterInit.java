package cluster;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ws.rs.client.ClientBuilder;

import model.Configuration;
import model.Host;
import model.User;
import service.ClusterService;

@Singleton
@Startup
public class ClusterInit {

	private String alias;
	private List<User> activeUsers;
	private HashMap<String, Host> hosts;
	
	@EJB
	ClusterService service;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {

		if (!Configuration.masterAdress.equals(Configuration.localAdress)) {
			System.out.println("*****************Not master************************");
			alias = (ClientBuilder.newClient().target("http://" + Configuration.masterAdress + "/UserAppWar/rest/cluster/" + Configuration.localAdress.split(":")[1]))
					.request().get().readEntity(String.class);
			System.out.println("registered alias " + alias);
			
			hosts = (ClientBuilder.newClient().target("http://" + Configuration.masterAdress + "/UserAppWar/rest/cluster")).request()
					.get().readEntity((Class<HashMap<String, Host>>)(Class<?>)HashMap.class);
//			service.setHosts(hosts);
			System.out.println("HOSTS: " + hosts);
			activeUsers = (ClientBuilder.newClient().target("http://" + Configuration.masterAdress + "/UserAppWar/rest/user/active"))
					.request().get().readEntity(List.class);
			System.out.println("ACTIVE USERS: " + activeUsers.size());
		}
	}

	@PreDestroy
	public void destroy() {
		if (!Configuration.masterAdress.equals(Configuration.localAdress)) {
			System.out.println("unregistered");
			(ClientBuilder.newClient().target("http://" + Configuration.masterAdress + "/UserAppWar/rest/cluster/" + alias))
					.request().delete();
		}
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public List<User> getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(List<User> activeUsers) {
		this.activeUsers = activeUsers;
	}

	public HashMap<String, Host> getHosts() {
		return hosts;
	}

	public void setHosts(HashMap<String, Host> hosts) {
		this.hosts = hosts;
	}
	
	
	
	
}
