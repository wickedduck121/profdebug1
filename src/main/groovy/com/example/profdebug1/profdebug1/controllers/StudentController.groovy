package com.example.profdebug1.profdebug1.controllers

import com.example.profdebug1.profdebug1.entities.Student
import com.example.profdebug1.profdebug1.entities.StudentFromTable
import com.example.profdebug1.profdebug1.repositories.StudentRepository
import com.example.profdebug1.profdebug1.services.WordService
import com.fasterxml.jackson.databind.util.BeanUtil
import groovy.util.logging.Slf4j
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import java.text.SimpleDateFormat

@Slf4j
@RestController
@RequestMapping("/api/students")
class StudentController {

    private final StudentRepository repo

    @Autowired
    StudentController(StudentRepository repoLocal){
        repo=repoLocal
    }

    @PostMapping
    Student saveOne(@RequestBody Object studentLoc){
        log.info("HELLO!")
        log.info(studentLoc.date)
        studentLoc.date= studentLoc.date.replaceAll("T"," ")
        studentLoc.date = studentLoc.date.replaceAll("Z","")
        def pattern = "yyyy-MM-dd hh:mm:ss"
        Date dt = new SimpleDateFormat(pattern).parse(studentLoc.date)
        Student stud = new Student()
        stud.pib = studentLoc.pib
        stud.groupE = studentLoc.groupE
        stud.address = studentLoc.address
        stud.code  = studentLoc.code
        stud.gender = studentLoc.gender
        stud.idStud = studentLoc.idStud
        stud.date = dt
        stud.prof = studentLoc.prof

        repo.save(stud)
    }

    @GetMapping
    List<Student> getAll(){
        log.info("got get")
        repo.findAll()
    }

    @GetMapping('/byid/{id}')
    Student getOne(@PathVariable('id') long idLoc){
        repo.findById(idLoc) as Student
    }

    @GetMapping('/bypib/{pib}')
    List<Student> getByPib(@PathVariable('pib') String pibLoc){
        repo.findByPib(pibLoc)
    }

    @GetMapping('/bygroup/{group}')
    List<Student> getByGroup(@PathVariable('group') String groupLoc){
        repo.findByGroupE(groupLoc)
    }

    @DeleteMapping('/del/{id}')
    delOne(@PathVariable('id') Long idLoc){
        Student studLoc
        Optional<Student> st = repo.findById(idLoc)
        if (st.isPresent()){
            studLoc = st.get()
            repo.delete(studLoc)
        }
        null
    }

    @PutMapping("/gender/{id}")
    Student updtGender(@PathVariable('id') Long idLoc, @RequestBody Object genderLoc)
    {
        Boolean gender = genderLoc.genderLoc
        Student stud = new Student()
        Optional<Student> st = repo.findById(idLoc)
        if(st.isPresent()){
            stud = st.get()
            stud.gender = gender
        }
        repo.save(stud)
    }

    @PutMapping("/prof/{id}")
    Student updtProf(@PathVariable('id') Long idLoc, @RequestBody Object profLoc)
    {
        Boolean prof = profLoc.profLoc
        Student stud = new Student()
        Optional<Student> st = repo.findById(idLoc)
        if(st.isPresent()){
            stud = st.get()
            stud.prof = prof
        }
        repo.save(stud)
    }

    @PutMapping("/{id}")
    Student updateOne(@PathVariable('id') Long idLoc, @RequestBody Object studentLoc)
    {
        studentLoc.el.date= studentLoc.date.replaceAll("T"," ")
        studentLoc.date = studentLoc.date.replaceAll("Z","")
        def pattern = "yyyy-MM-dd hh:mm:ss"
        Date dt = new SimpleDateFormat(pattern).parse(studentLoc.date)
        Student stud = new Student()
        Optional<Student> st = repo.findById(idLoc)
        if(st.isPresent()){
            stud = st.get()
            stud.pib = studentLoc.pib
            stud.groupE = studentLoc.groupE
            stud.address = studentLoc.address
            stud.code  = studentLoc.code
            stud.gender = studentLoc.gender
            stud.date = dt
            stud.prof = studentLoc.prof
        }
        repo.save(stud)
    }

    /*@GetMapping('/doc')
    ArrayList<StudentFromTable> getDoc(){
        WordService wrd = new WordService()
        wrd.wrd()
    }*/


}
