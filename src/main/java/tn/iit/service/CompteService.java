package tn.iit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



import lombok.RequiredArgsConstructor;

import tn.iit.dao.CompteRepository;
import tn.iit.entity.Client;
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
	public Compte save(Compte compte) {
        return compteRepository.save(compte);
    }



	public Compte findById(Integer rib) {

		return compteRepository.findById(rib)

				.orElseThrow(() -> new CompteNotFoundException("Compte with rib= " + rib + " not found!"));

	}
	
	public Page<Compte> findAllPageable(int page,int size) {

		return compteRepository.findAll(PageRequest.of(page,size));
		


	}
	
	public Page<Compte> getAllComptes(Pageable pageable) {
        return compteRepository.findAll(pageable);
    }
	
	public Page<Compte> findByClient(String cin,int page,int size) {

		return compteRepository.findByClientCin(cin,PageRequest.of(page,size));
		


	}

	


}