package com.beer.service.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.beer.service.model.Beer;

@Repository
public interface BeerRepository extends JpaRepository<Beer, UUID> {

}
