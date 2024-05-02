package it.corso.service;

import java.util.List;

import it.corso.dto.CategoriaCreazioneDto;
import it.corso.dto.CategoriaDto;

public interface CategoriaService {
	CategoriaDto getCategoria(int id);
	List<CategoriaDto> getAllWithFilter(String filterName);
	void createCategoria(CategoriaCreazioneDto categoriaDto);
	
	void deleteCategoria(int id);
	boolean existCategoriaById(int id);
	}
