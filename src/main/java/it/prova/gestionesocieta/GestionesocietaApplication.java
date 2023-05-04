package it.prova.gestionesocieta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.prova.gestionesocieta.service.BatteriaTestService;

@SpringBootApplication
public class GestionesocietaApplication implements CommandLineRunner{
	
	@Autowired
	private BatteriaTestService batteriaTestService;

	public static void main(String[] args) {
		SpringApplication.run(GestionesocietaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		System.out.println("################ START   #################");
		System.out.println("################ eseguo i test  #################");
		batteriaTestService.testInserimentoSocieta();
		batteriaTestService.testFindByExampleSocieta();
		batteriaTestService.testRimozioneSocieta();
		batteriaTestService.testInserimentoDipendente();
		
		
		
		
		System.out.println("################ FINE   #################");
		
	}

	
	
}
