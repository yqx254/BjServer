package com.ssm.maven.core.service.impl;

import com.ssm.maven.core.dao.ClientDao;
import com.ssm.maven.core.entity.Client;
import com.ssm.maven.core.service.ClientService;

import javax.annotation.Resource;
import java.util.List;

public class ClientServiceimpl implements ClientService {
    @Resource
    private ClientDao clientDao;

    @Override
    public List<Client> getClients(int caseId) {
        return clientDao.getClients(caseId);
    }

    @Override
    public Client conflictCheckClient(String clientName) {
        return clientDao.conflictCheckClient(clientName);
    }

    @Override
    public Client conflictCheckOpponent(String clientName) {
        return clientDao.conflictCheckOpponent(clientName);
    }

    @Override
    public int addClients(Client client) {
        return clientDao.addClients(client);
    }

    @Override
    public int deleteClients(int id) {
        return clientDao.deleteClients(id);
    }
}
