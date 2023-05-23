package com.sweetopia.controller;

import java.util.List;

import com.sweetopia.entity.Product;
import com.sweetopia.exception.SessionsException;
import com.sweetopia.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sweetopia.entity.Category;
import com.sweetopia.exception.CategoryException;
import com.sweetopia.service.CategoryService;

@RestController
@RequestMapping("/sweetopia/categories")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class CategoryController {
    //done
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SessionService sessionService;

    @PostMapping("/add/{uuid}")
    public ResponseEntity<Category> addCategory(@PathVariable String uuid,@RequestBody Category category) throws CategoryException, SessionsException {
        if(!sessionService.isAdmin(uuid))throw new SessionsException("Customer cannot add category");
        Category addedCategory = categoryService.addCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedCategory);
    }

    @PutMapping("/update/{categoryId}/{uuid}")
    public ResponseEntity<Category> updateCategory(@PathVariable String uuid,@PathVariable Long categoryId, @RequestBody Category category) throws CategoryException, SessionsException {
        if(!sessionService.isAdmin(uuid))throw new SessionsException("Customer cannot update category");
        category.setCategoryId(categoryId);
        Category updatedCategory = categoryService.updateCategory(category);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{categoryId}/{uuid}")
    public ResponseEntity<Category> cancelCategory(@PathVariable String uuid,@PathVariable Long categoryId) throws CategoryException, SessionsException {
        if(!sessionService.isAdmin(uuid))throw new SessionsException("Customer cannot delete category");
        Category cancelledCategory = categoryService.cancelCategory(categoryId);
        return ResponseEntity.ok(cancelledCategory);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Category>> showAllCategory()  throws CategoryException{
        List<Category> categories = categoryService.showAllCategory();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryId}/totalCost")
    public ResponseEntity<Double> calculateTotalCost(@PathVariable Long categoryId) throws CategoryException {
        double totalCost = categoryService.calculateTotalCost(categoryId);
        return ResponseEntity.ok(totalCost);
    }
    
    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategory(@PathVariable Long categoryId) throws CategoryException {
    	Category category = categoryService.getCategory(categoryId);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) throws CategoryException {
        List<Product> products = categoryService.getProductsOfCategory(categoryId);
        return ResponseEntity.ok(products);
    }

   

}
