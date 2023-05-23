package com.sweetopia.controller;

import java.util.List;

import com.sweetopia.entity.User;
import com.sweetopia.exception.ProductException;
import com.sweetopia.exception.SessionsException;
import com.sweetopia.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sweetopia.entity.Cart;
import com.sweetopia.service.CartService;

@RestController
@RequestMapping("/sweetopia/carts")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class CartController {
	//done

	@Autowired
	private CartService cartService;
	@Autowired
	private SessionService sessionService;
	

	@PostMapping("/addtocart/{uuid}")
	public ResponseEntity<Cart> addProductToCart(@RequestParam("uuid") String uuid,@RequestParam("productId") Long productId,@RequestParam("quantity") Integer quantity) throws ProductException, SessionsException {
		User user=sessionService.getUserByUUID(uuid);
		Cart cart=cartService.addProductToCart(user.getId(), productId,quantity);
		return new ResponseEntity<>(cart,HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/update/{uuid}")
	public ResponseEntity<Cart> updateCart(@PathVariable String uuid, @RequestBody Cart cart) throws ProductException, SessionsException {

			User user = sessionService.getUserByUUID(uuid);
			cart.setCartId(user.getCart().getCartId());
			Cart updatedCart = cartService.updateCart(cart);
			return new ResponseEntity<Cart>(updatedCart,HttpStatus.OK);
			

	}
	
	@GetMapping("/all/{uuid}")
	public ResponseEntity<List<Cart>> showAllCarts(@PathVariable String uuid) throws SessionsException {
		if(!sessionService.isAdmin(uuid))throw new SessionsException("Customer cannot access this");
		List<Cart> carts = cartService.showAllCarts();
		return new ResponseEntity<List<Cart>>(carts,HttpStatus.OK);
	}
	@GetMapping("/{cartId}/{uuid}")
	public ResponseEntity<Cart> showCartById(@PathVariable Long cartId,@PathVariable String uuid) throws SessionsException {
		if(sessionService.isCustomer(uuid))throw new SessionsException("Customer cannot access this");
		Cart carts = cartService.showAllCarts(cartId);
		return new ResponseEntity<>(carts,HttpStatus.OK);

	}
	
	@GetMapping("/{uuid}")
	public ResponseEntity<Cart> showCartById(@PathVariable String uuid) throws SessionsException {
			User user =sessionService.getUserByUUID(uuid);
			Cart carts = cartService.showAllCarts(user.getCart().getCartId());
			return new ResponseEntity<>(carts,HttpStatus.OK);

	}
	
}
