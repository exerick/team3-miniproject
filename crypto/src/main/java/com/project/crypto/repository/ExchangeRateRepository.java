package com.project.crypto.repository;

import com.project.crypto.model.ExchangeRateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRateModel, Integer> {
    ExchangeRateModel findExchangeRateByRateId (Integer id);
    ExchangeRateModel findExchangeRateByCurrency (String currency);
    List<ExchangeRateModel> findAll();
}
