package com.project.crypto.repository;

import com.project.crypto.model.CardPaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardPaymentRepository extends JpaRepository<CardPaymentModel,Integer> {
    CardPaymentModel findCardPaymentByPaymentId (Integer cardPymId);
    List<CardPaymentModel> findAll();

}
