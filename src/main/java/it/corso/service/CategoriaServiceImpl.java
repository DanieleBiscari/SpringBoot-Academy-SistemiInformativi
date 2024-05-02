package it.corso.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.corso.dao.CategoriaDao;
import it.corso.dto.CategoriaCreazioneDto;
import it.corso.dto.CategoriaDto;
import it.corso.model.Categoria;
import it.corso.model.NomeCategoria;

@Service
public class CategoriaServiceImpl implements CategoriaService {
	private ModelMapper modelMapper = new ModelMapper();

	@Autowired
	private CategoriaDao categoriaDao;

	@Override
	public CategoriaDto getCategoria(int id) {
		Optional<Categoria> categoria = categoriaDao.findById(id);
		if (categoria.isPresent()) {
			CategoriaDto categoriaDto = modelMapper.map(categoria, CategoriaDto.class);
			return categoriaDto;
		}

		return null;
	}

	@Override
	public List<CategoriaDto> getAllWithFilter(String filterName) {
		List<Categoria> listaCategoria = (List<Categoria>) categoriaDao.findAll();
		List<CategoriaDto> listaCategoriaDto = new ArrayList<>();
		
		String[] nomi = filterName.split(",");
		for(String nome: nomi) {
			listaCategoria.forEach(c -> {
				if (c.getNomeCategoria().name().equals(nome)) {
					listaCategoriaDto.add(modelMapper.map(c, CategoriaDto.class));
				}
			});
		}
		return listaCategoriaDto;
	}

	@Override
	public void createCategoria(CategoriaCreazioneDto categoriaCreazioneDto) {
		Categoria newCategoria = new Categoria();

		for (NomeCategoria categoria : NomeCategoria.values()) {
			if (categoria.name().equalsIgnoreCase(categoriaCreazioneDto.getNomeCategoria())) {
				newCategoria.setNomeCategoria(categoria);
				if (!categoriaDao.existsByNomeCategoria(categoria)) {
					categoriaDao.save(newCategoria);
				}
			}
		}
	}
	
	@Override
	public void deleteCategoria(int id) {
		Optional<Categoria> categoriaOptional = categoriaDao.findById(id);
		if (categoriaOptional.isPresent()) {
			categoriaDao.delete(categoriaOptional.get());
		}
		
	}

	@Override
	public boolean existCategoriaById(int id) {
		return categoriaDao.existsById(id);
	}


}
