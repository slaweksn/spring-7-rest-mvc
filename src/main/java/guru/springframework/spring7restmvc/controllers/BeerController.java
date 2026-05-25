package guru.springframework.spring7restmvc.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.spring7restmvc.model.Beer;
import guru.springframework.spring7restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/beer")
public class BeerController {

	private final BeerService beerService;
	
	@GetMapping()
	public List<Beer> listBeers() {
		log.debug("List Beers - in controller");
		return beerService.listBeers();
	}
	
	@GetMapping("{beerId}")
	public Beer getBeerById(@PathVariable("beerId") UUID id) {
		log.debug("Get Beer By Id - in controller");
		return beerService.getBeerById(id);
	}
	
}
