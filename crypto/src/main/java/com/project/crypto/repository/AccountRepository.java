package com.project.crypto.repository;

import com.project.crypto.model.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<AccountModel, Integer> {
    AccountModel findAccountByAccountNumber(Integer accountId);
    List<AccountModel> findAll();
}
