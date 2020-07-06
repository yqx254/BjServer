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

    int deleteByCase(int caseId);

    int solveClient(int caseId);

    List<Integer> findCase(String clientName);

    List<Integer> findCaseOp(String clientName);

    List<Integer> findCaseAll(String clientName);

    List<Client> clientsByCase(String caseId);

    String getClientsName(String caseId);

    String getOpponentsName(String caseId);
}
