package com.example.profdebug1.profdebug1.services

import com.example.profdebug1.profdebug1.entities.User
import com.example.profdebug1.profdebug1.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository repo

    @Override
    UserDetails loadUserByUsername(String name){

        /*repo
                .findByName(name)
                .map { new User(it.name, it.pass, []) }
                .orElseThrow { throw new UsernameNotFoundException() }*/
        def user = repo.findByUsName(name)
        List<String> rls = []
        /*for (int i = 0; i < user.roles.size(); i++) {
            rls.push(user.roles[i].role)
        }*/
       // User user1 = new User(user.usName,user.pass, user.email, [])
        org.springframework.security.core.userdetails.User
                .withUsername(user.usName)
                .password(user.pass)
                .roles("ADMIN", "USER").build()
        //user as UserDetails

    }
}
