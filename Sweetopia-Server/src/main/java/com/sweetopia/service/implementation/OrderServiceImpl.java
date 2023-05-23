package com.sweetopia.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sweetopia.dto.ProductDTO;
import com.sweetopia.entity.Cart;
import com.sweetopia.entity.Product;
import com.sweetopia.entity.User;
import com.sweetopia.exception.ProductException;
import com.sweetopia.service.CartService;
import com.sweetopia.service.ProductService;
import com.sweetopia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sweetopia.entity.Order;
import com.sweetopia.exception.OrderNotFoundException;
import com.sweetopia.repository.OrderRepository;
import com.sweetopia.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderRepository orderrepository;
	@Autowired
	private UserService userService;
	@Autowired
	private CartService cartService;
	@Autowired
	private ProductService productService;


	@Override
	public Order addSweetOrder(Long customerId) throws OrderNotFoundException, ProductException {
		// TODO Auto-generated method

		User user = userService.getCustomerById(customerId);
		List<ProductDTO> list= user.getCart().getListProduct();
		System.out.println(list);
		if(list.isEmpty())throw new OrderNotFoundException("Cart is empty add product to cart");
		Order order1=new Order();
		order1.setUser(user);
		for(ProductDTO p: user.getCart().getListProduct()){
			order1.getGroupedProducts().add(p);
		}

		Cart cart=cartService.showAllCarts(user.getCart().getCartId());
		cart.setGrandTotal(0.0);
		cart.setTotal(0.0);
		cart.setProductCount(0);
		cart.setListProduct(new ArrayList<>());
		cartService.updateCart(cart);

		return orderrepository.save(order1);
		 
	}

	@Override
	public Order updateSweetOrder(Long customerId,Order order) throws OrderNotFoundException, ProductException {
		// TODO Auto-generated method stub
		if(customerId==null)throw new OrderNotFoundException("customerId cannot be null");
		Optional<Order> ord;
		if(order.getOrderId()!=null) {
			Long id=order.getOrderId();
			if(orderrepository.findById(id).isEmpty())throw new OrderNotFoundException("Order not found");
			ord=orderrepository.findById(id);

		}else{
			throw new OrderNotFoundException("Order id cannot be null");
		}
		User user = userService.getCustomerById(customerId);
		Order order2=null;
		for(Order order1: user.getOrders()){
			if(order1.getOrderId()==order.getOrderId()){
				if(order1.getOrderBill()!=null)throw new OrderNotFoundException("Order has already been delivered");
				order2=order1;
				break;
			}
		}
		if(order2!=null){
			List<ProductDTO> products=order2.getGroupedProducts();
			if(order.getGroupedProducts()!=null)order2.setGroupedProducts(new ArrayList<>());
			for(ProductDTO p:products){
				Product p1=productService.getProductById(p.getProductId());
				p1.setAvailable(p1.getAvailable()+p.getQuantity());
				productService.updateProduct(p1);
			}
			for(ProductDTO p:order.getGroupedProducts()){
				try {
					Product p1=productService.getProductById(p.getProductId());
					productService.removeQuantity(p1.getProductId(),p.getQuantity());
					order2.getGroupedProducts().add(p);
				}catch(ProductException ex){

				}
			}
			return orderrepository.save(order2);
		}else{
			throw new OrderNotFoundException("Order with id: "+order.getOrderId()+" does not belong to customer id: "+customerId);
		}


		
	}

	@Override
	public Order cancelSweetOrder(Long customerId,Long orderId) throws OrderNotFoundException, ProductException {
		// TODO Auto-generated method stub

		User userOp=userService.getCustomerById(customerId);
		Order ord=null;
		for(Order o:userOp.getOrders()){
			if(o.getOrderId()==orderId){
				ord=o;
				if(o.getOrderBill()!=null)throw new ProductException("This order has already been delivered");
				break;
			}
		}

		if(ord!=null) {
			ord.setUser(null);
			List<ProductDTO> products=ord.getGroupedProducts();
			for(ProductDTO p:products){
				Product p1=productService.getProductById(p.getProductId());
				p1.setAvailable(p1.getAvailable()+p.getQuantity());
				productService.updateProduct(p1);
			}
			orderrepository.deleteById(orderId);
			return ord;
		}else {
			throw new OrderNotFoundException("Order with id " + orderId + " does not exist for the customer");
		}
		
		
	}

	@Override
	public List<Order> showAllSweetOrder() throws OrderNotFoundException {
		// TODO Auto-generated method stub
		List<Order> ord=orderrepository.findAll();
		if(ord.isEmpty()) {
			throw new OrderNotFoundException("No Order Found !");
		}else {
			return ord;
		}
		
	}
	@Override
	public Order showAllSweetOrderById(Long orderId) throws OrderNotFoundException {
		// TODO Auto-generated method stub
		Optional<Order> ord=orderrepository.findById(orderId);
		if(ord.isEmpty()) {
			throw new OrderNotFoundException("Order with id " + orderId + " does not exist");
		}else {
			return ord.get();
		}


	}
	@Override
	public Order showAllSweetOrderById(Long customerId,Long orderId) throws OrderNotFoundException {
		// TODO Auto-generated method stub
		if(customerId==null){
			Optional<Order> ord=orderrepository.findById(orderId);
			if(ord.isEmpty()) {
				throw new OrderNotFoundException("Order with id " + orderId + " does not exist");
			}else {
				return ord.get();
			}
		}else{
			Optional<Order> ord=orderrepository.findById(orderId);
			if(ord.isEmpty()) {
				throw new OrderNotFoundException("Order with id " + orderId + " does not exist");

			}else {
				if(ord.get().getUser().getId()!=customerId)throw new OrderNotFoundException("Order does not belong to customer with id: "+customerId);
				return ord.get();
			}
		}

		
		
	}

	@Override
	public double calculateTotalOrdercost(Long orderId) throws OrderNotFoundException {
		// TODO Auto-generated method stub
		Optional<Order> orders = orderrepository.findById(orderId);
        if (orders.isEmpty()) {
            throw new OrderNotFoundException("Order with id " + orderId + " does not exist");
        }
        return orders.get().getOrderBill().getTotalCost();
		
	}

}
