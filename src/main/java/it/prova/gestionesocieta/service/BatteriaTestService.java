package it.prova.gestionesocieta.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.prova.gestionesocieta.exception.SocietaConDipendentiException;
import it.prova.gestionesocieta.model.Dipendente;
import it.prova.gestionesocieta.model.Societa;

@Service
public class BatteriaTestService {

	@Autowired
	private SocietaService societaService;
	@Autowired
	private DipendenteService dipendenteService;

	public void testInserimentoSocieta() {
		System.out.println("testInserimentoSocieta INIZIO");
		Long nowInMillisecondi = new Date().getTime();
		Societa primaSocietaInserita = new Societa("La mia prima societa" + nowInMillisecondi,
				"Via " + nowInMillisecondi, LocalDate.now());
		if (primaSocietaInserita.getId() != null) {
			throw new RuntimeException("testInserimentoSocieta FALLITO: Oggetto transiente già valorizzato");
		}
		societaService.inserisciNuovo(primaSocietaInserita);
		if (primaSocietaInserita.getId() == null || primaSocietaInserita.getId() < 1) {
			throw new RuntimeException("testInserimentoSocieta FALLITO: inserimento fallito");
		}
		System.out.println(primaSocietaInserita);
		System.out.println("testInserimentoSocieta........OK");
	}

	public void testFindByExampleSocieta() {
		System.out.println("testFindByExampleSocieta INIZIO");
		Long nowInMillisecondi = new Date().getTime();
		Societa nuovaSocieta = new Societa("Societa example" + nowInMillisecondi, "Via " + nowInMillisecondi,
				LocalDate.now());
		societaService.inserisciNuovo(nuovaSocieta);
		if (nuovaSocieta.getId() == null || nuovaSocieta.getId() < 1) {
			throw new RuntimeException("testFindByExampleSocieta FALLITO: inserimento fallito");
		}
		String exampleRagioneSociale = "example";
		Societa example = new Societa(exampleRagioneSociale, "Via", LocalDate.now());
		List<Societa> listaDiEsempio1 = societaService.findByExample(example);
		if (listaDiEsempio1.size() != 1) {
			throw new RuntimeException("testFindByExampleSocieta FALLITO: le società non sono il numero previsto");
		}
		System.out.println("testFindByExampleSocieta PASSED");

	}

	public void testRimozioneSocieta() {
		System.out.println("testRimozioneSocieta INIZIO");
		Long nowInMillisecondi = new Date().getTime();
		Societa nuovaSocieta = new Societa("Societa " + nowInMillisecondi, "Via " + nowInMillisecondi, LocalDate.now());
		societaService.inserisciNuovo(nuovaSocieta);
		if (nuovaSocieta.getId() == null || nuovaSocieta.getId() < 1) {
			throw new RuntimeException("testRimozioneSocieta FALLITO: inserimento fallito");
		}
		IntStream.range(1, 5).forEach(i -> {
			Dipendente dipendente = new Dipendente("Mario" +i, "Rossi"+i,LocalDate.now(), 30000+(i*2), nuovaSocieta);
			dipendenteService.inserisciNuovo(dipendente);;
		});
		
		try {
			societaService.delete(nuovaSocieta.getId());
			throw new RuntimeException("testRimozioneSocieta FALLITO: non è stata l'eccezione custom");
		} catch (SocietaConDipendentiException e) {
			System.out.println("Catched Custom Exception");
		}
		System.out.println("testRimozioneSocieta PASSED");
	}

	public void testInserimentoDipendente() {
		System.out.println("testInserimentoDipendente INIZIO");
		Long nowInMillisecondi = new Date().getTime();
		Societa nuovaSocieta1 = new Societa("Societa " + nowInMillisecondi, "Via " + nowInMillisecondi, LocalDate.now());
		societaService.inserisciNuovo(nuovaSocieta1);
		if (nuovaSocieta1.getId() == null || nuovaSocieta1.getId() < 1) {
			throw new RuntimeException("testInserimentoDipendente FALLITO: inserimento fallito");
		}
		IntStream.range(1, 5).forEach(i -> {
			Dipendente dipendente = new Dipendente("Mario" +i, "Rossi"+i,LocalDate.now(), 30000+(i*2), nuovaSocieta1);
			dipendenteService.inserisciNuovo(dipendente);;
		});
		Societa societaCaricata = societaService.caricaSingolaSocietaEager(nuovaSocieta1.getId());
		if (societaCaricata.getDipendenti().size()!= 4) {
			throw new RuntimeException("testInserimentoDipendente FALLITO: i dipendenti inseriti non sono il numero previsto");
		}
		System.out.println("testInserimentoDipendente PASSED");
	}
}
