package com.codeoftheweb.salvo.repositories;
import com.codeoftheweb.salvo.models.Player; //importa el paquete(carpeta) que contiene la clase Player

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource; // libraries to create Rest Repositories

@RepositoryRestResource // turns PlayerRepository into a Rest Repository.

public interface PlayerRepository extends JpaRepository<Player, Long> {
    //add all the methods you want - the next one is an example
    Player findByUsername(String username);
}
