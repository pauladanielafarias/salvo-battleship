package com.codeoftheweb.salvo.repositories;

import com.codeoftheweb.salvo.models.Ship; //importa el paquete(carpeta) que contiene la clase ShipRepository
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource; // libraries to create Rest Repositories

@RepositoryRestResource // turns ShipRepository into a Rest Repository.

public interface ShipRepository extends JpaRepository<Ship, Long> {
    //add all the methods you want

}

