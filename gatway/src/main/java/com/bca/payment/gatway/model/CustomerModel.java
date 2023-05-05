package com.bca.payment.gatway.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "customer")
public class CustomerModel {
    @Id
    @Column(name = "customer_number")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer customerNumber;
    @Column(name = "name")
    private String name;
    @Column(name ="phone_number")
    private String phoneNumber;
    @Column(name ="email")
    private String email;
    @Column(name ="address")
    private String address;
    @Column(name ="status")
    private String status;
    @Column(name ="birthDate")
    private Date birthDate;

    @JsonBackReference
    @OneToMany(mappedBy = "cardNumber" )
    private List<CardModel> cardModels;

    public Integer getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Integer customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public List<CardModel> getCardModels() {
        return cardModels;
    }

    public void setCardModels(List<CardModel> cardModels) {
        this.cardModels = cardModels;
    }
}
