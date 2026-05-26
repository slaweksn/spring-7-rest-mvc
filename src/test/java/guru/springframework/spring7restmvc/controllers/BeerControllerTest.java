package guru.springframework.spring7restmvc.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import guru.springframework.spring7restmvc.ex.NotFoundException;
import guru.springframework.spring7restmvc.model.Beer;
import guru.springframework.spring7restmvc.services.BeerService;
import guru.springframework.spring7restmvc.services.BeerServiceImpl;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

	private static final String API_URL = "/api/v1/beer";
	private static final String API_URL_ID = API_URL + "/{id}";

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockitoBean
	private BeerService beerService;
	
	@Captor
	private ArgumentCaptor<UUID> uuidArgumentCaptor;
	
	@Captor
	private ArgumentCaptor<Beer> beerArgumentCaptor;
	
	private BeerServiceImpl beerServiceImpl;
	private Beer testBeer;
	
	@BeforeEach
	void setUp() {
		// Inicjalizacja adnotacji @Captor dla testów WebMvcTest
		MockitoAnnotations.openMocks(this);
		
		beerServiceImpl = new BeerServiceImpl();
		testBeer = beerServiceImpl.listBeers().get(0);
	}
	
	@Test
	void testPatchBeerById() throws Exception {
		Map<String, Object> beerMap = new HashMap<>();
		beerMap.put("beerName", "New Name");
		
		mockMvc.perform(patch(API_URL_ID, testBeer.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(beerMap)))
				.andExpect(status().isNoContent());
		
//		ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
//		ArgumentCaptor<Beer> beerArgumentCaptor = ArgumentCaptor.forClass(Beer.class);
		
		verify(beerService).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());
		
		assertThat(beerArgumentCaptor.getValue().getBeerName()).isEqualTo("New Name");
		assertThat(uuidArgumentCaptor.getValue()).isEqualTo(testBeer.getId());
	}
	
	@Test
	void testDeleteBeerById() throws Exception {
		mockMvc.perform(delete(API_URL_ID, testBeer.getId()))
				.andExpect(status().isNoContent());
		
		verify(beerService).deleteBeerById(any(UUID.class));
	}
	
	@Test
	void testUpdateBeerById() throws Exception {
		mockMvc.perform(put(API_URL_ID, testBeer.getId())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(testBeer)))
				.andExpect(status().isNoContent());
		
		verify(beerService).updateBeerById(any(UUID.class), any(Beer.class));
	}
	
	@Test
	void createNewBeer() throws Exception {
		given(beerService.saveNewBeer(testBeer)).willReturn(testBeer);
		
		ResultActions response = mockMvc.perform(post(API_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(testBeer)))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", API_URL + "/" + testBeer.getId()))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

		assertBeerJsonFields(response, testBeer);
	}
	
	@Test
	void testListBeers() throws Exception {
		given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());
		
		mockMvc.perform(get(API_URL)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()").value(3));
	}
	
	@Test
	void testGetBeerByIdNotFound() throws Exception {
		
		given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());
		
		mockMvc.perform(get(API_URL_ID, UUID.randomUUID())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error").exists())
				
				;
		
	}
	
	@Test
	void testGetBeerById() throws Exception {
		given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));
				
		ResultActions response = mockMvc.perform(get(API_URL_ID, testBeer.getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

		assertBeerJsonFields(response, testBeer);
	}

	private void assertBeerJsonFields(ResultActions response, Beer beer) throws Exception {
		response.andExpect(jsonPath("$.id").value(beer.getId().toString()))
				.andExpect(jsonPath("$.version").value(beer.getVersion()))
				.andExpect(jsonPath("$.beerName").value(beer.getBeerName()))
				.andExpect(jsonPath("$.beerStyle").value(beer.getBeerStyle().toString()))
				.andExpect(jsonPath("$.upc").value(beer.getUpc()))
				.andExpect(jsonPath("$.quantityOnHand").value(beer.getQuantityOnHand()))
				.andExpect(jsonPath("$.price").value(beer.getPrice().doubleValue()));
	}
}
