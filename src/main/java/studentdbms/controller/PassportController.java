package studentdbms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import studentdbms.entity.Passport;
import studentdbms.entity.Student;
import studentdbms.entity.Teacher;
import studentdbms.service.PassportService;
import studentdbms.service.StudentService;
import studentdbms.service.TeacherService;

@Controller
@RequestMapping("/passports")
public class PassportController 
{
	@Autowired
	private StudentService studentService;

	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private PassportService passportService;
	
	@GetMapping("/add")
	public String add(Model model, @RequestParam("id") int id)
	{
		Passport thePassport = new Passport();
		model.addAttribute("thePassport", thePassport);
		model.addAttribute("id", id);
		return "passport-form";
	}

	@GetMapping("/addForTeacher")
	public String addForTeacher(Model model, @RequestParam("id") int id)
	{
		Passport thePassport = new Passport();
		model.addAttribute("thePassport", thePassport);
		model.addAttribute("id", id);
		return "passport-form-teacher";
	}
	
	@PostMapping("/save")
	public String save(@RequestParam("id") int id, @ModelAttribute("thePassport") Passport thePassport)
	{
		Student student = studentService.findById(id);
		thePassport.setStudent(student);
		passportService.save(thePassport);
		student.setPassport(thePassport);
		studentService.save(student);
		return "redirect:/students";
	}

	@PostMapping("/saveForTeacher")
	public String saveForTeacher(@RequestParam("id") int id, @ModelAttribute("thePassport") Passport thePassport)
	{
		Teacher teacher = teacherService.findById(id);
		thePassport.setTeacher(teacher);
		passportService.save(thePassport);
		teacher.setPassport(thePassport);
		teacherService.save(teacher);
		return "redirect:/teachers";
	}
	
}
