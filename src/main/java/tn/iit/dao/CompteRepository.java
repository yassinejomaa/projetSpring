package tn.iit.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import tn.iit.entity.Client;
import tn.iit.entity.Compte;

public interface CompteRepository extends JpaRepository<Compte, Integer> {
	Page<Compte> findByClientCin(String cin,Pageable Pageable);
}
