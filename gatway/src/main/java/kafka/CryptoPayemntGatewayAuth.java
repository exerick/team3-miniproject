package kafka;

import java.util.Date;

public class CryptoPayemntGatewayAuth {
    private String messageType;
    private String merchantName;
    private String originalCurrency;
    private Double originalAmount;
    private String transactionType;
    private String referenceNumber;
    private Integer accountNumber;
    private Integer cardPaymentId;
    private String currency;
    private Double amount;
    private Double conversionRate;
    private Date transactionDate;
    private String status;
    private String tranCode;
    private Integer paymentId;

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
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

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Integer getCardPaymentId() {
        return cardPaymentId;
    }

    public void setCardPaymentId(Integer cardPaymentId) {
        this.cardPaymentId = cardPaymentId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTranCode() {
        return tranCode;
    }

    public void setTranCode(String tranCode) {
        this.tranCode = tranCode;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return "CryptoPayemntGatewayAuth{" +
                "messageType='" + messageType + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", originalCurrency='" + originalCurrency + '\'' +
                ", originalAmount=" + originalAmount +
                ", transactionType='" + transactionType + '\'' +
                ", referenceNumber='" + referenceNumber + '\'' +
                ", accountNumber=" + accountNumber +
                ", cardPaymentId=" + cardPaymentId +
                ", currency='" + currency + '\'' +
                ", amount=" + amount +
                ", conversionRate=" + conversionRate +
                ", transactionDate=" + transactionDate +
                ", status='" + status + '\'' +
                ", tranCode='" + tranCode + '\'' +
                ", paymentId=" + paymentId +
                '}';
    }
}
