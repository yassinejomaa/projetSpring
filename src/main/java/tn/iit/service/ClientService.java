package tn.iit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



import lombok.RequiredArgsConstructor;

import tn.iit.dao.ClientRepository;

import tn.iit.entity.Client;

import tn.iit.exception.CompteNotFoundException;



@RequiredArgsConstructor

@Service

public class ClientService {

	private final ClientRepository clientRepository;



	public void saveOrUpdate(Client client) {

		clientRepository.save(client);

	}



	public List<Client> findAll() {

		return clientRepository.findAll();

	}



	public void deleteById(String cin) {

		clientRepository.deleteById(cin);



	}
	
	



	public Client findById(String cin) {

		return clientRepository.findById(cin)

				.orElseThrow(() -> new CompteNotFoundException("Client with cin= " + cin + " not found!"));

	}
	
	public Page<Client> findByNom(String nom,int page,int size) {

		return clientRepository.findByNom(nom,PageRequest.of(page,size));
		


	}
	public Page<Client> findAllPageable(int page,int size) {

		return clientRepository.findAll(PageRequest.of(page,size));
		


	}
	public Page<Client> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }







}