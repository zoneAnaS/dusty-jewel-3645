package com.sweetopia.service.implementation;

import java.util.List;
import java.util.Optional;

import com.sweetopia.entity.Address;
import com.sweetopia.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;

import com.sweetopia.entity.User;

import com.sweetopia.exception.CustomerNotFoundException;
import com.sweetopia.exception.InvalidCustomerException;
import com.sweetopia.repository.CustomerRepository;
import com.sweetopia.service.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private AddressRepository addressRepository;

	@Override
	public User addCustomer(User user)throws InvalidCustomerException {
		Optional<User> customer2 =customerRepository.findByEmail(user.getEmail());
		if(customer2.isPresent())throw new InvalidCustomerException(user.getEmail()+" email already registered!");
		if (user.getId() != null) {
			if(customerRepository.findById(user.getId()).isPresent())throw new InvalidCustomerException("User with id:"+ user.getId()+" already present");
		}
		return customerRepository.save(user);

	}

	@Override
	public User updateCustomer(User user) throws InvalidCustomerException{

		if (user.getId() != null) {
			if(customerRepository.findById(user.getId()).isEmpty())throw new InvalidCustomerException("User with id:"+ user.getId()+" not found");
		}else{
			throw new InvalidCustomerException("No customer id mentioned");
		}


		return customerRepository.save(user);
	}

	@Override
	public User cancelCustomer(Long CustomerId)throws CustomerNotFoundException {

		User existingUser = customerRepository.findById(CustomerId).orElseThrow(()-> new CustomerNotFoundException("Customer not fouund with this id :"+CustomerId));
		customerRepository.deleteById(CustomerId);
		return existingUser;
	}

	@Override
	public List<User> showAllCustomers() throws InvalidCustomerException{
		List<User> list= customerRepository.findAll();
		if(list.isEmpty())throw new InvalidCustomerException("No customer in database");
		return list;
	}

	@Override
	public User getCustomerById(Long CustomerId)throws CustomerNotFoundException {
		Optional<User> customerOption=customerRepository.findById(CustomerId);
		if(customerOption.isEmpty())throw new CustomerNotFoundException("No customer found with id : "+CustomerId);


		return customerOption.get();


	}

	@Override
	public Address addAddressToCustomer(Long CustomerId, Address address) throws CustomerNotFoundException {
		User user =getCustomerById(CustomerId);
		address.setUser(user);
		return addressRepository.save(address);
	}

	@Override
	public Address updateAddressOfCustomer(Long CustomerId, Long addressId, Address address) throws CustomerNotFoundException {
		User user =getCustomerById(CustomerId);
		boolean flag=false;
		for(Address address1: user.getAddresses()){
			if(addressId==address1.getAddId()){
				flag=true;
				address.setAddId(addressId);
				user.getAddresses().remove(address1);
				user.getAddresses().add(address);
				break;
			}
		}
		if(!flag)throw new CustomerNotFoundException("No address found for customer");

		updateCustomer(user);
		return address;
	}

	@Override
	public Address deleteAddressOfCustomer(Long CustomerId, Long addressId) throws CustomerNotFoundException {
		User user =getCustomerById(CustomerId);
		boolean flag=false;
		Address address=null;
		for(Address address1: user.getAddresses()){
			if(addressId==address1.getAddId()){
				flag=true;
				address=address1;
				user.getAddresses().remove(address1);
				break;
			}
		}
		if(!flag)throw new CustomerNotFoundException("No address found for customer");
		address.setUser(null);
		addressRepository.delete(address);
		return address;
	}

	@Override
	public List<Address> getAllAddressByCustomerId(Long CustomerId) throws CustomerNotFoundException {
		User user =getCustomerById(CustomerId);
		List<Address> list= user.getAddresses();
		if(list.isEmpty())throw new CustomerNotFoundException("No address present for the given customer");

		return list;
	}

	@Override
	public User customerLogin(String email, String password) throws CustomerNotFoundException {
		Optional<User> customer=customerRepository.findByEmailAndUserPassword(email,password);
		if(customer.isEmpty())throw new CustomerNotFoundException("Invalid credentials");
		return customer.get();
	}

}