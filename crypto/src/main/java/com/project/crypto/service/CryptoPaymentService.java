package com.project.crypto.service;

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
import kafka.CryptoPayemntGatewayAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class CryptoPaymentService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ExchangeRateRepository exchangeRateRepository;

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    CardPaymentRepository cardPaymentRepository;

    @Autowired
    private KafkaTemplate<Integer, CryptoPayemntGatewayAuth> authResponse;


    public CryptoPaymentService(AccountRepository accountRepository, ExchangeRateRepository exchangeRateRepository, TransactionRepository transactionRepository, CardPaymentRepository cardPaymentRepository, KafkaTemplate<Integer, CryptoPayemntGatewayAuth> authResponse) {
        this.accountRepository = accountRepository;
        this.exchangeRateRepository = exchangeRateRepository;
        this.transactionRepository = transactionRepository;
        this.cardPaymentRepository = cardPaymentRepository;
        this.authResponse = authResponse;
    }

    public String consumePaymentGateway (CryptoPayemntGatewayAuth cpga){
        CryptoPayemntGatewayAuth cryptoPayemntGatewayAuth = authSellTransaction(cpga);
        authResponse.send("CryptoPaymentAuth",cpga.getPaymentId(),cpga);
        return "ok";
    }

    private CryptoPayemntGatewayAuth authSellTransaction(CryptoPayemntGatewayAuth cpga){
        //init respon status
        Double trxPoin = 0.0;
        Double trxRate = 0.0;
        cpga.setMessageType("Response");

        //cek apakah account ada di db
        AccountModel accountModel = accountRepository.findAccountByAccountNumber(cpga.getAccountNumber());
        if (accountModel == null)
        {
            cpga.setStatus("Account Not Found");
            return cpga;
        }

        //type harus jual
        if (!cpga.getTransactionType().equalsIgnoreCase("Sell")){
            cpga.setStatus("Invalid Type");
            return cpga;
        }

        //ori amount tidak boleh kurang dari 0
        if (cpga.getOriginalAmount() < 0 ){
            cpga.setStatus("Invalid Amount");
            return cpga;
        }

        if(cpga.getOriginalCurrency().equals(accountModel.getCurrency()))
        {
            trxPoin = cpga.getOriginalAmount();

        } else if (cpga.getOriginalCurrency().equalsIgnoreCase("IDR")) {
            // cari exchange rate
            ExchangeRateModel exchangeRateModel = exchangeRateRepository.findExchangeRateByCurrency(accountModel.getCurrency());
            if (exchangeRateModel.getRateId().equals(null)){
                cpga.setStatus("Internal error");
                return cpga;
            }
            trxRate = exchangeRateModel.getPriceSell();
            trxPoin = cpga.getOriginalAmount() / exchangeRateModel.getPriceSell();

        } else {
            cpga.setStatus("Invalid Currency");
            return cpga;
        }

        Double finalAmount = accountModel.getAmount() - trxPoin;
        Double finalTotalBuy = accountModel.getTotalBuy() - cpga.getOriginalAmount();
        Double finalAvg = 0.0;
        if (!finalAmount.equals(0.0))
        {
            finalAvg =finalTotalBuy / finalAmount;
        }

        if (finalAmount < 0)
        {
            cpga.setStatus("Funds is not enough");
            return cpga;
        }

        Random rand = new Random();
        Integer upperbound = 1000000;
        Integer int_random = rand.nextInt(upperbound);
        Date trxDate = new Date();

        //save transaction
        TransactionModel saveTM = saveTransaction(
                cpga, accountModel,trxPoin,trxRate,trxDate,int_random,null
        );


        //hit ke kafka untuk proses payment


        // isi respon setelahnya
        cpga.setCurrency(accountModel.getCurrency());
        cpga.setAmount(trxPoin);
        cpga.setConversionRate(trxRate);
        cpga.setTransactionDate(trxDate);
        cpga.setStatus("success");
        cpga.setTranCode(int_random.toString());
        cpga.setPaymentId(saveTM.getId());

        //update account
        accountModel.setAmount(finalAmount);
        accountModel.setTotalBuy(finalTotalBuy);
        accountModel.setAveragePerCoin(finalAvg);

        accountRepository.save(accountModel);


        //create proses untuk internal posting
        TransactionModel tmInt = new TransactionModel();
        tmInt.setOriginalCurrency(saveTM.getCurrency());
        tmInt.setOriginalAmount(saveTM.getAmount());
        tmInt.setCurrency(saveTM.getOriginalCurrency());
        tmInt.setAmount(saveTM.getAmount());
        tmInt.setConversionRate(saveTM.getConversionRate());
        tmInt.setTransactionDate(saveTM.getTransactionDate());
        tmInt.setReferenceNumber(saveTM.getReferenceNumber());
        //tembak ke 2
        AccountModel internalAccount = accountRepository.findAccountByAccountNumber(2);
        tmInt.setAccountModels(internalAccount);
        tmInt.setStatus("Success");
        tmInt.setTranCode(saveTM.getTranCode());
        tmInt.setTransactionType("Buy");
        if(tmInt.getTransactionType().equalsIgnoreCase("Sell"))
        {
            tmInt.setDescription("Sell crypto for transaction " + saveTM.getDescription());
            tmInt.setDebitCredit("Credit");
        }
        else {
            tmInt.setDescription("Buy crypto from" + saveTM.getDescription());
            tmInt.setDebitCredit("Debit");
        }

        transactionRepository.save(tmInt);

        AccountModel accountModelInternal = accountRepository.findAccountByAccountNumber(saveTM.getAccountModels().getAccountNumber());
        Double finalAmountInternal = accountModelInternal.getAmount() + saveTM.getAmount();
        Double finalTotalBuyInternal = accountModelInternal.getTotalBuy() + saveTM.getOriginalAmount();
        Double finalAvgInternal = 0.0;
        if (!finalAmountInternal.equals(0.0))
        {
            finalAvgInternal =finalTotalBuyInternal / finalAmountInternal;
        }
        accountModelInternal.setAmount(finalAmountInternal);
        accountModelInternal.setTotalBuy(finalTotalBuyInternal);
        accountModelInternal.setAveragePerCoin(finalAvgInternal);
        accountRepository.save(accountModelInternal);




        return cpga;
    }

    public TransactionModel saveTransaction(CryptoPayemntGatewayAuth cpga, AccountModel accountModel, Double trxPoin, Double trxRate, Date trxDate,
                                            Integer int_random, Integer cardPaymentId){
        TransactionModel tm = new TransactionModel();

        tm.setOriginalCurrency(cpga.getOriginalCurrency());
        tm.setOriginalAmount(cpga.getOriginalAmount());
        tm.setCurrency(accountModel.getCurrency());
        tm.setAmount(trxPoin);
        tm.setConversionRate(trxRate);
        tm.setTransactionDate(trxDate);
        tm.setReferenceNumber(cpga.getReferenceNumber());
        tm.setAccountModels(accountModel);
        tm.setStatus("Success");
        tm.setTranCode(int_random);
        tm.setTransactionType(cpga.getTransactionType());
        if(cpga.getTransactionType().equalsIgnoreCase("Sell"))
        {
            tm.setDescription("Sell crypto for transaction" + cpga.getMerchantName());
            tm.setDebitCredit("Credit");
        }
        else {
            tm.setDescription("Buy crypto from" + cpga.getMerchantName());
            tm.setDebitCredit("Debit");
            CardPaymentModel cardPaymentModel = cardPaymentRepository.getReferenceById(cpga.getCardPaymentId());
            tm.setCardPaymentModel(cardPaymentModel);
        }

        return transactionRepository.save(tm);
    }
}
