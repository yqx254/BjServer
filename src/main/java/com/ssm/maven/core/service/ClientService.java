package com.ssm.maven.core.service;

import com.ssm.maven.core.entity.Client;

import java.util.List;

public interface ClientService {

    List<Client> getClients(int caseId);

    Client conflictCheckClient(String clientName);

    Client conflictCheckOpponent(String clientName);

    int addClients(Client client);

    int deleteClients(int id);
}
