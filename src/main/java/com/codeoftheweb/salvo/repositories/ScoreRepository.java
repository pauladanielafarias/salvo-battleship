package com.codeoftheweb.salvo.repositories;
import com.codeoftheweb.salvo.models.Score; //importa el paquete(carpeta) que contiene la clase Score

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource; // libraries to create Rest Repositories

@RepositoryRestResource // turns ScoreRepository into a Rest Repository.

public interface ScoreRepository extends JpaRepository<Score, Long> {
    //add all the methods you want

}