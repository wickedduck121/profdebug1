package com.example.profdebug1.profdebug1.entities

import com.sun.istack.NotNull

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.Table
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "users")
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long idUser

    @NotNull
    @NotEmpty
    String usName

    @NotNull
    @NotEmpty
    String pass

    @NotNull
    @NotEmpty
    String email

    @ManyToMany(cascade = CascadeType.ALL)
    Set<Role> roles

}
