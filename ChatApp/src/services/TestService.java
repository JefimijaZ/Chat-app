package services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import message.JMSTopic;

@Path("/demo")
public class TestService {

	/**
	 * Testira REST sistem. URL izgleda ovako: <code>rest/demo/test</code>
	 * 
	 * @return Vraæa tekst da vidimo da sve radi.
	 */
	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
		new JMSTopic();
		return "REST is working.";
	}


}
