package com.codeoftheweb.salvo.repositories;


import com.codeoftheweb.salvo.models.Salvo; //importa el paquete(carpeta) que contiene la clase SalvoRepository
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource; // libraries to create Rest Repositories

@RepositoryRestResource // turns SalvoRepository into a Rest Repository.

public interface SalvoRepository extends JpaRepository<Salvo, Long> {
    //add all the methods you want

}