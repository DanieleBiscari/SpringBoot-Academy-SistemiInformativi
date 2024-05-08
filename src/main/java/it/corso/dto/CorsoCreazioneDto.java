package it.corso.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public class CorsoCreazioneDto {
	@Pattern(regexp = "[a-zA-Z0-9\\s']{5,20}", message = "nome non corretto")
	private String nomeCorso;
	@Pattern(regexp = "[a-zA-Z\\s']{0,50}", message = "descrizione non corretta")
	private String descrizioneBreve;
	@Pattern(regexp = "[a-zA-Z\\s']{0,250}", message = "descrizione non corretta")
	private String descrizioneCompleta;
	@Min(value = 0, message = "La durata non può essere negativa") 
	@Max(value = 30, message = "La durata non poò essere maggiore di 5 anni")
	private int durata;
	
	
	public String getNomeCorso() {
		return nomeCorso;
	}
	public void setNomeCorso(String nomeCorso) {
		this.nomeCorso = nomeCorso;
	}
	public String getDescrizioneBreve() {
		return descrizioneBreve;
	}
	public void setDescrizioneBreve(String descrizioneBreve) {
		this.descrizioneBreve = descrizioneBreve;
	}
	public String getDescrizioneCompleta() {
		return descrizioneCompleta;
	}
	public void setDescrizioneCompleta(String descrizioneCompleta) {
		this.descrizioneCompleta = descrizioneCompleta;
	}
	public int getDurata() {
		return durata;
	}
	public void setDurata(int durata) {
		this.durata = durata;
	}
}

