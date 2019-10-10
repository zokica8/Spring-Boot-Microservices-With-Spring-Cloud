package com.beer.service.bootstrap;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.beer.service.model.Beer;
import com.beer.service.repositories.BeerRepository;

@Component
public class BeerLoader implements CommandLineRunner {
	
	private final BeerRepository beerRepository;
	
	public BeerLoader(BeerRepository beerRepository) {
		this.beerRepository = beerRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		if(beerRepository.count() == 0) {
			beerRepository.save(Beer.builder()
					.beerName("Zajecarsko")
					.beerStyle("Crno")
					.minOnHand(25)
					.quantityToBrew(900)
					.upc(3700119933L)
					.price(new BigDecimal(99.90))
					.build());
			beerRepository.save(Beer.builder()
					.beerName("Jelen")
					.beerStyle("Svetlo")
					.minOnHand(60)
					.quantityToBrew(1550)
					.upc(3700119938L)
					.price(new BigDecimal(89.90))
					.build());
		}		
	}
}