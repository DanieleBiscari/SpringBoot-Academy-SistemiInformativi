package it.corso;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import it.corso.jwt.JWTTokenNeededFilter;
import jakarta.ws.rs.ApplicationPath;

/*
 * facciamo capire a Spring che questa classe è un componente di Spring
 * inseriamo il path iniziale delle API
 */
@Component
@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig {
	
	public JerseyConfig() {
		register(JWTTokenNeededFilter.class);
		packages("it.corso");
	}
}
