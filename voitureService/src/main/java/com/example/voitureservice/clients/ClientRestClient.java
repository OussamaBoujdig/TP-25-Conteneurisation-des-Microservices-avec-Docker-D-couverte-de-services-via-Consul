package com.example.voitureservice.clients;

import com.example.voitureservice.entities.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "client-service")
public interface ClientRestClient {

    @GetMapping("/api/clients/{id}")
    Client findClientById(@PathVariable("id") Long id);
}

