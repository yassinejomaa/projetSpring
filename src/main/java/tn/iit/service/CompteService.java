package tn.iit.service;

import java.util.List;



import org.springframework.stereotype.Service;



import lombok.RequiredArgsConstructor;

import tn.iit.dao.CompteRepository;

import tn.iit.entity.Compte;

import tn.iit.exception.CompteNotFoundException;



@RequiredArgsConstructor

@Service

public class CompteService {

	private final CompteRepository compteRepository;



	public void saveOrUpdate(Compte compte) {

		compteRepository.save(compte);

	}



	public List<Compte> findAll() {

		return compteRepository.findAll();

	}



	public void deleteById(Integer rib) {

		compteRepository.deleteById(rib);



	}



	public Compte findById(Integer rib) {

		return compteRepository.findById(rib)

				.orElseThrow(() -> new CompteNotFoundException("Compte with rib= " + rib + " not found!"));

	}

	


}