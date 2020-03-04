package com.example.profdebug1.profdebug1.controllers

import com.example.profdebug1.profdebug1.entities.Role
import com.example.profdebug1.profdebug1.entities.User
import com.example.profdebug1.profdebug1.repositories.RoleRepository
import com.example.profdebug1.profdebug1.repositories.UserRepository
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
@RequestMapping('/api/user')
class UserController {

    private final UserRepository repoUser
    private final RoleRepository repoRole

    @Autowired
    UserController(UserRepository repoLoc, RoleRepository repoRoleLoc){
        repoUser = repoLoc
        repoRole = repoRoleLoc
    }

   /* @GetMapping
    List<User> list(){
        repoUser.findAll()
    }*/

    @GetMapping('/name/{name}')
    User getOneName(@PathVariable('name') String name){
        User user = repoUser.findByUsName(name)
        //log.info('name: '+name)
        user
    }

    @PostMapping('/reg')
    User addUser(@RequestBody Object userLoc){
        log.info('got to reg')
        User user = new User()
        user.usName = userLoc.usName
        user.pass = userLoc.pass
        user.email = userLoc.email
        Role rl = repoRole.findByRole("COMMON_USER")
        if(rl!=null)
            user.roles = [rl]
       // user.roles.add(rl)
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder()
        user.pass=passwordEncoder.encode(user.pass)
        repoUser.save(user)
    }
    @DeleteMapping('{name}')
    delete(@PathVariable('name') User user){
        repoUser.delete(user);
    }


}
