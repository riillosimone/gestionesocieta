package it.prova.gestionesocieta.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "societa")
public class Societa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "ragionesociale")
	private String ragioneSociale;
	@Column(name = "indirizzo")
	private String indirizzo;
	@Column(name = "datafondazione")
	private LocalDate dataFondazione;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "societa")
	private Set<Dipendente> dipendenti = new HashSet<>();

	public Societa() {
		super();
	}

	public Societa(String ragioneSociale) {
		super();
		this.ragioneSociale = ragioneSociale;
	}

	public Societa(String ragioneSociale, String indirizzo, LocalDate dataFondazione) {
		super();
		this.ragioneSociale = ragioneSociale;
		this.indirizzo = indirizzo;
		this.dataFondazione = dataFondazione;
	}

	public Societa(String ragioneSociale, String indirizzo) {
		super();
		this.ragioneSociale = ragioneSociale;
		this.indirizzo = indirizzo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRagioneSociale() {
		return ragioneSociale;
	}

	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public LocalDate getDataFondazione() {
		return dataFondazione;
	}

	public void setDataFondazione(LocalDate dataFondazione) {
		this.dataFondazione = dataFondazione;
	}

	public Set<Dipendente> getDipendenti() {
		return dipendenti;
	}

	public void setDipendenti(Set<Dipendente> dipendenti) {
		this.dipendenti = dipendenti;
	}

	@Override
	public String toString() {
		return "Societa [id=" + id + ", ragioneSociale=" + ragioneSociale + ", indirizzo=" + indirizzo
				+ ", dataFondazione=" + dataFondazione + "]";
	}

}
