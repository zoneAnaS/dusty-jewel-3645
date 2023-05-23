package com.sweetopia.controller;

import java.util.List;

import com.sweetopia.entity.Role;
import com.sweetopia.entity.User;
import com.sweetopia.exception.OrderNotFoundException;
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


import com.sweetopia.entity.OrderBill;
import com.sweetopia.exception.OrderBillNotFoundException;

import com.sweetopia.service.OrderBillService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/sweetopia/orderbill")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class OrderBillController {
	
	@Autowired
	private OrderBillService orderbillservice;
	@Autowired
	private SessionService sessionService;
	
	@PostMapping("/add/{orderId}/{uuid}")
	public ResponseEntity<OrderBill> addOrderBill(@PathVariable String uuid,@PathVariable Long orderId) throws OrderBillNotFoundException, OrderNotFoundException, SessionsException {
		User user= sessionService.getUserByUUID(uuid);
		OrderBill ordbill = orderbillservice.addOrderBill(user.getId(),orderId);
		return new ResponseEntity<>(ordbill, HttpStatus.CREATED);
	}
	
//	@PutMapping("/orderbills")
//	public ResponseEntity<OrderBill> updateOrderBill(@Valid @RequestBody OrderBill orderbill) throws OrderBillNotFoundException{
//		OrderBill ordbill = orderbillservice.updateOrderBill(orderbill);
//		return new ResponseEntity<>(ordbill, HttpStatus.OK);
//	}
	
//	@DeleteMapping("/orderbills/{orderBillId}")
//	public ResponseEntity<OrderBill> cancelOrderBill(@PathVariable Long orderBillId) throws OrderBillNotFoundException, OrderNotFoundException {
//		OrderBill ordbill = orderbillservice.cancelOrderBill(orderBillId);
//		return new ResponseEntity<>(ordbill, HttpStatus.OK);
//	}
	
	@GetMapping("/all/{uuid}")
	public ResponseEntity<List<OrderBill>> showAllOrderBills(@PathVariable String uuid) throws OrderBillNotFoundException, SessionsException {
		if(!sessionService.isAdmin(uuid))throw new SessionsException("Only admin can access this");
		List<OrderBill> orderbill = orderbillservice.showAllOrderBills();
		return new ResponseEntity<>(orderbill, HttpStatus.OK);
		
	}
	
	@GetMapping("/orderbills/{orderBillId}/{uuid}")
	public ResponseEntity<OrderBill> showAllOrderBillsById(@PathVariable String uuid,@PathVariable Long orderBillId) throws OrderBillNotFoundException, SessionsException {
		User user=sessionService.getUserByUUID(uuid);
		Long customerId=null;
		if(user.getRole()== Role.Customer)customerId=user.getId();
		OrderBill ordbill = orderbillservice.showAllOrderBillsById(customerId,orderBillId);
		return new ResponseEntity<>(ordbill, HttpStatus.OK);
	}
	
	@GetMapping("/orderbills/{uuid}")
	public ResponseEntity<List<OrderBill>> showAllBillOfCustomer(@PathVariable String uuid) throws OrderBillNotFoundException, SessionsException {
		User user=sessionService.getUserByUUID(uuid);
		List<OrderBill> ordbill = orderbillservice.showAllBillOfCustomer(user.getId());
		return new ResponseEntity<>(ordbill, HttpStatus.OK);
	}

}
