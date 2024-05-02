package it.corso.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import it.corso.dto.CategoriaCreazioneDto;
import it.corso.dto.CategoriaDto;
import it.corso.service.CategoriaService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/categoria")
public class CategoriaController {

	@Autowired
	private CategoriaService categoriaService;

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategoriaById(@PathParam("id") int id) {
		try {
			CategoriaDto categoriaDto = categoriaService.getCategoria(id);
			if (categoriaDto != null) {
				return Response.ok().entity(categoriaDto).build();
			}
			return Response.status(Response.Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	public Response getAllCategorieWithFilter(@QueryParam("nome") String filterName) {
		try {
			if(filterName != null && !filterName.isEmpty()) {
				List<CategoriaDto> categoriaDto = categoriaService.getAllWithFilter(filterName);
				return Response.status(Response.Status.OK).entity(categoriaDto).build();
			}
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/create")
	public Response creaCategoria(@RequestBody CategoriaCreazioneDto categoriaCreazioneDto) {
		try {
			categoriaService.createCategoria(categoriaCreazioneDto);
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	
	@DELETE
	@Path("/{id}")
	public Response eliminaCategoria(@PathParam("id") int id) {
		try {
			categoriaService.deleteCategoria(id);
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

}
