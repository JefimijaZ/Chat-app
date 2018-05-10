package cluster;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ws.rs.client.ClientBuilder;

import org.jboss.resteasy.util.GenericType;

import model.Configuration;
import model.Host;
import model.User;

@Singleton
@Startup
public class ClusterInit {

	private String alias;
	private List<User> activeUsers;
	private HashMap<String, Host> hosts;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {

		if (!Configuration.masterAdress.equals(Configuration.localAdress)) {
			System.out.println("Not master");
			alias = (ClientBuilder.newClient().target("http://" + Configuration.masterAdress + "/UserAppWar/rest/cluster"))
					.request().post(null).readEntity(String.class);
			System.out.println("registered");
			hosts = (ClientBuilder.newClient().target("http://" + Configuration.masterAdress + "/UserAppWar/rest/cluster")).request()
					.get().readEntity((Class<HashMap<String, Host>>)(Class<?>)HashMap.class);
			System.out.println("HOSTS: " + hosts.size());
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
}
