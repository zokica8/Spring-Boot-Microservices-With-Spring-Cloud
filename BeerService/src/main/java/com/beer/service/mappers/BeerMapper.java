package com.beer.service.mappers;

import org.mapstruct.Mapper;

import com.beer.service.model.Beer;
import com.beer.service.web.dto.BeerDto;

@Mapper(uses = DateMapper.class)
public interface BeerMapper {
	
	Beer beerDtoToBeer(BeerDto beerDto);
	
	BeerDto beerToBeerDto(Beer beer);

}
