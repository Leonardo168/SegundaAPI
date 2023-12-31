package com.Leonardo168.api.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Leonardo168.api.dtos.UserRecordDto;
import com.Leonardo168.api.enums.RoleName;
import com.Leonardo168.api.models.RoleModel;
import com.Leonardo168.api.models.UserModel;
import com.Leonardo168.api.services.ProductService;
import com.Leonardo168.api.services.TransactionService;
import com.Leonardo168.api.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService userService;
	@Autowired
	ProductService productService;
	@Autowired
	TransactionService transactionService;
	
	@PostMapping
	public ResponseEntity <Object> saveUser(@RequestBody @Valid UserRecordDto userRecordDto){
		if(userService.existsByUsername(userRecordDto.username())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Username is already in use!");
		}
		UserModel userModel = new UserModel();
		BeanUtils.copyProperties(userRecordDto, userModel);
		List<RoleModel> roles = new ArrayList<>();
		roles.add(new RoleModel(UUID.fromString("f3bd1ddd-2b45-4dfd-be30-95fc4d21f97e"),RoleName.ROLE_USER));
		userModel.setRoles(roles);
		userModel.setEnable(true);
		userModel.setPassword(new BCryptPasswordEncoder().encode(userModel.getPassword()));
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userModel));
	}
	
	@GetMapping
	public ResponseEntity<Page<UserModel>> getAllUsers(@PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC)Pageable pageable){
		return ResponseEntity.status(HttpStatus.OK).body(userService.findAll(pageable));
	}
	
	@PutMapping
	public ResponseEntity <Object> updadateUser(@RequestBody @Valid UserRecordDto userRecordDto){
		if((userService.existsByUsername(userRecordDto.username())) && (!userService.getCurrentUsername().equals(userRecordDto.username()))) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Username is already in use!");
		}
		UserModel userModel = userService.findByUsername(userService.getCurrentUsername()).get();
		BeanUtils.copyProperties(userRecordDto, userModel);
		userModel.setPassword(new BCryptPasswordEncoder().encode(userModel.getPassword()));
		return ResponseEntity.status(HttpStatus.OK).body(userService.save(userModel));
	}
	
	@PutMapping("/admin/{id}")
	public ResponseEntity <Object> setAdminRole(@PathVariable(value = "id") UUID id){
		Optional<UserModel> userModelOptional = userService.findByID(id);
		if(!userModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}
		if(userModelOptional.get().getRoles().contains(new RoleModel(UUID.fromString("0c5d4f9a-51cb-48ba-ac18-745b87b5cb10"),RoleName.ROLE_ADMIN))) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("User already has admin role");
		}
		UserModel userModel = new UserModel();
		BeanUtils.copyProperties(userModelOptional.get(), userModel);
		List<RoleModel> roles = userModel.getRoles();
		roles.add(new RoleModel(UUID.fromString("0c5d4f9a-51cb-48ba-ac18-745b87b5cb10"),RoleName.ROLE_ADMIN));
		userModel.setRoles(roles);
		userService.save(userModel);
		return ResponseEntity.status(HttpStatus.OK).body("The user has been given an admin role.");
	}
	
	@DeleteMapping("/self")
	public ResponseEntity<Object> disableCurrentUser(){
		UserModel userModel = userService.findByUsername(userService.getCurrentUsername()).get();
		userModel.setEnable(false);
		userService.save(userModel);
		return ResponseEntity.status(HttpStatus.OK).body("User disabled");
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> disableUserById(@PathVariable(value = "id") UUID id){
		if (id.equals(UUID.fromString("eae7e721-05ee-4c59-95a5-e4a845c2ad8e"))) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("cannot disable default admin");
		}
		Optional<UserModel> userModelOptional = userService.findByID(id);
		if(!userModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}
		if(!userModelOptional.get().isEnabled()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("User is already disabled");
		}
		UserModel userModel = new UserModel();
		BeanUtils.copyProperties(userModelOptional.get(), userModel);
		userModel.setEnable(false);
		userService.save(userModel);
		return ResponseEntity.status(HttpStatus.OK).body("User disabled");
	}
	
	@DeleteMapping("definitivo/{id}")
	public ResponseEntity<Object> deleteUserById(@PathVariable(value = "id") UUID id){
		if (id.equals(UUID.fromString("eae7e721-05ee-4c59-95a5-e4a845c2ad8e"))) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("cannot delete default admin");
		}
		if (transactionService.existsByBuyerIdOrVendorId(id, id)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot delete users with registered transactions.");
		}
		if (productService.existsByVendorId(id)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot delete users with registered products.");
		}
		Optional<UserModel> userModelOptional = userService.findByID(id);
		if(!userModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}
		userService.delete(userModelOptional.get());
		return ResponseEntity.status(HttpStatus.OK).body("User deleted.");
	}

}
