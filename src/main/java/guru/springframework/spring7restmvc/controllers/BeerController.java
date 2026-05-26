package guru.springframework.spring7restmvc.controllers;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import guru.springframework.spring7restmvc.ex.NotFoundException;
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
	
	@PostMapping()
	public ResponseEntity<Beer> createNewBeer(@RequestBody Beer beer) {
		log.info("Create New Beer - in controller " + beer);
		
		Beer saveNewBeer = beerService.saveNewBeer(beer);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", "/api/v1/beer/" + saveNewBeer.getId());
		
		return ResponseEntity.created(URI.create("/api/v1/beer/" + saveNewBeer.getId())).body(saveNewBeer);
	}
	/**
	 * Update Beer By Id
	 * @param id
	 * @return
	 */
	@PutMapping("{beerId}")
	public ResponseEntity<?> updateBeerById(@PathVariable("beerId") UUID id, @RequestBody Beer beer) {
		log.info("Update Beer By Id - in controller " + beer);
		
		Beer updatedBeer = beerService.updateBeerById(id, beer);
	
		return ResponseEntity.noContent().build();
	}
	
	/**
	 * Delete Beer By Id
	 * @return
	 */
	@DeleteMapping("{beerId}")
	public ResponseEntity<?> deleteBeerById(@PathVariable("beerId") UUID id) {
		log.info("Delete Beer By Id - in controller");
		
		beerService.deleteBeerById(id);
		
		return ResponseEntity.noContent().build();
	}
	
	/**
	 * Update Patch Bear by Id
	 * @return
	 */
	@PatchMapping("{beerId}")
	public ResponseEntity<?> patchBeerById(@PathVariable("beerId") UUID id, @RequestBody Beer beer) {
		log.info("Patch Beer By Id - in controller " + beer);
		
		Beer updatedBeer = beerService.patchBeerById(id, beer);
	
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping()
	public List<Beer> listBeers() {
		log.info("List Beers - in controller");
		return beerService.listBeers();
	}
	
	@GetMapping("{beerId}")
	public Beer getBeerById(@PathVariable("beerId") UUID id) {
		log.info("Get Beer By Id - in controller");
		return beerService.getBeerById(id).orElseThrow(()-> new NotFoundException("Beer with id " + id.toString() + " not found. Cannot get."));
	}
	
//	@ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException ex) {
//		
//		log.error("NotFoundException: " + ex.getMessage());
//		
//		return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
//	}
}
