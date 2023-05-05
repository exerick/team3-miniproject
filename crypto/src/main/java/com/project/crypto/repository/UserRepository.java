package com.project.crypto.repository;

import com.project.crypto.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserModel,Integer> {
    UserModel findUserById (Integer id);
    UserModel findByUsername(String username);
    List<UserModel> findAll();
}
