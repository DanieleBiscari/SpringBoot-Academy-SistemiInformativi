package it.corso.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.corso.dao.CorsoDao;
import it.corso.dao.RuoloDao;
import it.corso.dao.UtenteDao;
import it.corso.dto.UtenteAggiornamentoDto;
import it.corso.dto.UtenteAggiornamentoProvaDto;
import it.corso.dto.UtenteDto;
import it.corso.dto.UtenteEliminazioneDto;
import it.corso.dto.UtenteIscrizioneCorsoDto;
import it.corso.dto.UtenteLoginRequestDto;
import it.corso.dto.UtenteRegistrazioneDto;
import it.corso.model.Corso;
import it.corso.model.Ruolo;
import it.corso.model.Utente;

@Service
public class UtenteServiceImpl implements UtenteService {
	private ModelMapper modelMapper = new ModelMapper();
	
	@Autowired
	private UtenteDao utenteDao;
	@Autowired
	private RuoloDao ruoloDao;
	@Autowired
	private CorsoDao corsoDao;
	
//  -> CONSTRUCTOR INJECTION	
//	public UtenteServiceImpl(UtenteDao utenteDao) {
//		this.utenteDao = utenteDao;
//	}
	
	// CREATE
	@Override
	public void registrazioneUtente(UtenteRegistrazioneDto utenteRegistrazioneDto) {
		Utente utente = new Utente();
		utente.setNome(utenteRegistrazioneDto.getNome());
		utente.setCognome(utenteRegistrazioneDto.getCognome());
		utente.setEmail(utenteRegistrazioneDto.getEmail());
		String sha256hex = DigestUtils.sha256Hex(utenteRegistrazioneDto.getPassword());
		utente.setPassword(sha256hex);
		utenteDao.save(utente); // -> aggiunge o modifica un record nel DB
	}

	@Override
	public boolean existsByEmail(String email) {
		return utenteDao.existsByEmail(email);
	}
	

	// READ
	@Override
	public UtenteDto selezionaUtenteDaEmail(String email) {
		Utente utente = utenteDao.findByEmail(email);
		UtenteDto utenteDto = modelMapper.map(utente, UtenteDto.class);
		return utenteDto;
	}

	// UPDATE
	@Override
	public void aggiornaUtente(UtenteAggiornamentoDto utenteDto) {
		Utente utente = utenteDao.findByEmail(utenteDto.getEmail());
		
		if (utente != null) { 
			utente.setNome(utenteDto.getNome());
			utente.setCognome(utenteDto.getCognome());
			utente.setEmail(utenteDto.getEmail());
			List<Ruolo> ruoliUtente = new ArrayList<>();
			
			for(Integer idRuolo: utenteDto.getIdRuoli()) {
				Optional<Ruolo> ruoloDb = ruoloDao.findById(idRuolo);
				if(ruoloDb.isPresent()) {
					Ruolo newRuolo = ruoloDb.get();
					ruoliUtente.add(newRuolo);
				}
			}
			utente.setRuoli(ruoliUtente);
			
//			Optional<Ruolo> ruoloDb = ruoloDao.findById(utenteDto.getIdRuolo());
//			if(ruoloDb.isPresent()) {
//				Ruolo ruolo = ruoloDb.get();
//				// ruolo.setId(utenteDto.getIdRuolo());
//				ruoliUtente.add(ruolo);
//				utente.setRuoli(ruoliUtente);
//			}
//			
			
			utenteDao.save(utente);
		}

	}
	
	@Override
	public void aggiornaRuoliUtente(String email, UtenteAggiornamentoProvaDto utenteDto) {
		Utente utente = utenteDao.findByEmail(email);
		if(utente != null) {
			List<Ruolo> listaRuolo = new ArrayList<>();
			for(Integer idRuolo: utenteDto.getIdRuoli()) {
				Optional<Ruolo> ruoloDb = ruoloDao.findById(idRuolo);
				if(ruoloDb.isPresent()) {
					Ruolo newRuolo = ruoloDb.get();
					listaRuolo.add(newRuolo);
				}
			}
			
			utente.setNome(utenteDto.getNome());
			utente.setCognome(utenteDto.getCognome());
			utente.setEmail(utenteDto.getEmail());
			utente.setRuoli(listaRuolo);
			
			utenteDao.save(utente);
		}
		
	}

	// DELETE
	@Override
	public void eliminazioneUtenteDaEmail(UtenteEliminazioneDto utenteDto) {
		Utente utente = utenteDao.findByEmail(utenteDto.getEmail());
		Optional<Utente> utenteOptional = utenteDao.findById(utente.getId());
		if (utenteOptional.isPresent()) {
			utenteDao.delete(utenteOptional.get());
		}
//		if(utente != null) {
//			utenteDao.delete(utente);
//		}
	}

	@Override
	public List<UtenteDto> selezionaUtenti() {
		List<Utente> listaUtenti = (List<Utente>) utenteDao.findAll();
		List<UtenteDto> listaUtentiDto = new ArrayList<>();
		listaUtenti.forEach(u -> listaUtentiDto.add(modelMapper.map(u, UtenteDto.class)));
		//List<Utente> listaUtenti  = utenteDao.findAll();
		return listaUtentiDto;
	}

	@Override
	public boolean loginUtente(UtenteLoginRequestDto utenteLoginDto) {
		Utente utente = new Utente();
		utente.setEmail(utenteLoginDto.getEmail());
		utente.setPassword(utenteLoginDto.getPassword());
		String passwordHash = DigestUtils.sha256Hex(utente.getPassword());
		Utente credenzialiUtente = utenteDao.findByEmailAndPassword(utente.getEmail(), passwordHash);
		
		return credenzialiUtente != null ? true : false;
	}

	@Override
	public Utente findByEmail(String email) {
		return utenteDao.findByEmail(email);
	}

	@Override
	public void iscriviUtenteCorso(UtenteIscrizioneCorsoDto utenteCorsoDto) {
		Utente utente = utenteDao.findByEmail(utenteCorsoDto.getEmailUtente());
		Corso corso = corsoDao.findByNomeCorso(utenteCorsoDto.getNomeCorso());
		List<Corso> corsi = utente.getCorsi();
		corsi.add(corso);
		utente.setCorsi(corsi);
		utenteDao.save(utente);
	}

}
