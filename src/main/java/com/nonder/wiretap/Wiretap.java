package com.nonder.wiretap;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class Wiretap extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("rabbitmq:amq.direct?queue=UKBank&routingKey=UKBankKey&autoDelete=false")
                .log(LoggingLevel.INFO, "Read from UK Bank: ${body}")
                .wireTap("direct:tab")
                .to("activemq:USBank")
                .log(LoggingLevel.INFO, "Sent to US Bank");

        from("direct:tab")
                .unmarshal().json(JsonLibrary.Jackson, TransactionDTO.class)
                .process(this::detectFraud)
                .marshal().json(JsonLibrary.Jackson, TransactionDTO.class)
                .to("file:///Users/nihat.onder/Desktop/wiretap/src/main/resources?fileName=fraudDetection.json")
                .log(LoggingLevel.INFO, "Fraud file created!");
    }

    private void detectFraud(Exchange exchange) throws InterruptedException {
        TransactionDTO transactionDTO = exchange.getMessage().getBody(TransactionDTO.class);
        Thread.sleep(10000);

        if(transactionDTO.getAmount() > 10000) {
            transactionDTO.setFraud("Suspicious!");
        } else {
            transactionDTO.setFraud("Ok!");
        }
    }
}
