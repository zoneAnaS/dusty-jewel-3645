package com.sweetopia.controller;

import java.util.List;

import com.sweetopia.dto.CustomerDTO;
import com.sweetopia.dto.CustomerLoginDTO;
import com.sweetopia.entity.Address;
import com.sweetopia.entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sweetopia.entity.User;
import com.sweetopia.service.CustomerService;




@RestController
@RequestMapping("/sweetopia/customers")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class CustomerController {

	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping
	public ResponseEntity<User> addCustomer(@RequestBody CustomerDTO customerDTO){
		User user =new User();
		user.setUserName(customerDTO.getUserName());
		user.setUserPassword(customerDTO.getUserPassword());
		user.setEmail(customerDTO.getEmail());
		Cart cart=new Cart();
		user.setCart(cart);

			customerService.addCustomer(user);
			return ResponseEntity.status(HttpStatus.CREATED).body(user);

	}
	
	@PutMapping("/{customerId}")
	public ResponseEntity<User> updateCustomer(@PathVariable("customerId")Long customerId, @RequestBody User user){

			user.setId(customerId);
			customerService.updateCustomer(user);
			return ResponseEntity.ok().build();
	}
	
	@GetMapping
	public ResponseEntity<List<User>> showAllCustomers(){
		
		List<User> users = customerService.showAllCustomers();
		return ResponseEntity.ok(users);
	}
	
	@GetMapping("/{customerId}")
	public ResponseEntity<User> showCustomerById(@PathVariable("customerId")Long customerId) {

		User user = customerService.getCustomerById(customerId);
		return ResponseEntity.ok(user);
	}
	@GetMapping("/{customerId}/addresses")
	public ResponseEntity<List<Address>> getAlladdress(@PathVariable Long customerId){
		List<Address> address1=customerService.getAllAddressByCustomerId(customerId);
		return new ResponseEntity<>(address1,HttpStatus.OK);
	}
	@PostMapping("/{customerId}/addresses")
	public ResponseEntity<Address> addAddressToCustomer(@PathVariable Long customerId, @RequestBody Address address){
		Address address1=customerService.addAddressToCustomer(customerId,address);
		return new ResponseEntity<>(address1,HttpStatus.ACCEPTED);
	}
	@PutMapping("/{customerId}/addresses/{addressId}")
	public ResponseEntity<Address> addAddressToCustomer(@PathVariable Long customerId,@PathVariable Long addressId, @RequestBody Address address){
		Address address1=customerService.updateAddressOfCustomer(customerId,addressId,address);
		return new ResponseEntity<>(address1,HttpStatus.ACCEPTED);
	}
	@DeleteMapping("/{customerId}/addresses/{addressId}")
	public ResponseEntity<Address> deleteAddress(@PathVariable Long customerId,@PathVariable Long addressId){
		Address address1=customerService.deleteAddressOfCustomer(customerId,addressId);
		return new ResponseEntity<>(address1,HttpStatus.ACCEPTED);
	}
	@PostMapping("/login")
	public ResponseEntity<User> loginCustomer(@RequestBody CustomerLoginDTO customerDTO){

		User user =customerService.customerLogin(customerDTO.getEmail(),customerDTO.getUserPassword());
		return new ResponseEntity<>(user,HttpStatus.ACCEPTED);
	}
	
	
	
}
