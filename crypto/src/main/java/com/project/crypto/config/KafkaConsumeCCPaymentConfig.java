package com.project.crypto.config;

import kafka.CCPaymentGatewayAuth;
import com.project.crypto.service.CCPaymentGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@EnableKafka
public class KafkaConsumeCCPaymentConfig {
    @Autowired
    CCPaymentGatewayService ccPaymentGatewayService;

    @KafkaListener(id = "authRequest", topics = "CCPaymentAuth",groupId = "paymentCyrpto")
    public void onEvent(CCPaymentGatewayAuth ccPaymentGatewayAuth) {
        System.out.println("data : " + ccPaymentGatewayAuth.toString());
        if(ccPaymentGatewayAuth.getMessageType().equalsIgnoreCase("Response")){
            ccPaymentGatewayService.responsePayment(ccPaymentGatewayAuth);
            System.out.println("listen final " + ccPaymentGatewayAuth.getStatus());
        }

    }
}
