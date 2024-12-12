package com.example.crypto.service;

import com.example.crypto.model.Transaction;
import com.example.crypto.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransactionService implements ITransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CurrencyConversionService currencyConversionService;
    private final SimpMessagingTemplate messagingTemplate;

    public TransactionService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public Transaction create(Transaction transaction) {
        double amount=currencyConversionService.convertEurToSepoliaEth(transaction.getAmount());
        String transactionToSend=transaction.getAmount()+","+transaction.getMerchantId()+","+transaction.getId()+","+amount;
        messagingTemplate.convertAndSend("/topic/string-data", transactionToSend);
        transaction.setAmount(amount);
        return transactionRepository.save(transaction);
    }
}
