package it.prova.gestionesocieta.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.gestionesocieta.model.Dipendente;
import it.prova.gestionesocieta.repository.DipendenteRepository;

@Service
public class DipendenteServiceImpl implements DipendenteService {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private DipendenteRepository dipendenteRepository;

	@Transactional(readOnly = true)
	public List<Dipendente> listAllDipendenti() {
		return (List<Dipendente>) dipendenteRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Dipendente caricaSingoloDipendente(Long id) {
		return dipendenteRepository.findById(id).orElse(null);
	}

	@Transactional
	public void aggiorna(Dipendente dipendenteInstance) {
		dipendenteRepository.save(dipendenteInstance);
	}

	@Transactional
	public void rimuovi(Dipendente dipendenteInstance) {
		dipendenteRepository.delete(dipendenteInstance);
	}

	@Transactional
	public void inserisciNuovo(Dipendente dipendenteInstance) {
		dipendenteRepository.save(dipendenteInstance);
	}

	@Override
	public List<Dipendente> findByExample(Dipendente example) {
		String query = "select d from Dipendente d where d.id is not null ";

		if (StringUtils.isNotEmpty(example.getNome()))
			query += " and d.nome like '%" + example.getNome() + "%' ";
		if (StringUtils.isNotEmpty(example.getCognome()))
			query += " and d.cognome like '%" + example.getCognome() + "%' ";
		if (example.getDataAssunzione()!=null)
			query += " and d.dataassunzione >=" + example.getDataAssunzione();
		if(example.getRedditoAnnuoLordo()!= null && example.getRedditoAnnuoLordo()>0)
			query += " and d.redditoannuolordo = "+ example.getRedditoAnnuoLordo();
		
		return entityManager.createQuery(query, Dipendente.class).getResultList();
	}
}
