package com.example.sep.controllers;

import com.example.sep.configuration.TradeWebSocketHandler;
import com.example.sep.dtos.ClientSubscriptionDto;
import com.example.sep.dtos.NewTransactionDto;
import com.example.sep.models.Transaction;
import com.example.sep.services.IClientService;
import com.example.sep.services.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="api/transaction")

public class TransactionController {
    private final TradeWebSocketHandler tradeWebSocketHandler;
    @Autowired
    private IClientService clientService;
    @Autowired
    private ITransactionService transactionService;

    @Autowired
    public TransactionController(TradeWebSocketHandler tradeWebSocketHandler, IClientService clientService, ITransactionService transactionService) {
        this.tradeWebSocketHandler = tradeWebSocketHandler;
        this.clientService=clientService;
        this.transactionService=transactionService;
    }
    @PostMapping
    public NewTransactionDto CreateTransaction(@RequestBody NewTransactionDto transaction) throws Exception {
        ClientSubscriptionDto clientSubscriptionDto=clientService.getSubscription(transaction);
        if(clientSubscriptionDto!=null) {
            tradeWebSocketHandler.broadcastMessage("New transaction created: " + clientSubscriptionDto.toString());
            this.transactionService.SaveTransaction(new Transaction(transaction));
        }
        return  transaction;
    }


}
