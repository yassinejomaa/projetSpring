package tn.iit.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import tn.iit.entity.Compte;
import tn.iit.entity.Client;
import tn.iit.service.CompteService;
import tn.iit.service.ClientService;

@AllArgsConstructor
@Controller
@RequestMapping("/comptes")
public class CompteController {

	private final CompteService compteService;
	private final ClientService clientService;

	@GetMapping({ "/all", "/", "/index" })
	public String findAll(Model model, 
	                      @RequestParam(name = "page", defaultValue = "0") int page,
	                      @RequestParam(name = "size", defaultValue = "5") int size,
	                      @RequestParam(name = "Keyword", defaultValue = "") String Keyword) {
	    
	    // Recherche des clients (pas paginé ici)
	    model.addAttribute("clients", clientService.findAll());

	    // Recherche des comptes paginés
	    Page<Compte> comptes;

	    if (Keyword.isEmpty()) {
	        comptes = compteService.findAllPageable(page, size); // Recherche paginée des comptes sans mot-clé
	    } else {
	    	comptes= compteService.findByClient(Keyword, page, size); 
	    }

	    // Ajout de la liste paginée de comptes au modèle
	    model.addAttribute("comptes", comptes.getContent()); // Utilisez `getContent()` pour obtenir la liste des comptes
	    model.addAttribute("pages", new int[comptes.getTotalPages()]); // Génère la pagination
	    model.addAttribute("currentPage", page); // Numéro de la page actuelle
	    model.addAttribute("Keyword", Keyword); // Le mot-clé de recherche

	    return "comptes";
	}

	
	@ResponseBody // json
	@GetMapping("/all-json-autocomplete")
	public List<Compte> findAllJsonForAutocomplet() {
		return compteService.findAll();
	}
	
	
	
	
	
	
	
	@ResponseBody
	 @GetMapping("/all-json")
	    public ResponseEntity<Page<Compte>> getAllComptes(
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "5") int size) {
	        Pageable pageable = PageRequest.of(page, size);
	        Page<Compte> comptes = compteService.getAllComptes(pageable);
	        return ResponseEntity.ok(comptes);
	    }
	
	
	
	
	
	
	
	
	
	
	
	

	/*@ResponseBody // json
	@GetMapping("/all-json")
	public List<Compte> findAllJson() {
		return compteService.findAll();
	}*/
	@ResponseBody
	@GetMapping("/all-json-client")
	public List<Client> findAllJsonClient() {
		return clientService.findAll();
	}

	@PostMapping("/save")
	public String save(@RequestParam String clientCin, @RequestParam float solde) {

		Client c1 = clientService.findById(clientCin);
		Compte compte = new Compte(solde, c1);
		compteService.saveOrUpdate(compte);
		return "redirect:/comptes/all";
	}

	@PostMapping("/saveModale")
	@ResponseBody
	public String saveModale(@RequestBody Map<String, Object> body) {
		// Récupérer les données envoyées depuis le frontend

		String clientCin = (String) body.get("clientCin");
		float solde = Float.parseFloat(body.get("solde").toString());
		Client client = clientService.findById(clientCin);
		if (client == null) {
			throw new IllegalArgumentException("Client introuvable avec le CIN : " + clientCin);
		}

		// Créer un nouvel objet Compte
		Compte compte = new Compte(solde, client);
		// Définir le solde

		// Sauvegarder le compte en base de données
		compteService.saveOrUpdate(compte);

		return "Compte ajouté avec succès !";
	}

	@ResponseBody
	@PostMapping("/delete-ajax")
	public void deleteAjax(@RequestParam Integer rib) {
		compteService.deleteById(rib);
	}

	@GetMapping("/delete/{rib}")
	public String delete(@PathVariable Integer rib) {
		compteService.deleteById(rib);
		return "redirect:/comptes/all";
	}

	@PostMapping("/edit")
	public String edit(@RequestParam Integer rib, Model model) {
		Compte compte = compteService.findById(rib);
		model.addAttribute("compte", compte);

		return "compte-edit";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute Compte compte) {
		compteService.saveOrUpdate(compte);
		return "redirect:/comptes/all";
	}

	@ResponseBody // json
	@GetMapping({ "/editModal" })
	public Compte editModal(@RequestParam int rib) {
		Compte compte = compteService.findById(rib);
		return compte;
	}
	@ResponseBody
	@PutMapping("/updateModale/{rib}")
	public Compte updateCompte(@PathVariable int rib, @RequestBody Object compte) {
		// Rechercher le compte à mettre à jour par son RIB
		Compte compteAModifier = compteService.findById(rib);
		if (compteAModifier == null) {
			throw new EntityNotFoundException("Compte introuvable avec le RIB : " + rib);
		}

		try {
			// Convertir l'objet générique reçu en une Map
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> compteMap = objectMapper.convertValue(compte, Map.class);

			// Mettre à jour les champs de Compte
			if (compteMap.containsKey("solde")) {
				compteAModifier.setSolde(Float.parseFloat(compteMap.get("solde").toString()));
			}

			if (compteMap.containsKey("clientCin")) {
				compteAModifier.setClient(clientService.findById(compteMap.get("clientCin").toString()));
				
			}
			
			System.out.println(compteMap.get("solde").toString());
			System.out.println(compteMap.get("clientCin").toString());

			// Sauvegarder les modifications
			return compteService.save(compteAModifier);

		} catch (Exception e) {
			throw new RuntimeException("Erreur lors de la mise à jour du compte : " + e.getMessage());
		}
	}
}
