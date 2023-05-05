package com.project.crypto.controller;

import com.project.crypto.model.CardPaymentModel;
import com.project.crypto.model.CustomerModel;
import com.project.crypto.objectData.CardPaymentRequest;
import com.project.crypto.objectData.CustomerRequest;
import com.project.crypto.repository.CardPaymentRepository;
import com.project.crypto.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("card_payment")
public class CardPaymentController {
    @Autowired
    CardPaymentRepository cardPaymentRepository;

    @Autowired
    CustomerRepository customerRepository;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<CardPaymentModel> inquiryAllCardPayment(){
        return cardPaymentRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody CardPaymentModel inquiryCardPaymentByid(@PathVariable("id") Integer payId){
        return cardPaymentRepository.findCardPaymentByPaymentId(payId);
    }
    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public CardPaymentModel addCardPayment(@RequestBody CardPaymentRequest cpr){
        CardPaymentModel cpm = new CardPaymentModel();
        cpm.setCardNumber(cpr.getCardNumber());
        cpm.setExpDate(cpr.getExpDate());
        cpm.setCvv(cpr.getCvv());
        cpm.setCardName(cpr.getCardName());
        CustomerModel customerModel = customerRepository.findCustomerById(cpr.getCustomerId());
        cpm.setCustomerModel(customerModel);
        return cardPaymentRepository.save(cpm);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CardPaymentModel updateCardPayment(@RequestBody CardPaymentRequest cpr,@PathVariable("id") Integer id){
        CardPaymentModel cpm = new CardPaymentModel();
        cpm.setPaymentId(id);
        cpm.setCardNumber(cpr.getCardNumber());
        cpm.setExpDate(cpr.getExpDate());
        cpm.setCvv(cpr.getCvv());
        cpm.setCardName(cpr.getCardName());

        CustomerModel customerModel = customerRepository.findCustomerById(cpr.getCustomerId());
        cpm.setCustomerModel(customerModel);

        return cardPaymentRepository.save(cpm);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCardPayment(@PathVariable("id") Integer id){
        cardPaymentRepository.deleteById(id);
    }
}
