package com.sweetopia.controller;

import com.sweetopia.entity.Product;
import com.sweetopia.exception.ProductException;
import com.sweetopia.exception.SessionsException;
import com.sweetopia.repository.ProductRepository;
import com.sweetopia.service.ProductService;
import com.sweetopia.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sweetopia")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ProductController {
    //done

    @Autowired
    private ProductService productService;

    @Autowired
    private SessionService sessionService;

    @PostMapping("/products/add/{uuid}")
    public ResponseEntity<Product> addProduct(@PathVariable String uuid,@Valid @RequestBody Product product) throws ProductException, SessionsException {
        sessionService.isSessionValid(uuid);
        if(!sessionService.isAdmin(uuid))throw new SessionsException("Customer cannot add products");
        Product p1=productService.addProduct(product);
        return new ResponseEntity<>(p1, HttpStatus.ACCEPTED);
    }
    @PostMapping("/products/add/all/{uuid}")
    public ResponseEntity<List<Product>> addProducts(@PathVariable String uuid,@Valid @RequestBody List<Product> product) throws ProductException, SessionsException {
        sessionService.isSessionValid(uuid);
        if(!sessionService.isAdmin(uuid))throw new SessionsException("Customer cannot add products");
        List<Product> p1=productService.addAllProducts(product);
        return new ResponseEntity<>(p1, HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/products/{id}/{uuid}")
    public ResponseEntity<Product> deleteProduct(@PathVariable String uuid,@PathVariable Long id) throws ProductException, SessionsException {
        sessionService.isSessionValid(uuid);
        if(!sessionService.isAdmin(uuid))throw new SessionsException("Customer cannot delete products");
        Product p1=productService.deleteProduct(id);
        return new ResponseEntity<>(p1,HttpStatus.OK);
    }
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() throws ProductException {
        List<Product> list=productService.getAllProducts();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id)throws ProductException {
        Product product=productService.getProductById(id);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    @PutMapping("/products/{id}/{uuid}")
    public ResponseEntity<Product> updateProduct(@PathVariable String uuid,@PathVariable Long id,@RequestBody Product product) throws ProductException, SessionsException {
        sessionService.isSessionValid(uuid);
        if(!sessionService.isAdmin(uuid))throw new SessionsException("Customer cannot update products");
        product.setProductId(id);
        Product p1=productService.updateProduct(product);
        return new ResponseEntity<>(p1,HttpStatus.OK);
    }

   @GetMapping("/products/paged")
    public ResponseEntity<List<Product>> getProductsByPage(@RequestParam("page") Integer pageNo,@RequestParam("size") Integer pageSize,@RequestParam(name = "sortBy",required = false) String sort,@RequestParam(name = "direction",required = false) String dir) throws ProductException {
        Pageable pageable;
        pageNo-=1;
        if(sort==null){
            pageable=PageRequest.of(pageNo,pageSize);
        }else{

            if(!dir.equals("DESC")){
                pageable=PageRequest.of(pageNo,pageSize, Sort.Direction.ASC,sort);
            }else{
                pageable=PageRequest.of(pageNo,pageSize, Sort.Direction.DESC,sort);
            }

        }
        List<Product> list=productService.getAllPagedProducts(pageable);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam("productName") String productName){
        return new ResponseEntity<>(productService.searchByName(productName),HttpStatus.OK);
    }

}
