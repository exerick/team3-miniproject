package com.project.crypto.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "customer")
public class CustomerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "birthday")
    private Date birthday;
    @Column(name = "gender")
    private Boolean gender;
    @Column(name = "phoneNumber")
    private String phoneNumber;

    @JsonBackReference
    @OneToMany(mappedBy = "accountNumber" )
    private List<AccountModel> accountModels;
    @JsonBackReference
    @OneToMany(mappedBy = "paymentId")
    private List<CardPaymentModel> cardPaymentModels;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<AccountModel> getAccountModels() {
        return accountModels;
    }

    public void setAccountModels(List<AccountModel> accountModels) {
        this.accountModels = accountModels;
    }

    public List<CardPaymentModel> getCardPaymentModels() {
        return cardPaymentModels;
    }

    public void setCardPaymentModels(List<CardPaymentModel> cardPaymentModels) {
        this.cardPaymentModels = cardPaymentModels;
    }


}
