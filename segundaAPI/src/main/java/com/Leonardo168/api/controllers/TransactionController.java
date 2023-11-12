package com.Leonardo168.api.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Leonardo168.api.models.ProductModel;
import com.Leonardo168.api.models.TransactionModel;
import com.Leonardo168.api.models.UserModel;
import com.Leonardo168.api.services.ProductService;
import com.Leonardo168.api.services.TransactionService;
import com.Leonardo168.api.services.UserService;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
	
	@Autowired
	TransactionService transactionService;
	@Autowired
	ProductService productService;
	@Autowired
	UserService userService;
	
	@PostMapping("/{productId}")
	public ResponseEntity<Object> saveTransaction(@PathVariable(value = "productId") UUID productId){
		Optional<ProductModel> productModelOptional = productService.findById(productId);
		if(!productModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
		}
		if(!productModelOptional.get().isAvailable()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not available.");
		}
		UserModel userModel = userService.findByUsername(userService.getCurrentUsername()).get();
		TransactionModel transactionModel = new TransactionModel();
		transactionModel.setBuyerId(userModel.getUserId());
		transactionModel.setProductId(productModelOptional.get().getProductId());
		transactionModel.setTransactionDate(LocalDateTime.now(ZoneId.of("UTC")));
		transactionService.save(transactionModel);
		ProductModel productModel = productModelOptional.get();
		productModel.setAvailable(false);
		productService.save(productModel);
		return ResponseEntity.status(HttpStatus.CREATED).body(transactionModel);
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity <Object> getProduct(@PathVariable(value = "userId") UUID userId, @PageableDefault(page = 0, size = 10, sort = "transactionDate", direction = Sort.Direction.ASC)Pageable pageable){
		Optional<Page<Object>> transactionModelOptional = transactionService.findByBuyerId(userId, pageable);
		if(transactionModelOptional.get().isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Transaction found for user: " + userId);
		}
		return ResponseEntity.status(HttpStatus.OK).body(transactionModelOptional.get());
	}
}