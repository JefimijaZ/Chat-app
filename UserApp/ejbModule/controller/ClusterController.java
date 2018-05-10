package controller;

import java.util.HashMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import model.Host;
import service.ClusterService;

@Path("/cluster")
@Stateless
public class ClusterController {

	@EJB
	ClusterService clusterService;
	@Context
	private HttpServletRequest request;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public String add() {
		Host host = new Host(request.getRemoteAddr() + ":" + request.getServerPort(), "");
		System.out.println("HOST ADD : " + host);
		clusterService.add(host);
		return host.getAlias();
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{alias}")
	public void remove(@PathParam("alias") String alias) {
		clusterService.remove(alias);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, Host> getHosts(){
		return clusterService.getHosts();
	}

}
