package com.example.sep.services;

import com.example.sep.dtos.ClientAuthenticationDataDto;
import com.example.sep.dtos.ClientSubscriptionDto;
import com.example.sep.dtos.NewTransactionDto;
import com.example.sep.models.Client;
import com.example.sep.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.security.SecureRandom;

@Service
public class ClientService implements IClientService{
    @Autowired
    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository){
        this.clientRepository=clientRepository;
    }
    @Override
    public ClientAuthenticationDataDto create(Client client, String address) {
        String merchantId=generateRandomString();
        client.setMerchantId(merchantId);
        client.setMerchantPass(generateRandomString());
        this.clientRepository.save(client);
        SendCredentials(client,address);
        return new ClientAuthenticationDataDto(client.getMerchantId(),client.getMerchantPass());
    }

    private void SendCredentials(Client client, String address){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8086/api/psp-subscription/credentials";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Location", address);

        // Create the JSON body
        String body = "{ \"merchantId\" : \"" + client.getMerchantId() + "\", \"merchantPass\" : \"" + client.getMerchantPass() + "\" }";

        // Set up the HTTP entity with headers and body
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        // Send the POST request
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }
    @Override
    public ClientSubscriptionDto getSubscription(NewTransactionDto newTransactionDto) {
        Client client=clientRepository.getClientByMerchantId(newTransactionDto.merchantId);
        if(client!=null && client.getMerchantPass().equals(newTransactionDto.getMerchantPass())){
            return new ClientSubscriptionDto(client.getSubscription(),client.getMerchantId(),newTransactionDto.getMerchantOrderId());
        }
        return null;
    }


    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomString() {
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

}
