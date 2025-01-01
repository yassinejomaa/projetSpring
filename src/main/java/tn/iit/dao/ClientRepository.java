package tn.iit.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import tn.iit.entity.Client;

public interface ClientRepository extends JpaRepository<Client, String> {

	Page<Client> findByNom(String nom,Pageable Pageable);
}
