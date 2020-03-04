package com.example.profdebug1.profdebug1.repositories

import com.example.profdebug1.profdebug1.entities.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String Role)

}