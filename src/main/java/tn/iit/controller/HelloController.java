package tn.iit.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/hello")
public class HelloController {
	@GetMapping("/home")
	public String goTohello(Model model ) {
		model.addAttribute("date", LocalDate.now());
		return "hello";// va a la page html
	}
	
	@GetMapping("/home2")
	public ModelAndView goTohello2() {
		ModelAndView modelAndView=new ModelAndView();
		//charger une donné et l'envoyer au view
		modelAndView.setViewName("hello");
		modelAndView.addObject("date", LocalDate.now());
		return modelAndView;// 
	}
}
