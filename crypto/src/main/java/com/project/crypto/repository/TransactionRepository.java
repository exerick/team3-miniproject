package com.project.crypto.repository;

import com.project.crypto.model.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionModel,Integer> {
    TransactionModel findTransactionById(Integer id);
    List<TransactionModel> findAll();
}
