package it.corso.dto;

import java.util.List;

import jakarta.validation.constraints.Pattern;

public class UtenteAggiornamentoDto {
	@Pattern(regexp = "[a-zA-Z\\s']{5,50}", message = "nome non corretto")
	private String nome;
	@Pattern(regexp = "[a-zA-Z\\s']{5,50}", message = "nome non corretto")
	private String cognome;
	@Pattern(regexp = "[A-Za-z0-9\\.\\+_-]+@[A-Za-z0-9\\._-]+\\.[A-Za-z]{2,24}", message = "email non corretta")
	private String email;
	// in questo caso non dichiarando una lista di ruoli in input potremmo assegnare
	// solamente un ruolo per utente
	private List<Integer> idRuoli;
	
	
	
	public List<Integer> getIdRuoli() {
		return idRuoli;
	}

	public void setIdRuoli(List<Integer> idRuoli) {
		this.idRuoli = idRuoli;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
