package com.project.crypto.controller;

import com.project.crypto.model.CustomerModel;
import com.project.crypto.model.UserModel;
import com.project.crypto.model.UserModel;
import com.project.crypto.objectData.CustomerRequest;
import com.project.crypto.objectData.UserRequest;
import com.project.crypto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<UserModel> inquiryAllUser(){
        return userRepository.findAll();
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody UserModel inquiryUserById(@PathVariable("id") Integer id){
        return userRepository.findUserById(id);
    }
    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public UserModel addUser(@RequestBody UserRequest ur){
        UserModel um = new UserModel();
        um.setName(ur.getName());
        um.setName(ur.getName());
        um.setEmail(ur.getEmail());
        um.setPhone(ur.getPhone());
        um.setAddress(ur.getAddress());
        um.setRole(ur.getRole());
        um.setBirthDate(ur.getBirthDate());
        um.setUsername(ur.getUsername());
        um.setPassword(ur.getPassword());
        return userRepository.save(um);
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserModel updateUser(@RequestBody UserRequest ur,@PathVariable("id") Integer id){
        UserModel um = new UserModel();
        um.setId(id);
        um.setName(ur.getName());
        um.setName(ur.getName());
        um.setEmail(ur.getEmail());
        um.setPhone(ur.getPhone());
        um.setAddress(ur.getAddress());
        um.setRole(ur.getRole());
        um.setBirthDate(ur.getBirthDate());
        um.setUsername(ur.getUsername());
        um.setPassword(ur.getPassword());
        return userRepository.save(um);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable("id") Integer id){
        userRepository.deleteById(id);
    }
}
