package it.prova.gestionesocieta.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import it.prova.gestionesocieta.model.Dipendente;

public interface DipendenteRepository extends CrudRepository<Dipendente, Long>, QueryByExampleExecutor<Dipendente>{

	@EntityGraph(attributePaths = { "societa"})
	public Dipendente findFirst1BySocieta_DataFondazioneBeforeOrderByDataAssunzioneAsc(LocalDate data);
}
