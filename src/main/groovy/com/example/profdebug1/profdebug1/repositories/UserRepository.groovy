package com.example.profdebug1.profdebug1.repositories

import com.example.profdebug1.profdebug1.entities.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository extends JpaRepository<User, Long> {
    User findByUsName(String usName)

    User findByEmail(String email)

}