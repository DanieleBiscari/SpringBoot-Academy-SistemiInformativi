package it.corso.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.corso.dto.UtenteAggiornamentoDto;
import it.corso.dto.UtenteAggiornamentoProvaDto;
import it.corso.dto.UtenteDto;
import it.corso.dto.UtenteIscrizioneCorsoDto;
import it.corso.dto.UtenteLoginRequestDto;
import it.corso.dto.UtenteLoginResponseDto;
import it.corso.dto.UtenteRegistrazioneDto;
import it.corso.jwt.JWTTokenNedeed;
import it.corso.jwt.Secured;
import it.corso.model.Ruolo;
import it.corso.model.Utente;
import it.corso.service.BlackList;
import it.corso.service.UtenteService;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Path("/utente")
public class UtenteController {

	@Autowired
	private UtenteService utenteService;

	@Autowired
	private BlackList blackList;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/registrazione")
	public Response registrazioneUtente(@Valid @RequestBody UtenteRegistrazioneDto utenteDto) {
		try {
			if (!Pattern.matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,20}",
					utenteDto.getPassword())) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
			if (utenteService.existsByEmail(utenteDto.getEmail())) {
				return Response.status(Response.Status.BAD_REQUEST).build();
			}

			utenteService.registrazioneUtente(utenteDto);
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@DELETE
	@Path("/elimina/{email}")
	public Response eliminaUtente(@PathParam("email") String email) {
		try {
			utenteService.eliminazioneUtenteDaEmail(email);
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@Secured(role = "Admin")
	@JWTTokenNedeed
	@GET
	@Path("/getUtente")
	@Produces(MediaType.APPLICATION_JSON)
	public Response selezionaUtenteDaEmail(@QueryParam("email") String email) {

		try {
			if (email != null && !email.isEmpty()) {
				UtenteDto utenteDto = utenteService.selezionaUtenteDaEmail(email);
				if (utenteDto != null) {
					return Response.ok().entity(utenteDto).build();
				}
			}

			return Response.status(Response.Status.BAD_REQUEST).build();

		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@PUT
	@Path("/aggiorna")
	public Response aggiornaUtente(@Valid @RequestBody @QueryParam("email") String email,
			UtenteAggiornamentoDto utenteDto) {
		try {
			utenteService.aggiornaUtente(email, utenteDto);
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@PUT
	@Path("/aggiornaProva")
	public Response aggiornaProvaUtente(@Valid @RequestBody @QueryParam("email") String email,
			UtenteAggiornamentoProvaDto utenteDto) {
		try {
			utenteService.aggiornaRuoliUtente(email, utenteDto);
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}

	@GET
	@Path("/getUtenti")
	@Produces(MediaType.APPLICATION_JSON)
	public Response selezionaUtenti() {
		try {
			List<UtenteDto> listUtenteDto = utenteService.selezionaUtenti();
			return Response.status(Response.Status.OK).entity(listUtenteDto).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response loginUtente(@RequestBody UtenteLoginRequestDto utenteLoginDto) {
		try {
			if (utenteService.loginUtente(utenteLoginDto)) {
				return Response.ok(creaToken(utenteLoginDto.getEmail())).build();
			}
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	// token che serve per proteggere l'endpoint
	private UtenteLoginResponseDto creaToken(String email) {
		// eseguiamo una cifratura attraverso l'algoritmo di crittografia HMAC
		byte[] secretKey = "ciaoabcdefghi123456781111111111111111".getBytes();
		// crittografia
		Key key = Keys.hmacShaKeyFor(secretKey);
		Utente utente = utenteService.findByEmail(email);

		// passiamo delle proprietà al token utilizzando una mappa
		Map<String, Object> map = new HashMap<>();
		map.put("email", email);
		map.put("nome", utente.getNome());
		map.put("cognome", utente.getCognome());

		List<String> ruoli = new ArrayList<>();
		for (Ruolo ruolo : utente.getRuoli()) {
			ruoli.add(ruolo.getTipologia().name()); // -> .name() restituisce il nome della enum in string, si poteva
													// usare anche .toString()
		}

		map.put("ruoli", ruoli);
		// data di creazione
		Date dataCreazione = new Date();
		// ttl(TimeToLive) del token
		Date dataFine = Timestamp.valueOf(LocalDateTime.now().plusMinutes(15L));

		// creazione del token firmato con la chiave creata sopra
		String jwtToken = Jwts.builder().setClaims(map).setIssuer("http://localhost:8080").setIssuedAt(dataCreazione)
				.setExpiration(dataFine).signWith(key).compact(); // -> codifica il token in una stringa

		UtenteLoginResponseDto token = new UtenteLoginResponseDto();
		token.setToken(jwtToken);
		token.setTokenCreationTime(dataCreazione);
		token.setTtl(dataFine);

		return token;
	}

	// -> Implementazione incontro Sistemi Informativi 24/04/2024
	// Redis: per cachare le risposte di backend, memoria tampone perchè molto
	// veloce
	@GET
	@Path("/logout")
	public Response logout(ContainerRequestContext containerRequestContext) {
		try {
			String authorizationHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
			String token = authorizationHeader.substring("Bearer".length()).trim();
			blackList.invalidateToken(token);
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	// <-

	@POST
	@Path("/iscrizioneCorso")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response iscrizioneCorso(UtenteIscrizioneCorsoDto utenteIscrizioneCorsoDto) {
		try {
			utenteService.iscriviUtenteCorso(utenteIscrizioneCorsoDto);
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
}
