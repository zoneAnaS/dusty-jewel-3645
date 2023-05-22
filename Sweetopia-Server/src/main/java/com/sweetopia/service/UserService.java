package com.sweetopia.service;

import java.util.List;

import com.sweetopia.entity.Address;
import com.sweetopia.entity.User;
import com.sweetopia.exception.CustomerNotFoundException;
import com.sweetopia.exception.InvalidCustomerException;

public interface UserService {

	public User addCustomer(User user) throws InvalidCustomerException ;
	public User updateCustomer(Long id,User user) throws InvalidCustomerException ;
	public User cancelCustomer(Long CustomerId) throws CustomerNotFoundException;
	public List<User> showAllCustomers();
	public User getCustomerById(Long CustomerId) throws CustomerNotFoundException;
	public Address addAddressToCustomer(Long CustomerId,Address address)throws CustomerNotFoundException;
	public Address updateAddressOfCustomer(Long CustomerId,Long addressId,Address address)throws CustomerNotFoundException;
	public Address deleteAddressOfCustomer(Long CustomerId,Long addressId)throws CustomerNotFoundException;
	public List<Address> getAllAddressByCustomerId(Long CustomerId)throws CustomerNotFoundException;

	public User customerLogin(String email, String password)throws CustomerNotFoundException;
}
