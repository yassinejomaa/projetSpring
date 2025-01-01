package tn.iit.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.domain.Pageable;


import lombok.AllArgsConstructor;
import tn.iit.entity.Client;
import tn.iit.entity.Compte;
import tn.iit.service.ClientService;

@AllArgsConstructor
@Controller
@RequestMapping("/clients")
public class ClientController {

	private final ClientService clientService;

	@GetMapping({ "/all", "/", "/index" })
	public String findAll(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size,
			@RequestParam(name = "Keyword", defaultValue = "") String Keyword) {

		Page<Client> clients;

		if (Keyword.isEmpty()) {
			clients = clientService.findAllPageable(page, size);
		} else {
			clients = clientService.findByNom(Keyword, page, size);
		}

		model.addAttribute("clients", clients.getContent());
		model.addAttribute("pages", new int[clients.getTotalPages()]);
		model.addAttribute("currentPage", page);
		model.addAttribute("Keyword", Keyword);

		return "client";
	}

	@ResponseBody // json
	@GetMapping("/editModal" )
	public Client editModal(@RequestParam String cin) {
		Client c = clientService.findById(cin);
		System.out.println(cin);
		System.out.println("hello");
		System.out.println(c);
		return c;
	}

	/*@ResponseBody // json
	@GetMapping("/all-json")
	public List<Client> findAllJson() {
		return clientService.findAll();
	}*/
	@ResponseBody
	 @GetMapping("/all-json")
	    public ResponseEntity<Page<Client>> getAllClients(
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "5") int size) {
	        Pageable pageable = PageRequest.of(page, size);
	        Page<Client> clients = clientService.getAllClients(pageable);
	        return ResponseEntity.ok(clients);
	    }
	
	
	
	
	
	
	@ResponseBody // json
	@GetMapping("/all-json-autocomplete")
	public List<Client> findAllJson() {
		return clientService.findAll();
	}
	
	

	@PostMapping("/save")
	public String save(@RequestParam String cin, @RequestParam String nom, @RequestParam String prenom) {
		// FIXME
		Client client = new Client(cin, nom, prenom);
		clientService.saveOrUpdate(client);
		return "redirect:/clients/all";
	}
	@ResponseBody
	@PostMapping("/saveModale")
	public String saveModale(Client c) {
		// FIXME
		System.out.println(c);
		clientService.saveOrUpdate(c);
		return "redirect:/clients/all";
	}

	@ResponseBody
	@PostMapping("/delete-ajax")
	public void deleteAjax(@RequestParam String cin) {
		clientService.deleteById(cin);
	}

	@GetMapping("/delete/{rib}")
	public String delete(@PathVariable String cin) {
		clientService.deleteById(cin);
		return "redirect:/clientes/all";
	}

	@PostMapping("/edit")
	public String edit(@RequestParam String cin, Model model) {
		Client client = clientService.findById(cin);
		
		model.addAttribute("client", client);

		return "client-edit";
	}
	@ResponseBody
	@PutMapping("/update/{cin}")
	public ResponseEntity<String> update(@PathVariable String cin, @RequestBody Client client) {
	    try {
	        Client clientAModifier = clientService.findById(cin);
	        
	        if (client.getNom() != null) {
	            clientAModifier.setNom(client.getNom());
	        }

	        if (client.getPrenom() != null) {
	            clientAModifier.setPrenom(client.getPrenom());
	        }

	        clientService.saveOrUpdate(clientAModifier);
	        return ResponseEntity.ok("Mise à jour réussie");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body("Erreur lors de la mise à jour");
	    }
	}


}
