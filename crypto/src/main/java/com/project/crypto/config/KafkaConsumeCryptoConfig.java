package com.project.crypto.config;

import com.project.crypto.service.CryptoPaymentService;
import kafka.CCPaymentGatewayAuth;
import kafka.CryptoPayemntGatewayAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@EnableKafka
public class KafkaConsumeCryptoConfig {
    @Autowired
    CryptoPaymentService cryptoPaymentService;

    @KafkaListener(id = "cryptoAuthRequest", topics = "CryptoPaymentAuth",groupId = "cryptoPaymentGw")
    public void onEvent(CryptoPayemntGatewayAuth cryptoPayemntGatewayAuth) {
        System.out.println("tes cpgwa " + cryptoPayemntGatewayAuth.toString());
        if (cryptoPayemntGatewayAuth.getMessageType().equalsIgnoreCase("Request")){
            String result = cryptoPaymentService.consumePaymentGateway(cryptoPayemntGatewayAuth);
            System.out.println("hasil consume " + result);
        }
    }
}
