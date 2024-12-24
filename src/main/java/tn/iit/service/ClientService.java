package tn.iit.service;

import java.util.List;



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



}