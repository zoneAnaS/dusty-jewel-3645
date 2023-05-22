package com.sweetopia.controller;

import java.util.List;

import com.sweetopia.dto.CustomerDTO;
import com.sweetopia.dto.CustomerLoginDTO;
import com.sweetopia.entity.Address;
import com.sweetopia.entity.Cart;
import com.sweetopia.exception.SessionsException;
import com.sweetopia.service.SessionService;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sweetopia.entity.User;
import com.sweetopia.service.UserService;




@RestController
@RequestMapping("/sweetopia/customers")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class UserController {

	
	@Autowired
	private UserService userService;
	@Autowired
	private SessionService sessionService;
	
	@PostMapping
	public ResponseEntity<User> addCustomer(@RequestBody CustomerDTO customerDTO){
		User user =new User();
		user.setUserName(customerDTO.getUserName());
		user.setUserPassword(customerDTO.getUserPassword());
		user.setEmail(customerDTO.getEmail());
		user.setRole(customerDTO.getRole());
		Cart cart=new Cart();
		user.setCart(cart);

			userService.addCustomer(user);
			return ResponseEntity.status(HttpStatus.CREATED).body(user);

	}
	
	@PutMapping("/{uuid}")
	public ResponseEntity<User> updateCustomer(@PathVariable("uuid")String uuid, @RequestBody User user) throws Exception {
			sessionService.isSessionValid(uuid);
			User user1=sessionService.getUserByUUID(uuid);
			User user2=userService.updateCustomer(user1.getId(),user);
			return ResponseEntity.ok(user2);
	}
	
	@GetMapping("/all/{uuid}")
	public ResponseEntity<List<User>> showAllCustomers(@PathVariable String uuid) throws SessionsException {
		if(!sessionService.isAdmin(uuid))throw new SessionsException("Customers cannot access this");
		List<User> users = userService.showAllCustomers();
		return ResponseEntity.ok(users);
	}
	
	@GetMapping("/{uuid}")
	public ResponseEntity<User> showCustomerByUUID(@PathVariable("uuid")String uuid) throws SessionsException {
		User user = sessionService.getUserByUUID(uuid);
		return ResponseEntity.ok(user);
	}
	@GetMapping("/{uuid}/addresses")
	public ResponseEntity<List<Address>> getAlladdress(@PathVariable String uuid) throws SessionsException {
		List<Address> address1= sessionService.getUserByUUID(uuid).getAddresses();
		return new ResponseEntity<>(address1,HttpStatus.OK);
	}
	@PostMapping("/{uuid}/addresses")
	public ResponseEntity<Address> addAddressToCustomer(@PathVariable String uuid, @RequestBody Address address) throws SessionsException {
		User user=sessionService.getUserByUUID(uuid);
		Address address1= userService.addAddressToCustomer(user.getId(),address);
		return new ResponseEntity<>(address1,HttpStatus.ACCEPTED);
	}
	@PutMapping("/{uuid}/addresses/{addressId}")
	public ResponseEntity<Address> addAddressToCustomer(@PathVariable String uuid,@PathVariable Long addressId, @RequestBody Address address) throws SessionsException {
		User user=sessionService.getUserByUUID(uuid);
		Address address1= userService.updateAddressOfCustomer(user.getId(),addressId,address);
		return new ResponseEntity<>(address1,HttpStatus.ACCEPTED);
	}
	@DeleteMapping("/{uuid}/addresses/{addressId}")
	public ResponseEntity<Address> deleteAddress(@PathVariable String uuid,@PathVariable Long addressId) throws SessionsException {
		User user=sessionService.getUserByUUID(uuid);
		Address address1= userService.deleteAddressOfCustomer(user.getId(),addressId);
		return new ResponseEntity<>(address1,HttpStatus.ACCEPTED);
	}
	@PostMapping("/login")
	public ResponseEntity<String> loginCustomer(@RequestBody CustomerLoginDTO customerDTO){
		User user = userService.customerLogin(customerDTO.getEmail(),customerDTO.getUserPassword());
		return new ResponseEntity<>(sessionService.createSession(user),HttpStatus.OK);
	}
	
	
	
}
