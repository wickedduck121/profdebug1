package com.example.profdebug1.profdebug1.repositories

import com.example.profdebug1.profdebug1.entities.Faculty
import org.springframework.data.jpa.repository.JpaRepository

interface FacultyRepository extends JpaRepository<Faculty, Long> {
    //Faculty findByFacName(String facName)

}