package com.project.crypto.controller;


import com.project.crypto.model.AccountModel;
import com.project.crypto.model.CardPaymentModel;
import com.project.crypto.model.CustomerModel;
import com.project.crypto.objectData.AccountRequest;
import com.project.crypto.objectData.CardPaymentRequest;
import com.project.crypto.repository.AccountRepository;
import com.project.crypto.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("account")
public class AccountController {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CustomerRepository customerRepository;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<AccountModel> inquiryAllAccount(){
        return accountRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody AccountModel inquiryAccountByAccountNumber(@PathVariable("id") Integer accountId){
        return accountRepository.findAccountByAccountNumber(accountId);
    }
    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public AccountModel addAccount(@RequestBody AccountRequest ar){
        AccountModel am = new AccountModel();
        am.setAccountName(ar.getAccountName());
        am.setType(ar.getType());
        am.setCurrency(ar.getCurrency());
        am.setAmount(0.0);
        am.setBuyCurrency(ar.getBuyCurrency());
        am.setTotalBuy(0.0);
        am.setAveragePerCoin(0.0);

        CustomerModel cm = customerRepository.findCustomerById(ar.getCustomerId());
        am.setCustomerModel(cm);

        return accountRepository.save(am);
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountModel updateAccount(@RequestBody AccountRequest ar,@PathVariable("id") Integer id){
        //get prev account to put last amount
        AccountModel last = accountRepository.findAccountByAccountNumber(ar.getAccountNumber());

        AccountModel am = new AccountModel();
        am.setAccountNumber(id);
        am.setAccountName(ar.getAccountName());
        am.setType(ar.getType());
        am.setCurrency(ar.getCurrency());
        am.setBuyCurrency(ar.getBuyCurrency());
        CustomerModel cm = customerRepository.findCustomerById(ar.getCustomerId());
        am.setCustomerModel(cm);

        am.setAmount(last.getAmount());
        am.setAveragePerCoin(last.getAveragePerCoin());
        am.setTotalBuy(last.getTotalBuy());



        return accountRepository.save(am);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccount(@PathVariable("id") Integer id){
        accountRepository.deleteById(id);
    }
}
