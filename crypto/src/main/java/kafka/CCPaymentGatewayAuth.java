package kafka;

import java.sql.Date;

public class CCPaymentGatewayAuth {
    private String messageType;
    private String cardNumber;
    private Integer cvv;
    private String expDate;
    private Double amount;
    private Integer merchantId;
    private Integer refTransaction;
    private Date dateTransaction;
    private String status;
    private Integer approvalCode;
    private Integer transactionId;


    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getRefTransaction() {
        return refTransaction;
    }

    public void setRefTransaction(Integer refTransaction) {
        this.refTransaction = refTransaction;
    }

    public Date getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(Date dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(Integer approvalCode) {
        this.approvalCode = approvalCode;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "CCPaymentGatewayAuth{" +
                "messageType='" + messageType + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", cvv=" + cvv +
                ", expDate='" + expDate + '\'' +
                ", amount=" + amount +
                ", merchantId=" + merchantId +
                ", refTransaction=" + refTransaction +
                ", dateTransaction=" + dateTransaction +
                ", status='" + status + '\'' +
                ", approvalCode=" + approvalCode +
                ", transactionId=" + transactionId +
                '}';
    }
}
