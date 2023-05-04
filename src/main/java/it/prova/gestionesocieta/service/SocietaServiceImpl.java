package it.prova.gestionesocieta.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.gestionesocieta.model.Societa;
import it.prova.gestionesocieta.repository.SocietaRepository;

@Service
public class SocietaServiceImpl implements SocietaService{

	@Autowired
	private SocietaRepository societaRepository;

	@Transactional(readOnly = true)
	public List<Societa> listAllSocieta() {
		return (List<Societa>) societaRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Societa caricaSingolaSocieta(Long id) {
		return societaRepository.findById(id).orElse(null);
	}

	@Transactional
	public void aggiorna(Societa societaInstance) {
		societaRepository.save(societaInstance);
	}

	@Transactional
	public void inserisciNuovo(Societa societaInstance) {
		societaRepository.save(societaInstance);		
	}

	@Transactional
	public void delete(Societa societaInstance) {
		societaRepository.delete(societaInstance);		
	}
}
