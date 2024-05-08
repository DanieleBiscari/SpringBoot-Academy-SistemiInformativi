package it.corso.service;

import java.util.List;

import it.corso.dto.CorsoCreazioneDto;
import it.corso.dto.CorsoDto;

public interface CorsoService {
	List<CorsoDto> getCourses();
	void createCourse(CorsoCreazioneDto corsoDto);
}
