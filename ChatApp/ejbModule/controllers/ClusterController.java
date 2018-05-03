package controllers;

import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import model.Host;
import service.ClusterService;

@Path("/cluster")
@Stateless
public class ClusterController {

	@EJB
	ClusterService clusterService;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void add(Host host) {
		clusterService.add(host);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public HashMap<String, Host> getHosts(){
		return clusterService.getHosts();
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{alias}")
	public void remove(@PathParam("alias") String alias) {
		clusterService.remove(alias);
	}

}
