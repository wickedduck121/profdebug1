package com.example.profdebug1.profdebug1.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "students")
class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long idStud

    Boolean prof

    String pib

    String groupE

    Boolean gender

    String code

    Date date

    String address

    String trainingForm

    @ManyToOne
    Faculty faculty

}
