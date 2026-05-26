package guru.springframework.spring7restmvc.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import guru.springframework.spring7restmvc.model.Beer;

public interface BeerService {
	
	Optional<Beer> getBeerById(UUID id);

	List<Beer> listBeers();
	
	Beer saveNewBeer(Beer beer);

	Beer updateBeerById(UUID id, Beer beer);

	void deleteBeerById(UUID id);

	Beer patchBeerById(UUID id, Beer beer);
}
