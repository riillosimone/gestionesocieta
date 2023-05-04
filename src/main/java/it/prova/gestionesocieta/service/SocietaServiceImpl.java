package it.prova.gestionesocieta.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.gestionesocieta.exception.SocietaConDipendentiException;
import it.prova.gestionesocieta.model.Societa;
import it.prova.gestionesocieta.repository.SocietaRepository;

@Service
public class SocietaServiceImpl implements SocietaService {

	@PersistenceContext
	private EntityManager entityManager;

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
	public void delete(Long idSocieta) {
		Societa societaInstance = societaRepository.findByIdEager(idSocieta);
		if (societaInstance.getDipendenti().size() > 0) {
			throw new SocietaConDipendentiException("Attenzione! Stai rimuovendo una societa con dipendenti associati");
		}
		societaRepository.deleteById(idSocieta);
	}

	@Override
	public List<Societa> findByExample(Societa example) {

		Map<String, Object> paramaterMap = new HashMap<String, Object>();
		List<String> whereClauses = new ArrayList<String>();

		StringBuilder queryBuilder = new StringBuilder("select s from Societa s where s.id = s.id ");

		if (StringUtils.isNotEmpty(example.getRagioneSociale())) {
			whereClauses.add(" s.ragioneSociale  like :ragioneSociale ");
			paramaterMap.put("ragioneSociale", "%" + example.getRagioneSociale() + "%");
		}
		if (StringUtils.isNotEmpty(example.getIndirizzo())) {
			whereClauses.add(" s.indirizzo like :indirizzo ");
			paramaterMap.put("indirizzo", "%" + example.getIndirizzo() + "%");
		}

		if (example.getDataFondazione() != null) {
			whereClauses.add("s.dataFondazione >= :dataFondazione ");
			paramaterMap.put("dataFondazione", example.getDataFondazione());
		}

		queryBuilder.append(!whereClauses.isEmpty() ? " and " : "");
		queryBuilder.append(StringUtils.join(whereClauses, " and "));
		TypedQuery<Societa> typedQuery = entityManager.createQuery(queryBuilder.toString(), Societa.class);

		for (String key : paramaterMap.keySet()) {
			typedQuery.setParameter(key, paramaterMap.get(key));
		}

		return typedQuery.getResultList();
	}

	@Override
	public Societa caricaSingolaSocietaEager(Long id) {
		return societaRepository.findByIdEager(id);
	}

	@Override
	public List<Societa> listaSocietaDistinteConDipendenteRedditoMaggiore(int redditoMinimo) {
		return societaRepository.findAllDistinctByDipendenti_RedditoAnnuoLordoGreaterThan(redditoMinimo);
	}

}
