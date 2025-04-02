package com.backend.controller;

import com.backend.model.*;
import com.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*") 
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired private UserService userService;
    @PostMapping 
    public ResponseEntity<User> addUser(@RequestBody User user) { 
    	return ResponseEntity.ok(userService.addUser(user)); 
    	}
    
    @GetMapping 
    public ResponseEntity<List<User>> getAllUsers() { 
    	return ResponseEntity.ok(userService.getAllUsers()); 
    	}
    
    @GetMapping("/{id}") 
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}") 
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id); 
        return ResponseEntity.noContent().build();
    }
}

