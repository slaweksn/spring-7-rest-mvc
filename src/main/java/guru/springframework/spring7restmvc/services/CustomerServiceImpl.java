package guru.springframework.spring7restmvc.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import guru.springframework.spring7restmvc.model.Customer;

@Service
public class CustomerServiceImpl implements CustomerService {

	
	private final Map<UUID, Customer> CUSTOMERS;
	
	public CustomerServiceImpl() {
		super();
		CUSTOMERS = new HashMap<>();
		Customer c = null;
		c = Customer.builder().id(UUID.randomUUID()).customerName("Customer 1").version(1).createdDate(LocalDateTime.now()).build();
		CUSTOMERS.put(c.getId(), c);
		
		c = Customer.builder().id(UUID.randomUUID()).customerName("Customer 2").version(1).createdDate(LocalDateTime.now()).build();
		CUSTOMERS.put(c.getId(), c);
		
		c = Customer.builder().id(UUID.randomUUID()).customerName("Customer 3").version(1).createdDate(LocalDateTime.now()).build();
		CUSTOMERS.put(c.getId(), c);
	}

	@Override
	public List<Customer> getAllCustomers() {
		// TODO Auto-generated method stub
		return CUSTOMERS.values().stream().toList();
	}

	@Override
	public Optional<Customer> getCustomerById(UUID id) {
		// TODO Auto-generated method stub
		return Optional.ofNullable(CUSTOMERS.get(id));
	}

	@Override
	public Customer saveNewCustomer(Customer customer) {
		
		Customer newCustomer = Customer.builder()
				.id(UUID.randomUUID())
				.customerName(customer.getCustomerName())
				.version(1)
				.createdDate(LocalDateTime.now())
				.build();
		
		CUSTOMERS.put(newCustomer.getId(), newCustomer);
		
		return newCustomer;
	}

	@Override
	public Customer updateCustomerById(UUID customerId, Customer customer) {
		// TODO Auto-generated method stub
		 if(customerId == null) {
			 throw new RuntimeException("Customer Id cannot be null. Cannot update.");
		 }
		 if(customer == null) {
			 throw new RuntimeException("Customer cannot be null. Cannot update.");
		 }
		 if(!customerId.equals(customer.getId())) {
			 throw new RuntimeException("Customer Id does not match with the id in the path. Cannot update.");
		 }
		 
		 if(CUSTOMERS.containsKey(customerId)) {
			 Customer existingCustomer = CUSTOMERS.get(customerId);
			 
			 existingCustomer.setCustomerName(customer.getCustomerName());
			 existingCustomer.setVersion(existingCustomer.getVersion() + 1);
			 existingCustomer.setUpdateDate(LocalDateTime.now());
			 
			 return existingCustomer;
		 } else {
			 throw new RuntimeException("Customer with id " + customerId + " not found. Cannot update.");
		 }
	}

	@Override
	public void deleteCustomerById(UUID customerId) {
		// TODO Auto-generated method stub
		if(customerId == null) {
			throw new RuntimeException("Customer Id cannot be null. Cannot delete.");
		}
		if(CUSTOMERS.containsKey(customerId)) {
			CUSTOMERS.remove(customerId);
		} else {
			//throw new RuntimeException("Customer with id " + customerId + " not found. Cannot delete.");
		}
	}

	
}
