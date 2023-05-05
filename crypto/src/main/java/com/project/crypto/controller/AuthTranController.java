package com.project.crypto.controller;

import com.project.crypto.model.AccountModel;
import com.project.crypto.model.CardPaymentModel;
import com.project.crypto.model.ExchangeRateModel;
import com.project.crypto.model.TransactionModel;
import com.project.crypto.objectData.AuthTranRequest;
import com.project.crypto.objectData.AuthTranResponse;
import com.project.crypto.repository.AccountRepository;
import com.project.crypto.repository.CardPaymentRepository;
import com.project.crypto.repository.ExchangeRateRepository;
import com.project.crypto.repository.TransactionRepository;
import com.project.crypto.service.CCPaymentGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("auth_tran")
public class AuthTranController {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ExchangeRateRepository exchangeRateRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CardPaymentRepository cardPaymentRepository;

    @Autowired
    CCPaymentGatewayService ccPaymentGatewayService;

    @PostMapping("/buy")
    @ResponseStatus(HttpStatus.OK)
    public AuthTranResponse buyAuth(@RequestBody AuthTranRequest atr){
        //init variable
        Double trxPoin = 0.0;
        Double trxRate = 0.0;
        //init respon status
        AuthTranResponse atres = new AuthTranResponse();
        atres.setAccountNumber(atr.getAccountNumber());
        atres.setOriginalCurrency(atr.getOriginalCurrency());
        atres.setOriginalAmount(atr.getOriginalAmount());
        atres.setTransactionType(atr.getTransactionType());
        atres.setReferenceNumber(atr.getReferenceNumber());

        // authorization sequence process for buy crypto

        //cek apakah account ada di db
        AccountModel accountModel = accountRepository.findAccountByAccountNumber(atres.getAccountNumber());
        if (accountModel.getAccountNumber().equals(null))
        {
            atres.setStatus("Account Not Found");
            return atres;
        }

        //type harus beli

        if (!atr.getTransactionType().equalsIgnoreCase("Buy")){
            atres.setStatus("Invalid Type");
            return atres;
        }

        // payment id harus terisi
        if(atr.getCardPaymentId().equals(null) || atr.getCardPaymentId().equals(0))
        {
            atres.setStatus("Payment Id is invalid");
            return atres;
        }

        //untuk saat ini pembayaran harus idr

        if(!atr.getOriginalCurrency().equalsIgnoreCase("IDR")){
            atres.setStatus("Invalid Currency");
            return atres;
        }

        //amount harus lebih dari 0
        if (atr.getOriginalAmount() < 0){
            atres.setStatus("Invalid Amount");
            return atres;
        }

        // cari exchange rate
        ExchangeRateModel exchangeRateModel = exchangeRateRepository.findExchangeRateByCurrency(accountModel.getCurrency());
        if (exchangeRateModel.getRateId().equals(null)){
            atres.setStatus("Internal error");
            return atres;
        }

        //calculate crypto yg didapat
        trxRate = exchangeRateModel.getPriceBuy();
        trxPoin = atr.getOriginalAmount() / trxRate;

        Random rand = new Random();
        Integer upperbound = 1000000;
        Integer int_random = rand.nextInt(upperbound);

        Date trxDate = new Date();

        TransactionModel saveTM = saveTransaction(
                atr, accountModel,trxPoin,trxRate,trxDate,int_random,atr.getCardPaymentId()
        );

        //hit ke kafka untuk proses payment
        String paymentRequestStatus = ccPaymentGatewayService.requestPayment(atr.getCardPaymentId(),atr.getOriginalAmount(),saveTM.getId());
        if(!paymentRequestStatus.equalsIgnoreCase("Success")){
            atres.setStatus("Payment Gateway Error");
            return atres;
        }


        // isi respon setelahnya
        atres.setCurrency(accountModel.getCurrency());
        atres.setAmount(trxPoin);
        atres.setConversionRate(exchangeRateModel.getPriceBuy());
        atres.setTransactionDate(trxDate);
        atres.setStatus("Pending");
        atres.setTranCode(int_random.toString());
        atres.setPaymentId(saveTM.getId());

        return atres;
    }


    public TransactionModel saveTransaction(AuthTranRequest atr,AccountModel accountModel,Double trxPoin,Double trxRate,Date trxDate,
                                            Integer int_random, Integer cardPaymentId){

        TransactionModel tm = new TransactionModel();

        tm.setOriginalCurrency(atr.getOriginalCurrency());
        tm.setOriginalAmount(atr.getOriginalAmount());
        tm.setCurrency(accountModel.getCurrency());
        tm.setAmount(trxPoin);
        tm.setConversionRate(trxRate);
        tm.setTransactionDate(trxDate);
        tm.setReferenceNumber(atr.getReferenceNumber());
        tm.setAccountModels(accountModel);
        tm.setStatus("Pending");
        tm.setTranCode(int_random);
        tm.setTransactionType(atr.getTransactionType());
        if(atr.getTransactionType().equalsIgnoreCase("Sell"))
        {
            tm.setDescription("Sell crypto for transaction" + atr.getMerchantName());
            tm.setDebitCredit("Credit");
            CardPaymentModel cardPaymentModel = cardPaymentRepository.getReferenceById(atr.getCardPaymentId());
            tm.setCardPaymentModel(cardPaymentModel);
        }
        else {
            tm.setDescription("Buy crypto from" + atr.getMerchantName());
            tm.setDebitCredit("Debit");
        }

        return transactionRepository.save(tm);
    }


}
