package tn.iit.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.AllArgsConstructor;
import tn.iit.entity.Client;
import tn.iit.service.ClientService;

@AllArgsConstructor
@Controller
@RequestMapping("/clients")
public class ClientController {

	private final ClientService clientService;

	@GetMapping({ "/all", "/", "/index" })
	public String findAll(Model model) {
		model.addAttribute("client", clientService.findAll());
		return "client";
	}

	@ResponseBody // json
	@GetMapping("/all-json")
	public List<Client> findAllJson() {
		return clientService.findAll();
	}

	@PostMapping("/save")
	public String save(@RequestParam String cin,@RequestParam String nom,@RequestParam String prenom) {
		//FIXME
		Client client =new Client(cin, nom,prenom);
		clientService.saveOrUpdate(client);
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

	@PostMapping("/update")
	public String update(@ModelAttribute Client client) {
		clientService.saveOrUpdate(client);
		return "redirect:/clients/all";
	}

}
