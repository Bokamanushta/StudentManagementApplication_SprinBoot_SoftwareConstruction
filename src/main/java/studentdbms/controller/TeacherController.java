package studentdbms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import studentdbms.entity.Course;
import studentdbms.entity.Teacher;
import studentdbms.service.CourseService;
import studentdbms.service.TeacherService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/teachers")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CourseService courseService;

    @GetMapping("")
    public String findAll(Model model)
    {
        List<Teacher> teachers = teacherService.findAll();
        model.addAttribute("teachers", teachers);
        return "teacher-list";
    }

    @GetMapping("/add")
    public String add(Model model)
    {
        Teacher teacher = new Teacher();
        model.addAttribute("theTeacher", teacher);
        return "teacher-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("theTeacher") Teacher theTeacher)
    {
        teacherService.save(theTeacher);
        return "redirect:/teachers";
    }

    @GetMapping("/{id}/courses")
    public String viewCourses(@PathVariable("id") int id, Model model)
    {
        Teacher teacher = teacherService.findById(id);
        List<Course> courses = teacher.getCourses();
        if(courses.isEmpty()) {
            return "redirect:/teachers/" + id + "/addCourses";
        }
        model.addAttribute("remove_id", id);
        model.addAttribute("courses", courses);
        return "course-list-teacher";
    }

    @GetMapping("/{id}/addCourses")
    public String addCourses(@PathVariable("id") int id, Model model)
    {
        List<Course> teacherCourses = teacherService.findById(id).getCourses();
        List<Course> courses = courseService.findAll();
        List<Course> remainingCourses = new ArrayList<Course>();
        for(Course c: courses)
        {
            if(!teacherCourses.contains(c)) {
                remainingCourses.add(c);
            }
        }
        model.addAttribute("courses", remainingCourses);
        model.addAttribute("add_id", id);
        return "course-list-teacher";
    }

    @GetMapping("/{sid}/addCourse")
    public String addCourse(@PathVariable("sid") int sid, @RequestParam("cid") int cid)
    {
        Teacher teacher = teacherService.findById(sid);
        Course course = courseService.findById(cid);
        teacher.addCourse(course);
        teacherService.save(teacher);
        course.addTeacher(teacher);
        courseService.save(course);
        return "redirect:/teachers/"+sid+"/courses";
    }

    @GetMapping("/{sid}/removeCourse")
    public String removeCourse(@PathVariable("sid") int sid, @RequestParam("cid") int cid)
    {
        Teacher teacher = teacherService.findById(sid);
        Course course = courseService.findById(cid);
        teacher.removeCourse(course);
        teacherService.save(teacher);
        course.removeTeacher(teacher);
        courseService.save(course);
        return "redirect:/teachers/"+sid+"/courses";
    }
}
