package it.prova.gestionesocieta.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
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
			Dipendente dipendente = new Dipendente("Mario" +i, "Rossi"+i,LocalDate.now(), 35+(i*2), nuovaSocieta);
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
			Dipendente dipendente = new Dipendente("Mario" +i, "Rossi"+i,LocalDate.now(), 35+(i*2), nuovaSocieta1);
			dipendenteService.inserisciNuovo(dipendente);;
		});
		Societa societaCaricata = societaService.caricaSingolaSocietaEager(nuovaSocieta1.getId());
		if (societaCaricata.getDipendenti().size()!= 4) {
			throw new RuntimeException("testInserimentoDipendente FALLITO: i dipendenti inseriti non sono il numero previsto");
		}
		System.out.println("testInserimentoDipendente PASSED");
	}
	
	public void testModificaDipendente() {
		System.out.println("testModificaDipendente INIZIO");
		Long nowInMillisecondi = new Date().getTime();
		Societa nuovaSocieta1 = new Societa("Societa " + nowInMillisecondi, "Via " + nowInMillisecondi, LocalDate.now());
		societaService.inserisciNuovo(nuovaSocieta1);
		if (nuovaSocieta1.getId() == null || nuovaSocieta1.getId() < 1) {
			throw new RuntimeException("testModificaDipendente FALLITO: inserimento fallito");
		}
		Dipendente dipendenteDaModificare = new Dipendente("fabrizio", "De nicola", LocalDate.of(1990,12,1), 20000, nuovaSocieta1);
		dipendenteService.inserisciNuovo(dipendenteDaModificare);
		if (dipendenteDaModificare.getId() == null || dipendenteDaModificare.getId() < 1) {
			throw new RuntimeException("testModificaDipendente FALLITO: inserimento fallito");
		}
		String nuovoNome ="Nicola";
		LocalDate nuovaData = LocalDate.now();
		dipendenteDaModificare.setNome(nuovoNome);
		dipendenteDaModificare.setDataAssunzione(nuovaData);
		dipendenteService.aggiorna(dipendenteDaModificare);
		if (dipendenteDaModificare.getNome() != nuovoNome || dipendenteDaModificare.getDataAssunzione() != nuovaData) {
			throw new RuntimeException("testModificaDipendente FALLITO: La modifica non corrisponde");
		}
		System.out.println("testInserimentoDipendente PASSED");
	}
	
	public void testAllByDipendenteRedditoMinimo () {
		System.out.println("testAllByDipendenteRedditoMinimo INIZIO");
		int redditoToCheck = 30000;
		IntStream.range(1, 5).forEach(i-> {
			Societa nuovaSocieta = new Societa("Societa " + i, "Via " + i, LocalDate.now());
			societaService.inserisciNuovo(nuovaSocieta);
			Dipendente dipendente1 = new Dipendente("fabrizio" +i, "De nicola"+i, LocalDate.of(1990,12,1), 20000, nuovaSocieta);
			dipendenteService.inserisciNuovo(dipendente1);
			Dipendente dipendente2 = new Dipendente("mario", "lippi", LocalDate.of(1990,12,1), 35000, nuovaSocieta);
			dipendenteService.inserisciNuovo(dipendente2);
			
		});
		List<Societa> risultati = societaService.listaSocietaDistinteConDipendenteRedditoMaggiore(redditoToCheck);
		if (risultati.size()!=4) {
			throw new RuntimeException("testAllByDipendenteRedditoMinimo FALLITO: in numero di società non corrisponde");
		}
		risultati.stream().forEach(r->System.out.println(r));
		System.out.println("testAllByDipendenteRedditoMinimo PASSED");
	}
	
	public void testTrovaIlPiuAnziano() {
		System.out.println("testTrovaIlPiuAnziano INIZIO");
		LocalDate dataCheck = LocalDate.ofYearDay(1990, 1);
		Societa nuovaSocieta = new Societa("Societa 1 ", "Via A " , LocalDate.of(1990, 3, 1));
		societaService.inserisciNuovo(nuovaSocieta);
		Societa nuovaSocieta2 = new Societa("Societa 1 ", "Via A " , LocalDate.of(1982, 1, 1));
		societaService.inserisciNuovo(nuovaSocieta2);
		Dipendente nuovoDipendente = new Dipendente("Mario", "Rizzo", LocalDate.of(2000, 2, 14), 2000, nuovaSocieta2);
		dipendenteService.inserisciNuovo(nuovoDipendente);
		Dipendente nuovoDipendente2 = new Dipendente("Giuseppe", "Pardo", LocalDate.of(2003, 2, 14), 1000, nuovaSocieta2);
		dipendenteService.inserisciNuovo(nuovoDipendente2);
		Dipendente nuovoDipendente3 = new Dipendente("Francesco", "Amato", LocalDate.of(1999, 2, 14), 400, nuovaSocieta);
		dipendenteService.inserisciNuovo(nuovoDipendente3);
		
		Dipendente piuAnziano = dipendenteService.trovaIlPiuAnziano(dataCheck);
		if (piuAnziano.getId() != nuovoDipendente.getId()) {
			throw new RuntimeException("testTrovaIlPiuAnziano FALLITO: il dipendente non corrisponde");
		}
		System.out.println(piuAnziano);
		System.out.println("testTrovaIlPiuAnziano PASSED");
		
	}
	
	
}
