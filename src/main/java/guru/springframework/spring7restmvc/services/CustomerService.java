package guru.springframework.spring7restmvc.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import guru.springframework.spring7restmvc.model.Customer;

public interface CustomerService {

	List<Customer> getAllCustomers();
	
	Optional<Customer> getCustomerById(UUID id);

	Customer saveNewCustomer(Customer customer);

	Customer updateCustomerById(UUID customerId, Customer customer);

	void deleteCustomerById(UUID customerId);
}
