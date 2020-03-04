package com.example.profdebug1.profdebug1.entities



import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = 'roles')
class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long idRole

    String role


}
