package guru.springframework.spring7restmvc.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import guru.springframework.spring7restmvc.model.Customer;
import guru.springframework.spring7restmvc.services.CustomerService;
import guru.springframework.spring7restmvc.services.CustomerServiceImpl;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@MockitoBean
	CustomerService customerService;
	
	CustomerServiceImpl customerServiceImpl = new CustomerServiceImpl();
	
	/**
	 * Delete Mapping Test
	 */
	@Test
	void testDeleteCustomerById() throws JacksonException, Exception {
		
		Customer customer = customerServiceImpl.getAllCustomers().get(0);
		//when
		mockMvc.perform(delete("/api/v1/customer/" + customer.getId())
				//.accept(MediaType.APPLICATION_JSON)
				//.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isNoContent());
		
		ArgumentCaptor<UUID> argumentCaptor = ArgumentCaptor.forClass(UUID.class);
		verify(customerService).deleteCustomerById(argumentCaptor.capture());
		
		assertThat(argumentCaptor.getValue()).isEqualTo(customer.getId());
//		verify(customerService).deleteCustomerById(customer.getId());
	}
	
	/**
	 * Put Mapping Test
	 */
	@Test
	void testUpdateCustomerById() throws JacksonException, Exception {
		
		Customer customer = customerServiceImpl.getAllCustomers().get(0);
		//given
		//given(customerService.updateCustomerById(customer.getId(), customer)).willReturn(customer);
		//when + then
		mockMvc.perform(put("/api/v1/customer/" + customer.getId())
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(customer)))
			.andExpect(status().isNoContent());
		
		verify(customerService).updateCustomerById(customer.getId(), customer);
	}
	
	/**
	 * Post Mapping Test
	 * @throws JacksonException 
	 * @throws Exception
	 */
	@Test
	void testCreateNewCustomer() throws JacksonException, Exception {
		//given
		Customer customer = customerServiceImpl.getAllCustomers().get(0);
		given(customerService.saveNewCustomer(customer)).willReturn(customer);
		
		mockMvc.perform(post("/api/v1/customer")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(customer)))
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(header().string("Location", "/api/v1/customer/" + customer.getId()))
			//.andExpect(jsonPath("$.id").value(customer.getId().toString()))
			.andExpect(jsonPath("$.customerName").value(customer.getCustomerName()));
		
	}
	
	@Test
	void testGetAllCustomers() throws Exception {
		//given
		given(customerService.getAllCustomers()).willReturn(customerServiceImpl.getAllCustomers());
		
		//when + then
		mockMvc.perform(get("/api/v1/customer")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.length()").value(3));
	}

	@Test
	void testGetCustomerById() throws Exception {
		Customer customer = customerServiceImpl.getAllCustomers().get(0);
		
		given(customerService.getCustomerById(customer.getId())).willReturn(java.util.Optional.of(customer));
		
		//when + then
		mockMvc.perform(get("/api/v1/customer/" + customer.getId())
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").value(customer.getId().toString()))
			.andExpect(jsonPath("$.customerName").value(customer.getCustomerName()));
	}

}
