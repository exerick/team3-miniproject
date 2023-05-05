package com.project.crypto.controller;

import com.project.crypto.model.*;
import com.project.crypto.model.TransactionModel;
import com.project.crypto.objectData.TransactionRequest;
import com.project.crypto.repository.AccountRepository;
import com.project.crypto.repository.CardPaymentRepository;
import com.project.crypto.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("transaction")
public class TransactionController {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CardPaymentRepository cardPaymentRepository;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<TransactionModel> inquiryAllTransaction(){
        return transactionRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody TransactionModel inquiryTransactionByid(@PathVariable("id") Integer tranId){
        return transactionRepository.findTransactionById(tranId);
    }


}
