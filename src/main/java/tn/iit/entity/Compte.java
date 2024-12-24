package tn.iit.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString

@Entity
@Table(name = "t_compte")
public class Compte implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Include
	private Integer rib;
	private float solde;

	@ManyToOne
	@JoinColumn(name="id_client")
	private Client client;
	//fetch: ma manière de chargement des attributs de relation
	//EAGER: au chargement d'un compte, son attribut client est chargé avec
	//LAZY:au chargement d'un compte, son attribut client n'est pas chargé
	//LAZY suite: le client sera chargé lors de l'appel de compte.getClient()
//default fetch selon JPA
	// 1 --> EAGER
	// * --> LAZY
	public Compte(float solde, Client client) {
		super();
		this.solde = solde;
		this.client = client;
	}
	
	
}
