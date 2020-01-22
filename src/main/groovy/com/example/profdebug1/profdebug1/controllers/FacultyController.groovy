package com.example.profdebug1.profdebug1.controllers

import com.example.profdebug1.profdebug1.entities.Faculty
import com.example.profdebug1.profdebug1.entities.Student
import com.example.profdebug1.profdebug1.repositories.FacultyRepository
import com.example.profdebug1.profdebug1.repositories.StudentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/faculty")
class FacultyController {

    private final FacultyRepository repo
    private final StudentRepository studRepo

    @Autowired
    FacultyController(FacultyRepository repoLoc, StudentRepository studRepoLoc){
        repo = repoLoc
        studRepo = studRepoLoc
    }

    @PostMapping
    Faculty addOne(@RequestBody Faculty facLoc){
        repo.save(facLoc)
    }

    @GetMapping('/{id}')
    Faculty getOne(@PathVariable('id') Long facId){
        Optional<Faculty> facTemp = repo.findById(facId)
        if(facTemp.isPresent()){
            facTemp.get()
        }
        null
    }

    @GetMapping
    List<Faculty> getAll(){
        repo.findAll()
    }

    @DeleteMapping('/{id}')
    Faculty delOne(@PathVariable('id') Long facId){

        Optional<Faculty> facTemp = repo.findById(facId)
        Faculty fac = new Faculty()
        if (facTemp.isPresent()){
            fac = facTemp.get()
            List<Student> studs = studRepo.findByFaculty(fac)
            for (int i=0;i<studs.size();i++){
                studs[i].faculty=null
                studRepo.save(studs[i])
            }
            repo.delete(fac)
        }
        fac
    }

    @PutMapping
    Faculty updateOne(@RequestBody Faculty facLoc){
        Optional<Faculty> facTemp = repo.findById(facLoc.facId)
        Faculty fac = new Faculty()
        if (facTemp.isPresent()){
            fac = facTemp.get()
            fac.facName = facLoc.facName
            fac.abbreviation = facLoc.abbreviation
            repo.save(fac)
        }
        fac
    }

}
