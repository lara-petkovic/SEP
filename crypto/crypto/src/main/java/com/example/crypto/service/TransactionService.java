package com.example.crypto.service;

import com.example.crypto.model.Merchant;
import com.example.crypto.model.Transaction;
import com.example.crypto.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionService implements ITransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private IMerchantService merchantService;
    @Autowired
    private CurrencyConversionService currencyConversionService;
    private final SimpMessagingTemplate messagingTemplate;

    public TransactionService(SimpMessagingTemplate messagingTemplate, IMerchantService merchantService) {
        this.messagingTemplate = messagingTemplate;
        this.merchantService=merchantService;
    }

    @Override
    public Transaction create(Transaction transaction) {
        double amount=currencyConversionService.convertEurToSepoliaEth(transaction.getAmount());
        Merchant m=merchantService.getByMerchantId(transaction.getMerchantId());
        if(m!=null) {
            String transactionToSend = transaction.getAmount() + "," + m.getWalletAddress() + "," + transaction.getId() + "," + amount;
            messagingTemplate.convertAndSend("/topic/string-data", transactionToSend);
            transaction.setAmount(amount);
            return transactionRepository.save(transaction);
        }else{
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8087/api/response";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String body = "{ \"responseUrl\": \"" + "fail" + "\", \"orderId\": \"" + transaction.getMerchantOrderId() + "\" }";

            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            return null;
        }
    }

    @Override
    public Transaction getById(Long id) {
        return transactionRepository.getTransactionById(id);
    }
}
