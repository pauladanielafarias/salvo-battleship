package com.codeoftheweb.salvo.repositories;

import com.codeoftheweb.salvo.models.GamePlayer; //importa el paquete(carpeta) que contiene la clase GamePlayerRepository
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource; // libraries to create Rest Repositories

@RepositoryRestResource // turns GamePlayerRepository into a Rest Repository.

public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {
    //add all the methods you want
    GamePlayer findById(long id);

}
