package guru.springframework.spring7restmvc.controllers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import guru.springframework.spring7restmvc.model.Beer;

@SpringBootTest
class BeerControllerTest {

	@Autowired
	BeerController beerController;

	@Test
	void testGetBeerById() {
		Beer beer = beerController.getBeerById(java.util.UUID.randomUUID());
		assertNotNull(beer);
		
		System.out.println(beer);
	}

}
