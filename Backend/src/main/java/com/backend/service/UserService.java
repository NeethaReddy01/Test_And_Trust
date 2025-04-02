package com.backend.service;

import com.backend.model.*;
import com.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired private UserRepository userRepository;
    public User addUser(User user) { 
    	return userRepository.save(user); 
    	}
    public List<User> getAllUsers() { 
    	return userRepository.findAll(); 
    	}
    public Optional<User> getUserById(Long id) { 
    	return userRepository.findById(id); 
    	}
    public Optional<User> getUserByEmail(String email){
    	return userRepository.findByEmail(email);
    }
    public void deleteUser(Long id) { 
    	userRepository.deleteById(id); 
    	}
}

