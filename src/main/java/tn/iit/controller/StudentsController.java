package tn.iit.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.AllArgsConstructor;
import tn.iit.dto.StudentDto;
import tn.iit.exception.StudentNotFoundException;
import tn.iit.service.StudentService;

@AllArgsConstructor
@Controller
@RequestMapping("/students")
public class StudentsController {
	private final StudentService studentService;

	@GetMapping({ "/all", "/", "/index" })
	public String findAll(Model model) {
		model.addAttribute("students", studentService.findAll());
		return "students";// va a la page html
	}

	@PostMapping("/save")
	public String save(@RequestParam("id") int id, @RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName) {
		StudentDto studentDto = new StudentDto(id, firstName, lastName);
		studentService.save(studentDto);
		return "redirect:/students/all";
	}

	@GetMapping("delete/{id}")
	public String delete(@PathVariable int id) {
		studentService.deleteById(id);
		return "redirect:/students/all";
	}
	
	@PostMapping("/edit")
	public String edit(@RequestParam("id") int id,Model model) {
		Optional<StudentDto> studentDtoOpt=studentService.findById(id);
		if(studentDtoOpt.isPresent()) {
		model.addAttribute("student",studentDtoOpt.get());
		}else {
			throw new StudentNotFoundException("Student with id" +id + "notFound");
		}
		return "student-edit";
	}
	
	@PostMapping("/update")
	public String update(@ModelAttribute StudentDto studentDto) {
		studentService.update(studentDto);
		return "redirect:/students/all";
	}
}
