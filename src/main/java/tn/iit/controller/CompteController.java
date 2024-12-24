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
import tn.iit.entity.Compte;
import tn.iit.service.CompteService;

@AllArgsConstructor
@Controller
@RequestMapping("/comptes")
public class CompteController {

	private final CompteService compteService;

	@GetMapping({ "/all", "/", "/index" })
	public String findAll(Model model) {
		model.addAttribute("comptes", compteService.findAll());
		return "comptes";
	}

	@ResponseBody // json
	@GetMapping("/all-json")
	public List<Compte> findAllJson() {
		return compteService.findAll();
	}

	@PostMapping("/save")
	public String save(@RequestParam String nomClient, @RequestParam float solde) {
		//FIXME
		Compte compte = null;//new Compte(nomClient, solde);
		compteService.saveOrUpdate(compte);
		return "redirect:/comptes/all";
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

}
