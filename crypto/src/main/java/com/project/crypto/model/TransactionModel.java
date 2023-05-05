package com.project.crypto.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "transaction")
public class TransactionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "description")
    private String description;
    @Column(name = "debit_credit")
    private String debitCredit;
    @Column(name = "original_currency")
    private String originalCurrency;
    @Column(name = "original_amount")
    private Double originalAmount;
    @Column(name = "currency")
    private String currency;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "conversion_rate")
    private Double conversionRate;
    @Column(name = "transaction_date")
    private Date transactionDate;
    @Column(name = "reference_number")
    private String referenceNumber;
    @Column(name = "transaction_type")
    private String transactionType;
    @Column(name = "status")
    private String status;
    @Column(name = "tran_code")
    private Integer tranCode;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "accountNumber")
    private AccountModel accountModels;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "cardPaymentId")
    private CardPaymentModel cardPaymentModel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDebitCredit() {
        return debitCredit;
    }

    public void setDebitCredit(String debitCredit) {
        this.debitCredit = debitCredit;
    }

    public String getOriginalCurrency() {
        return originalCurrency;
    }

    public void setOriginalCurrency(String originalCurrency) {
        this.originalCurrency = originalCurrency;
    }

    public Double getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(Double originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(Double conversionRate) {
        this.conversionRate = conversionRate;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AccountModel getAccountModels() {
        return accountModels;
    }

    public void setAccountModels(AccountModel accountModels) {
        this.accountModels = accountModels;
    }

    public Integer getTranCode() {
        return tranCode;
    }

    public void setTranCode(Integer tranCode) {
        this.tranCode = tranCode;
    }

    public CardPaymentModel getCardPaymentModel() {
        return cardPaymentModel;
    }

    public void setCardPaymentModel(CardPaymentModel cardPaymentModel) {
        this.cardPaymentModel = cardPaymentModel;
    }
}
