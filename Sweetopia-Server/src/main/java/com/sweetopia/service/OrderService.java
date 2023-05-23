package com.sweetopia.service;

import java.util.List;

import com.sweetopia.entity.Order;
import com.sweetopia.exception.OrderNotFoundException;
import com.sweetopia.exception.ProductException;

public interface OrderService {

	public Order addSweetOrder(Long customerId) throws OrderNotFoundException, ProductException;
	
	public Order updateSweetOrder(Long customerId,Order order) throws OrderNotFoundException, ProductException;

	public Order cancelSweetOrder(Long customerId,Long orderId) throws OrderNotFoundException, ProductException;
	
	public List<Order> showAllSweetOrder() throws OrderNotFoundException;
	
	
	public Order showAllSweetOrderById(Long customerId,Long orderId) throws OrderNotFoundException;
	public Order showAllSweetOrderById(Long orderId) throws OrderNotFoundException;
	
	public double calculateTotalOrdercost(Long orderId) throws OrderNotFoundException;
	

}
