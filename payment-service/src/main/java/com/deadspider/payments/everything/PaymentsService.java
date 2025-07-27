package com.deadspider.payments.everything;


import com.deadspider.payments.client.OrderClient;
import com.deadspider.payments.client.ShmuckClient;
import com.deadspider.payments.models.ApiResponse;
import com.deadspider.payments.models.OrderResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentsService {


    private KafkaTemplate<String, String> template;
    private OrderClient orderClient;
    private ShmuckClient shmuckBank;
    private static final String ordersTopic = "com.deadspider.orders";

    public void intiPayment(Long orderId) throws JsonProcessingException {
        template.send(ordersTopic, UUID.randomUUID().toString(), new ObjectMapper().writeValueAsString(
                PaymentResponse.builder()
                        .orderId(orderId)
                        .status(PaymentStatus.PENDING)
                        .build()
        ));
    }

    public PaymentResponse pay(PaymentRequest request) throws JsonProcessingException {
        OrderResponse order = orderClient.byId(request.getOrderId());

        TransferRequest req = TransferRequest.builder()
                .amount(order.getAmount()*order.getQuantity())
                .accno(request.getAccno())
                .type("DEBIT")
                .narration("payment against orderId: " + order.getId())
                .build();

        ApiResponse transactionResponse = shmuckBank.makePayment(req);

        PaymentResponse payres = PaymentResponse.builder()
                .orderId(request.getOrderId())
                .build();
        if(transactionResponse.getCode() == HttpStatus.OK.value()) {
            passPayment(request.getOrderId(), transactionResponse.getMessage());
            payres.setStatus(PaymentStatus.COMPLETED);
        }else {
            failPayment(request.getOrderId());
            payres.setStatus(PaymentStatus.FAILED);
        }
        return payres;
    }

    public void failPayment(Long orderId) throws JsonProcessingException {
        template.send(ordersTopic, UUID.randomUUID().toString(), new ObjectMapper().writeValueAsString(
                PaymentResponse.builder()
                        .orderId(orderId)
                        .status(PaymentStatus.FAILED)
                        .build()
        ));
    }

    public void passPayment(Long orderId, String ref) throws JsonProcessingException {
        template.send(ordersTopic, ref , new ObjectMapper().writeValueAsString(
                PaymentResponse.builder()
                        .orderId(orderId)
                        .status(PaymentStatus.COMPLETED)
                        .build()
        ));
    }

}
