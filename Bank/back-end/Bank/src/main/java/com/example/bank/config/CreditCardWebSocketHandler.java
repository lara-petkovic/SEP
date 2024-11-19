package com.example.bank.config;
import com.example.bank.domain.model.Account;
import com.example.bank.domain.model.PaymentRequest;
import com.example.bank.service.AccountService;
import com.example.bank.service.PaymentRequestService;
import com.example.bank.service.dto.PaymentDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class CreditCardWebSocketHandler extends TextWebSocketHandler {
    private WebSocketSession frontEndSession;
    @Autowired
    private PaymentRequestService paymentRequestService;
    @Autowired
    private AccountService accountService;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("New WebSocket connection: " + session.getId());
        frontEndSession = session;
        frontEndSession.sendMessage(new TextMessage("Welcome to the Credit Card WebSocket!"));
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message: " + message.getPayload());
        ObjectMapper objectMapper = new ObjectMapper();
        PaymentDto paymentDto = objectMapper.readValue(message.getPayload(), PaymentDto.class);
        PaymentRequest paymentRequest = paymentRequestService.getPaymentRequest(paymentDto.PaymentRequestId);
        Account merchantAccount = accountService.getMerchantAccount(paymentRequest);
        Account issuerAccount = accountService.getIssuerAccount(paymentDto);
        if(merchantAccount!=null){
            if(issuerAccount!=null){
                if(issuerAccount.getBalance()>=paymentRequest.getAmount()){
                    issuerAccount.setBalance(issuerAccount.getBalance()-paymentRequest.getAmount());
                    merchantAccount.setBalance(merchantAccount.getBalance()+paymentRequest.getAmount());
                    accountService.save(issuerAccount);
                    accountService.save(merchantAccount);
                }
                else{
                    //not enough money
                }
            }
            else{
                //call issuers bank via pcc
            }
        }
    }
    public void openCreditCardForm(int paymentId, double amount) throws Exception{
        frontEndSession.sendMessage(new TextMessage(paymentId + "," + amount));
    }















//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        try {
//            Map<String, Object> data = objectMapper.readValue(message.getPayload(), Map.class);
//            String paymentOption = (String) data.get("name");
//            Integer orderid = (Integer) data.get("orderid");
//            String merchantid = (String) data.get("merchantid");
//
//            TransactionService transactionService=new TransactionService(this.transactionRepository);
//            Transaction transaction=transactionService.GetTransactionByMerchantIdAndMerchantOrderId(merchantid,orderid);
//            System.out.println("Received payment opotion: " + paymentOption + ", ID: " + orderid+merchantid);
//
//            RestTemplate restTemplate = new RestTemplate();
//            String url = "http://localhost:8086/api/payments";
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.set("Payment", paymentOption);
//
//            // Create the JSON body
//            String body = "{ \"MerchantId\" : \"" + transaction.getMerchantId() + "\", " +
//                    "\"MerchantPassword\" : \"" + transaction.getMerchantPass() + "\", " +
//                    "\"Amount\" : \"" + transaction.getAmount() + "\", " +
//                    "\"MerchantOrderId\" : \"" + transaction.getOrderId() + "\", " +
//                    "\"MerchantTimestamp\" : \"" + transaction.getTimestamp() + "\", " +
//                    "\"SuccessUrl\" : \"http://localhost:4201/success\", " +
//                    "\"FailedUrl\" : \"http://localhost:4201/fail\", " +
//                    "\"Error\" : \"http://localhost:4201/error\" }";
//
//            // Set up the HTTP entity with headers and body
//            HttpEntity<String> entity = new HttpEntity<>(body, headers);
//
//            // Send the POST request
//            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//
//
//        } catch (Exception e) {
//            System.out.println("Parrsing error: " + e.getMessage());
//            session.sendMessage(new TextMessage("Invalid format!"));
//        }
//    }

}
