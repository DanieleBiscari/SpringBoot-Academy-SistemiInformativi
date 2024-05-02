package it.corso.jwt;

import java.io.IOException;
import java.security.Key;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.corso.service.BlackList;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@JWTTokenNedeed
@Provider
public class JWTTokenNeededFilter implements ContainerRequestFilter {
	// iniettiamo il contesto
	@Context // -> simile all'autowired, prende le informazioni all'interno della classe
	private ResourceInfo resourceInfo;
	
	@Autowired
	private BlackList blackList;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// verifichiamo se c'è l'annotation nei metodi
		Secured annotationRole = resourceInfo.getResourceMethod().getAnnotation(Secured.class);
		if(annotationRole == null) {
			// verifichiamo se c'è l'annotation nelle classi
			annotationRole = resourceInfo.getResourceClass().getAnnotation(Secured.class);
		}
		
		// quello che estraiamo dall'header è il nostro token che sarà un Bearer Token
		String authorizationHeader =  requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new NotAuthorizedException("Authorization header must be provided");
		}
		
		
		// estrae il token dall'HTTP authorization header
		String token = authorizationHeader.substring("Bearer".length()).trim();
		
		// -> Implementazione incontro Sistemi Informativi 24/04/2024 
		if(blackList.isTokenRevoked(token)) {
			throw new NotAuthorizedException("Authorization header must be provided");
		}
		//<-
		
		try {
			byte[] secretKey = "ciaoabcdefghi123456781111111111111111".getBytes();
			// crittografia
			Key key =  Keys.hmacShaKeyFor(secretKey);
			Jws<Claims> claims =  Jwts.parserBuilder()
									.setSigningKey(key)
									.build()
									.parseClaimsJws(token);
			Claims body = claims.getBody();
			List<String> ruoliToken = body.get("ruoli", List.class);
			
			// facciamo un controllo per verificare se il ruolo dell'annotation è contenuto dentro la lista dei ruoli 
			Boolean hasRole = false;
			for(String ruolo: ruoliToken) {
				if(ruolo.equals(annotationRole.role())) {
					hasRole = true;
				}
			}
			if(!hasRole) {
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
			}
		} catch (Exception e) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}
	}
	
}
