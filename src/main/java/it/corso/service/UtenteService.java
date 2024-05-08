package it.corso.service;

import java.util.List;

import it.corso.dto.UtenteAggiornamentoDto;
import it.corso.dto.UtenteAggiornamentoProvaDto;
import it.corso.dto.UtenteDto;
import it.corso.dto.UtenteEliminazioneDto;
import it.corso.dto.UtenteIscrizioneCorsoDto;
import it.corso.dto.UtenteLoginRequestDto;
import it.corso.dto.UtenteRegistrazioneDto;
import it.corso.model.Utente;


public interface UtenteService {
	void registrazioneUtente(UtenteRegistrazioneDto utenteRegistrazioneDto);
	UtenteDto selezionaUtenteDaEmail(String email);
	Utente findByEmail(String email);
	boolean existsByEmail(String email);
	void aggiornaUtente(UtenteAggiornamentoDto utenteDto);
	void aggiornaRuoliUtente(String email, UtenteAggiornamentoProvaDto utenteDto);
	void eliminazioneUtenteDaEmail(UtenteEliminazioneDto utenteDto);
	List<UtenteDto> selezionaUtenti();
	boolean loginUtente(UtenteLoginRequestDto utenteLoginDto);
	void iscriviUtenteCorso(UtenteIscrizioneCorsoDto utenteCorsoDto);
}
