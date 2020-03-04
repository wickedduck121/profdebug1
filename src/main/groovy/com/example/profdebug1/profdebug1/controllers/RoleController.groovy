package com.example.profdebug1.profdebug1.controllers

import com.example.profdebug1.profdebug1.entities.Faculty
import com.example.profdebug1.profdebug1.entities.Role
import com.example.profdebug1.profdebug1.entities.Student
import com.example.profdebug1.profdebug1.entities.User
import com.example.profdebug1.profdebug1.repositories.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping('/api/role')
class RoleController {

    private final RoleRepository repo

    @Autowired
    RoleController(RoleRepository repoLoc){
        repo = repoLoc
    }

    @GetMapping
    List<Role> list(){
        repo.findAll();
    }

    @GetMapping('/name/{id}')
    Role getOneName(@PathVariable('id') String idLoc){
        Role role = repo.findByRole(idLoc)
        //log.info('name: '+name)
        role
    }
    @PostMapping
    Role addRole(@RequestBody Role roleLoc){
        repo.save(roleLoc)
    }

    @DeleteMapping('/{id}')
    Role delOne(@PathVariable('id') Long roleId){

        Optional<Role> roleTemp = repo.findById(roleId)
        Role role = new Role()
        if (roleTemp.isPresent()){
            role = roleTemp.get()
            repo.delete(role)
        }
        role
    }

}
