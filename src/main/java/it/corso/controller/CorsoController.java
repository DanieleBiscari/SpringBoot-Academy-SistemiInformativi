package it.corso.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import it.corso.dto.CorsoCreazioneDto;
import it.corso.dto.CorsoDto;
import it.corso.service.CorsoService;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/corso")
public class CorsoController {

	@Autowired
	private CorsoService corsoService;

	@GET
	@Path("/getCorsi")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCourses() {
		try {
			List<CorsoDto> listaCorsi = corsoService.getCourses();
			return Response.status(Response.Status.OK).entity(listaCorsi).build();

		} catch (Exception e) {

			return Response.status(Response.Status.BAD_REQUEST).entity("Errore caricamento utenti").build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/create")
	public Response createCourse(@Valid @RequestBody CorsoCreazioneDto corsoDto) {
		try {
			corsoService.createCourse(corsoDto);
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

}
