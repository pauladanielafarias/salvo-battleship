package com.codeoftheweb.salvo.repositories;
import com.codeoftheweb.salvo.models.Game; //importa el paquete(carpeta) que contiene la clase Game

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource; // libraries to create Rest Repositories

@RepositoryRestResource // turns GameRepository into a Rest Repository.

public interface GameRepository extends JpaRepository<Game, Long> {
    //add all the methods you want
    Game findById(long id);

}
