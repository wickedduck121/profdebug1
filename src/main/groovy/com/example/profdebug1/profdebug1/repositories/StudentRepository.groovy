package com.example.profdebug1.profdebug1.repositories

import com.example.profdebug1.profdebug1.entities.Student
import org.springframework.data.jpa.repository.JpaRepository

interface StudentRepository extends JpaRepository<Student, Long> {
    //Student findById(long id)
    List<Student> findByGroupE(String group)
    List<Student> findByPib(String pib)
}