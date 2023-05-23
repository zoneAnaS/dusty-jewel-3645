package com.sweetopia.controller;

import java.util.List;

import com.sweetopia.entity.User;
import com.sweetopia.exception.ProductException;
import com.sweetopia.exception.SessionsException;
import com.sweetopia.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sweetopia.entity.Order;
import com.sweetopia.exception.OrderNotFoundException;
import com.sweetopia.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/sweetopia/orders")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class OrderController {
	
	@Autowired
	private OrderService orderservice;

	@Autowired
	private SessionService sessionService;
	
	
	
	@PostMapping("/add/{uuid}")
	public ResponseEntity<Order> addOrder(@PathVariable String uuid) throws OrderNotFoundException, ProductException, SessionsException {
		User user=sessionService.getUserByUUID(uuid);
		Order ord=orderservice.addSweetOrder(user.getId());
		return new ResponseEntity<>(ord, HttpStatus.CREATED);
		
	}
	
	@PutMapping("/update/{uuid}")
	public ResponseEntity<Order> UpdateSweetOrder(@PathVariable String uuid,@Valid @RequestBody Order order) throws OrderNotFoundException, SessionsException, ProductException {
		User user=sessionService.getUserByUUID(uuid);
		Order ord=orderservice.updateSweetOrder(user.getId(),order);
		return new ResponseEntity<>(ord, HttpStatus.OK);
	}



	@DeleteMapping("/orders/{orderId}/{uuid}")
	public ResponseEntity<Order> cancelSweetOrder(@PathVariable String uuid,@PathVariable Long orderId) throws OrderNotFoundException, SessionsException, ProductException {
		User user = sessionService.getUserByUUID(uuid);
		Order ord= orderservice.cancelSweetOrder(user.getId(),orderId);
		return new ResponseEntity<>(ord, HttpStatus.OK);
	}
	@GetMapping("/orders/{uuid}")
	public ResponseEntity<List<Order>> showAllSweetOrder(@PathVariable String uuid) throws OrderNotFoundException, SessionsException {
		if(!sessionService.isAdmin(uuid))throw new SessionsException("Only admin can access this");
		List<Order> ord = orderservice.showAllSweetOrder();
		return new ResponseEntity<>(ord, HttpStatus.OK);
		
	}
	
	@GetMapping("/orders/{orderId}/{uuid}")
	public ResponseEntity<Order>  showAllSweetOrderById(@PathVariable String uuid,@PathVariable Long orderId) throws OrderNotFoundException, SessionsException {
		Long customerId=null;
		if(sessionService.isCustomer(uuid))customerId=sessionService.getUserByUUID(uuid).getId();
		Order ord = orderservice.showAllSweetOrderById(customerId,orderId);
		return new ResponseEntity<>(ord, HttpStatus.OK);
		
	}
	
//	@GetMapping("/orders/{orderId}")
	public ResponseEntity<String> calculateTotalOrdercost(Long orderId) throws OrderNotFoundException{
		double total=orderservice.calculateTotalOrdercost(orderId);
		return new ResponseEntity<>("total order amount is "+total, HttpStatus.OK);
	}
	
	
	

}
