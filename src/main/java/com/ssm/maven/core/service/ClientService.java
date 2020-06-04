package com.ssm.maven.core.service;

import com.ssm.maven.core.entity.Client;

import java.util.List;

/**
 * @author fstar
 */
public interface ClientService {

    List<Client> getClients(int caseId);

    Client conflictCheckClient(String clientName);

    Client conflictCheckOpponent(String clientName);

    int addClient(Client client);

    int deleteClients(int id);

    List<Integer> findCase(String clientName);

    List<Integer> findCaseOp(String clientName);
}
