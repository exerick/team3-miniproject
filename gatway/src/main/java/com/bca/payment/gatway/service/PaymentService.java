package com.bca.payment.gatway.service;

import com.bca.payment.gatway.model.CardModel;
import com.bca.payment.gatway.model.MerchantModel;
import com.bca.payment.gatway.model.TransactionModel;
import com.bca.payment.gatway.odp.TransactionRequest;
import com.bca.payment.gatway.odp.TransactionResponse;
import kafka.CCPaymentGatewayAuth;
import com.bca.payment.gatway.repository.CardRepository;
import com.bca.payment.gatway.repository.MerchantRepository;
import com.bca.payment.gatway.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class PaymentService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    MerchantRepository merchantRepository;

    public PaymentService(TransactionRepository transactionRepository, CardRepository cardRepository, MerchantRepository merchantRepository, KafkaTemplate<Integer, CCPaymentGatewayAuth> authResponse) {
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
        this.merchantRepository = merchantRepository;
        this.authResponse = authResponse;
    }

    private KafkaTemplate<Integer, CCPaymentGatewayAuth> authResponse;

    public void authTransaction(CCPaymentGatewayAuth transactionAuth){
        System.out.println("all " + transactionAuth.toString());
        if (transactionAuth.getMessageType().equalsIgnoreCase("Request"))
        {
            System.out.println("req " + transactionAuth.toString());
            CCPaymentGatewayAuth authResult = trxAuthCC(transactionAuth);
            authResponse.send("CCPaymentAuth",transactionAuth.getTransactionId(),transactionAuth);
        }

    }
    
    private CCPaymentGatewayAuth trxAuthCC(CCPaymentGatewayAuth trq){
        //auth seq
        Boolean cardCheck = false;
        Boolean merCheck = false;
        Boolean limitCheck = false;
        System.out.println("cek ini  " + trq.toString());
        //init value
        Date trxDate = new Date();
        trq.setApprovalCode(0);
        trq.setMessageType("Response");
        trq.setDateTransaction(trxDate);


        //cek level kartu
        String cardAuth = cardCheck(trq.getCardNumber(),trq.getExpDate(), trq.getCvv(), trq.getAmount());
        if(cardAuth.equalsIgnoreCase("Approved"))
        {
            cardCheck = true;
        } else if (cardAuth.equalsIgnoreCase("Invalid Card Number")) {
            trq.setStatus("Invalid Card Number");
            return trq;
        } else {
            TransactionModel tm = addTransactionHistory(cardAuth,trq);
            trq.setApprovalCode(tm.getApprovalCode());
            trq.setStatus(tm.getStatus());
            trq.setTransactionId(tm.getTransactionId());
            return trq;
        }

        //validasi merchant
        String merchantAuth = merchantCheck(trq.getMerchantId());
        if(merchantAuth.equalsIgnoreCase("Approved")){
            merCheck = true;
        }
        else {
            TransactionModel tm = addTransactionHistory(cardAuth,trq);
            trq.setApprovalCode(tm.getApprovalCode());
            trq.setStatus(tm.getStatus());
            trq.setTransactionId(tm.getTransactionId());
            return trq;
        }

        //proses potong limit nasabah
        String limitAuth = updateLimitCard(trq.getCardNumber(), trq.getAmount());
        if (limitAuth.equalsIgnoreCase("Approved")){
            limitCheck = true;
        }else {
            TransactionModel tm = addTransactionHistory(limitAuth,trq);
            trq.setApprovalCode(tm.getApprovalCode());
            trq.setStatus(tm.getStatus());
            trq.setTransactionId(tm.getTransactionId());
            return trq;
        }

        if (!(limitCheck && cardCheck && merCheck)){
            TransactionModel tm = addTransactionHistory("Decline",trq);
            trq.setApprovalCode(tm.getApprovalCode());
            trq.setStatus(tm.getStatus());
            trq.setTransactionId(tm.getTransactionId());
            return trq;
        }
        TransactionModel tm = addTransactionHistory("Approved",trq);
        trq.setApprovalCode(tm.getApprovalCode());
        trq.setStatus(tm.getStatus());
        trq.setTransactionId(tm.getTransactionId());
        
        
        return trq;
    }
    
    private String cardCheck(String cardNo, String expDate, Integer cvv, Double amount){
        CardModel cardModel = cardRepository.findCardByCardNumber(cardNo);
        if (cardModel == null)
        {
            return "Invalid Card Number";
        }
        else if (!cardModel.getCardNumber().equalsIgnoreCase(cardNo)){
            return "Invalid Card Number";
        } else if (!cardModel.getCvv().equals(cvv)) {
            return "Invalid Card CVV";
        } else if (!cardModel.getExpDate().equalsIgnoreCase(expDate)) {
            return "Invalid Card Exp";
        } else if (cardModel.getAvailLimitCard() < amount) {
            return "insufficient limits";
        } else if (!cardModel.getStatus().equalsIgnoreCase("normal")) {
            return "Card Has Bad Status";
        } else {
            return "Approved";
        }
    }

    private String merchantCheck(Integer merchantNumber){
        try {
            MerchantModel merchantModel = merchantRepository.findMerchantByMerchantNumber(merchantNumber);
            if (!merchantModel.getMerchantNumber().equals(merchantNumber)){
                return "Invalid Merchant Number";
            } else if (!merchantModel.getStatus().equals("normal")) {
                return "Merchant Not Normal";
            }
            else {
                return "Approved";
            }
        } catch (Exception ex){
            System.out.println("merchant not found");
            return "Merchant Not Found";
        }

    }

    private TransactionModel addTransactionHistory(String status, CCPaymentGatewayAuth transactionRequest){
        //prepare input
        Date trxDate = transactionRequest.getDateTransaction();
        Timestamp trxTime = new Timestamp(System.currentTimeMillis());

        Integer min = 100;
        Integer max = 999999;
        Integer random_int = (int) Math.floor(Math.random() * (max - min + 1) + min);

        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setDate(trxDate);
        transactionModel.setTimestamp(trxTime);

        transactionModel.setStatus(status);
        transactionModel.setNominalTransaction(transactionRequest.getAmount());
        transactionModel.setDebetCredit(true);
        if (status.equalsIgnoreCase("Approved"))
        {
            transactionModel.setApprovalCode(random_int);
        }
        else {
            transactionModel.setApprovalCode(0);
        }


        CardModel cardModel = cardRepository.findCardByCardNumber(transactionRequest.getCardNumber());
        transactionModel.setCardModel(cardModel);

        MerchantModel merchantModel = merchantRepository.findMerchantByMerchantNumber(transactionRequest.getMerchantId());
        transactionModel.setMerchantModel(merchantModel);
        transactionModel.setDescription(merchantModel.getName());
        transactionModel.setRefTransaction(transactionRequest.getRefTransaction());

        return transactionRepository.save(transactionModel);
    }

    private String updateLimitCard(String cardNo, Double amount){
        CardModel cardModel = cardRepository.findCardByCardNumber(cardNo);
        if(!cardModel.getCardNumber().equalsIgnoreCase(cardNo)){
            return "Invalid Card Number";
        }
        else {
            Double newBalance = cardModel.getBalance() + amount;
            Double newAvailLimit = cardModel.getAvailLimitCard() - amount;
            cardModel.setAvailLimitCard(newAvailLimit);
            cardModel.setBalance(newBalance);
            cardRepository.save(cardModel);
            return "Approved";
        }
    }
    
}
