package com.beer.service.web.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.beer.service.mappers.BeerMapper;
import com.beer.service.repositories.BeerRepository;
import com.beer.service.web.dto.BeerDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/beer")
@RequiredArgsConstructor
public class BeerController {
	
	private final BeerMapper beerMapper;
	private final BeerRepository beerRepository;
	
	@GetMapping("/{id}")
	public ResponseEntity<BeerDto> getBeerById(@PathVariable UUID id) {
		return new ResponseEntity<>(beerMapper.beerToBeerDto(beerRepository.findById(id).get()), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<? extends BeerDto> createBeer(@RequestBody @Validated BeerDto beerDto) {
		
		beerRepository.save(beerMapper.beerDtoToBeer(beerDto));
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateBeerById(@PathVariable UUID id, @RequestBody @Validated BeerDto beerDto) {
		
		beerRepository.findById(id).ifPresent(beer -> {
			beer.setBeerName(beerDto.getBeerName());
			beer.setBeerStyle(beerDto.getBeerStyle().name());
			beer.setPrice(beerDto.getPrice());
			beer.setUpc(beerDto.getUpc());
			
			beerRepository.save(beer);
		});	
	}

}
