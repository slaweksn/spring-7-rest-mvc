package guru.springframework.spring7restmvc.controllers;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import guru.springframework.spring7restmvc.model.Customer;
import guru.springframework.spring7restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/customer")
@AllArgsConstructor
public class CustomerController {

	private final CustomerService customerService;
	
	/**
	 * Update Customer By Id
	 * @param customer
	 * @return
	 */
	@PutMapping("/{customerId}")
	public ResponseEntity<?> updateCustomerById(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer) {
		log.info("Update Customer By Id - in controller " + customer);
		
		Customer updatedCustomer = customerService.updateCustomerById(customerId, customer);
	
		return ResponseEntity.noContent().build();
	}
	
	
	@PostMapping("")
	public ResponseEntity<Customer> createNewCustomer(@RequestBody Customer customer) {
		log.info("Create New Customer - in controller " + customer);
		
		Customer newCustomer = customerService.saveNewCustomer(customer);
		
		return ResponseEntity.created(URI.create("/api/v1/customer/" + newCustomer.getId())).body(newCustomer);
	}
	
	/**
	 * Delete Customer By Id
	 * @return
	 */
	@DeleteMapping("/{customerId}")
	public ResponseEntity<?> deleteCustomerById(@PathVariable("customerId") UUID customerId) {
		log.info("Delete Customer By Id - in controller");
		customerService.deleteCustomerById(customerId);
		
		return ResponseEntity.noContent().build();
	}
	
	
	@GetMapping("")
	public List<Customer> getAllCustomers() {
		log.info("Get all customers - in controller");
		
		return customerService.getAllCustomers();
	}
	
	@GetMapping("/{customerId}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable("customerId") UUID customerId) {
		log.info("Get customer by id - in controller " + customerId);
		
		return customerService.getCustomerById(customerId).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	// --- LOKALNA OBSŁUGA BŁĘDU ---
    // Ta metoda przechwyci błąd konwersji typów TYLKO dla tego kontrolera
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleLocalTypeMismatch(MethodArgumentTypeMismatchException ex) {
        
        Map<String, Object> errorBody = new HashMap<>();
        
        // Sprawdzamy, czy problem dotyczy konkretnie typu UUID
        if (Objects.equals(ex.getRequiredType(), UUID.class)) {
            errorBody.put("error", "Lokalny błąd formatu UUID");
            errorBody.put("message", String.format("Wartość '%s' nie jest poprawnym UUID dla parametru '%s'.", 
                    ex.getValue(), ex.getName()));
        } else {
            errorBody.put("error", "Niepoprawny typ danych");
            errorBody.put("message", ex.getMessage());
        }
        
        errorBody.put("status", HttpStatus.BAD_REQUEST.value());
        
        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }
}
