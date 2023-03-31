package com.sweetopia.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.sweetopia.entity.OrderBill;
import com.sweetopia.exception.OrderBillNotFoundException;

import com.sweetopia.service.OrderBillService;

import jakarta.validation.Valid;

@RestController
public class OrderBillController {
	
	@Autowired
	private OrderBillService orderbillservice;
	
	@PostMapping("/orderbill")
	public ResponseEntity<OrderBill> addOrderBill(@Valid @RequestBody OrderBill orderbill) throws OrderBillNotFoundException{
		
		OrderBill ordbill = orderbillservice.addOrderBill(orderbill);
		return new ResponseEntity<>(ordbill, HttpStatus.CREATED);
	}
	
	@PutMapping("/orderbills")
	public ResponseEntity<OrderBill> updateOrderBill(@Valid @RequestBody OrderBill orderbill) throws OrderBillNotFoundException{
		OrderBill ordbill = orderbillservice.updateOrderBill(orderbill);
		return new ResponseEntity<>(ordbill, HttpStatus.OK);
	}
	
	@DeleteMapping("/orderbills/{orderBillId}")
	public ResponseEntity<OrderBill> cancelOrderBill(@PathVariable Long orderBillId) throws OrderBillNotFoundException{
		OrderBill ordbill = orderbillservice.cancelOrderBill(orderBillId);
		return new ResponseEntity<>(ordbill, HttpStatus.OK);
	}
	
	@GetMapping("/orderbills")
	public ResponseEntity<List<OrderBill>> showAllOrderBills() throws OrderBillNotFoundException{
		List<OrderBill> orderbill = orderbillservice.showAllOrderBills();
		return new ResponseEntity<>(orderbill, HttpStatus.OK);
		
	}
	
	@GetMapping("/orderbills/{orderBillId}")
	public ResponseEntity<OrderBill> showAllOrderBillsById(Long orderBillId) throws OrderBillNotFoundException {
		OrderBill ordbill = orderbillservice.showAllOrderBillsById(orderBillId);
		return new ResponseEntity<>(ordbill, HttpStatus.OK);
	}

}
