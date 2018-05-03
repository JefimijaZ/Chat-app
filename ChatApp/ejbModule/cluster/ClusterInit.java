package cluster;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ws.rs.client.ClientBuilder;

import org.jboss.resteasy.util.GenericType;

import model.Const;
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

		if (!Const.masterNode.equals("master")) {
			System.out.println("Not master");
			alias = (ClientBuilder.newClient().target("http://" + Const.masterAdress + "/UserAppWar/rest/cluster"))
					.request().post(null).readEntity(String.class);
			System.out.println("registered");
			hosts = (ClientBuilder.newClient().target("http://" + Const.masterAdress + "/UserAppWar/cluster")).request()
					.get().readEntity((Class<HashMap<String, Host>>)(Class<?>)HashMap.class);
			activeUsers = (ClientBuilder.newClient().target("http://" + Const.masterAdress + "/UserAppWar/user/active"))
					.request().get().readEntity(List.class);
		}
	}

	@PreDestroy
	public void destroy() {
		if (!Const.masterNode.equals("master")) {
			(ClientBuilder.newClient().target("http://" + Const.masterAdress + "/UserAppWar/rest/cluster/" + alias))
					.request().delete();
		}
	}
}
