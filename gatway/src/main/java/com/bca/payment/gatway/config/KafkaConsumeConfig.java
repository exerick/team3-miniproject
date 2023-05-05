package com.bca.payment.gatway.config;

import com.bca.payment.gatway.service.PaymentService;
import kafka.CCPaymentGatewayAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@EnableKafka
public class KafkaConsumeConfig {
    @Autowired
    PaymentService paymentService;

    @KafkaListener(id = "authRequest", topics = "CCPaymentAuth",groupId = "payment")
    public void onEvent(CCPaymentGatewayAuth ccPaymentGatewayAuth) {
        paymentService.authTransaction(ccPaymentGatewayAuth);

    }

}
