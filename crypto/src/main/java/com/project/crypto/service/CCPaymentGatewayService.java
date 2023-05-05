package com.project.crypto.service;

import com.project.crypto.model.AccountModel;
import com.project.crypto.model.CardPaymentModel;
import com.project.crypto.model.TransactionModel;
import kafka.CCPaymentGatewayAuth;
import com.project.crypto.repository.AccountRepository;
import com.project.crypto.repository.CardPaymentRepository;
import com.project.crypto.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CCPaymentGatewayService {
    private KafkaTemplate<Integer, Object> authPayment;

    @Autowired
    CardPaymentRepository cardPaymentRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;

    public CCPaymentGatewayService(KafkaTemplate<Integer, Object> authPayment, CardPaymentRepository cardPaymentRepository, TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.authPayment = authPayment;
        this.cardPaymentRepository = cardPaymentRepository;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public String requestPayment(Integer paymentId, Double amount, Integer ref){
        String status ="Fail";
        CCPaymentGatewayAuth msgAuth = new CCPaymentGatewayAuth();

        CardPaymentModel cpm = cardPaymentRepository.findCardPaymentByPaymentId(paymentId);
        if (cpm.getPaymentId().equals(null)){
            return "Payment ID Not Found";
        }

        msgAuth.setMessageType("Request");
        msgAuth.setCardNumber(cpm.getCardNumber());
        msgAuth.setCvv(cpm.getCvv());
        msgAuth.setExpDate(cpm.getExpDate());
        msgAuth.setAmount(amount);
        msgAuth.setMerchantId(1);
        msgAuth.setRefTransaction(ref);

        /*
        Message<CCPaymentGatewayAuth> message = MessageBuilder
                .withPayload(msgAuth)
                .setHeader(KafkaHeaders.TOPIC, "CCPayment")
                .build();

        authPayment.send(message);*/

        authPayment.send("CCPaymentAuth",msgAuth);
        status = "Success";

        return status;
    }

    public String responsePayment(CCPaymentGatewayAuth ccPaymentGatewayAuth){
        //find trx
        TransactionModel transactionModel = transactionRepository.findTransactionById(ccPaymentGatewayAuth.getRefTransaction());
        if (transactionModel == null)
        {
            return "transaction not found";
        }

        if(ccPaymentGatewayAuth.getStatus().equalsIgnoreCase("Approved")){
            transactionModel.setStatus("Success");
            transactionModel.setTranCode(ccPaymentGatewayAuth.getApprovalCode());
            AccountModel accountModel = accountRepository.findAccountByAccountNumber(transactionModel.getAccountModels().getAccountNumber());
            Double finalAmount = accountModel.getAmount() + transactionModel.getAmount();
            Double finalTotalBuy = accountModel.getTotalBuy() + transactionModel.getOriginalAmount();
            Double finalAvg = 0.0;
            if (!finalAmount.equals(0.0))
            {
                finalAvg =finalTotalBuy / finalAmount;
            }
            accountModel.setAmount(finalAmount);
            accountModel.setTotalBuy(finalTotalBuy);
            accountModel.setAveragePerCoin(finalAvg);
            System.out.println(finalAmount +" " + finalTotalBuy +" "+finalAvg + " " + accountModel.getAccountNumber());
            System.out.println("all " + accountModel.toString());
            accountRepository.save(accountModel);
        }
        else {
            transactionModel.setStatus(ccPaymentGatewayAuth.getStatus());
        }

        transactionRepository.save(transactionModel);


        //create proses untuk internal posting
        TransactionModel tmInt = new TransactionModel();
        tmInt.setOriginalCurrency(transactionModel.getCurrency());
        tmInt.setOriginalAmount(transactionModel.getAmount());
        tmInt.setCurrency(transactionModel.getOriginalCurrency());
        tmInt.setAmount(transactionModel.getAmount());
        tmInt.setConversionRate(transactionModel.getConversionRate());
        tmInt.setTransactionDate(transactionModel.getTransactionDate());
        tmInt.setReferenceNumber(transactionModel.getReferenceNumber());
        //tembak ke 2
        AccountModel internalAccount = accountRepository.findAccountByAccountNumber(2);
        tmInt.setAccountModels(internalAccount);
        tmInt.setStatus("Success");
        tmInt.setTranCode(transactionModel.getTranCode());
        tmInt.setTransactionType("Sell");
        if(tmInt.getTransactionType().equalsIgnoreCase("Sell"))
        {
            tmInt.setDescription("Sell crypto for transaction " + transactionModel.getDescription());
            tmInt.setDebitCredit("Credit");
        }
        else {
            tmInt.setDescription("Buy crypto from" + transactionModel.getDescription());
            tmInt.setDebitCredit("Debit");
        }

        transactionRepository.save(tmInt);

        AccountModel accountModelInternal = accountRepository.findAccountByAccountNumber(2);
        Double finalAmountInternal = accountModelInternal.getAmount() - transactionModel.getAmount();
        Double finalTotalBuyInternal = accountModelInternal.getTotalBuy() - transactionModel.getOriginalAmount();
        Double finalAvgInternal = 0.0;
        if (!finalAmountInternal.equals(0.0))
        {
            finalAvgInternal =finalTotalBuyInternal / finalAmountInternal;
        }
        accountModelInternal.setAmount(finalAmountInternal);
        accountModelInternal.setTotalBuy(finalTotalBuyInternal);
        accountModelInternal.setAveragePerCoin(finalAvgInternal);
        accountRepository.save(accountModelInternal);

        return "ok";
    }

}
