package com.project.crypto.controller;

import com.project.crypto.model.*;
import com.project.crypto.model.ExchangeRateModel;
import com.project.crypto.objectData.ExchangeRateRequest;
import com.project.crypto.repository.ExchangeRateRepository;
import com.project.crypto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("exchange_rate")
public class ExchangeRateController {
    @Autowired
    ExchangeRateRepository exchangeRateRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<ExchangeRateModel> inquiryAllExchangeRate() {
        return exchangeRateRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ExchangeRateModel inquiryExchangeRateByRateId(@PathVariable("id") Integer ExchangeRateId) {
        return exchangeRateRepository.findExchangeRateByRateId(ExchangeRateId);
    }
    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ExchangeRateModel addExchangeRate(@RequestBody ExchangeRateRequest err){
        ExchangeRateModel erm = new ExchangeRateModel();
        erm.setLastDateUpdate(err.getLastDateUpdate());
        erm.setCurrency(err.getCurrency());
        erm.setPriceBuy(err.getPriceBuy());
        erm.setPriceSell(err.getPriceSell());

        UserModel userModel = userRepository.findUserById(err.getUserId());
        erm.setUserModel(userModel);

        return exchangeRateRepository.save(erm);
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ExchangeRateModel updateExchangeRate(@RequestBody ExchangeRateRequest err,@PathVariable("id") Integer id){
        ExchangeRateModel erm = new ExchangeRateModel();
        erm.setRateId(id);
        erm.setLastDateUpdate(err.getLastDateUpdate());
        erm.setCurrency(err.getCurrency());
        erm.setPriceBuy(err.getPriceBuy());
        erm.setPriceSell(err.getPriceSell());

        UserModel userModel = userRepository.findUserById(err.getUserId());
        erm.setUserModel(userModel);

        return exchangeRateRepository.save(erm);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteExchangeRate(@PathVariable("id") Integer id){
        exchangeRateRepository.deleteById(id);
    }

}