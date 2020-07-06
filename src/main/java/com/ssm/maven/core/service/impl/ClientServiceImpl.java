package com.ssm.maven.core.service.impl;

import com.ssm.maven.core.dao.ClientDao;
import com.ssm.maven.core.entity.Client;
import com.ssm.maven.core.service.ClientService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fstar
 */
@Service("ClientService")
public class ClientServiceImpl implements ClientService {
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
    public int addClient(Client client) {
        if(client.getClientName() == null || "".equals(client.getClientName())){
            return 0;
        }
        return clientDao.addClient(client);
    }

    @Override
    public int deleteClients(int id) {
        return clientDao.deleteClients(id);
    }

    @Override
    public int deleteByCase(int caseId) {
        return clientDao.deleteByCase(caseId);
    }

    @Override
    public int solveClient(int caseId) {
        return clientDao.solveClient(caseId);
    }

    @Override
    public List<Integer> findCase(String clientName) {
        return clientDao.findCase(clientName);
    }

    @Override
    public List<Integer> findCaseOp(String clientName) {
        return clientDao.findCaseOp(clientName);
    }

    @Override
    public List<Integer> findCaseAll(String clientName){
        return clientDao.findCaseAll(clientName);
    }

    @Override
    public List<Client> clientsByCase(String caseId) {
        return clientDao.clientsByCase(caseId);
    }

    @Override
    public String getClientsName(String caseId) {
        List<Client>  clients= clientDao.getClientByCase(Integer.parseInt(caseId));
        StringBuilder stb = new StringBuilder();
        for(Client c : clients){
            stb.append(c.getClientName());
            stb.append(" ");
        }
        return stb.toString();
    }

    @Override
    public String getOpponentsName(String caseId) {
        List<Client>  opponents= clientDao.getOpponentByCase(Integer.parseInt(caseId));
        StringBuilder stb = new StringBuilder();
        for(Client c : opponents){
            stb.append(c.getClientName());
            stb.append(" ");
        }
        return stb.toString();
    }
}
