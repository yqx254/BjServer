package com.ssm.maven.core.dao;

import com.ssm.maven.core.entity.Client;

import java.util.List;

public interface ClientDao {
    /**
     * 获取案件相关的当事人
     * @param caseId 案件ID
     * @return 当事人List
     */
    List<Client> getClients(int caseId);

    /**
     * 委托人检查
     * @param clientName 录入的委托人名称，在对方当事人中进行查询
     * @return 有冲突的对方当事人信息
     */
    Client conflictCheckClient(String clientName);

    /**
     * 对方当事人检查
     * @param clientName 录入的对方当事人名称，在委托人信息中进行查询
     * @return 有冲突的对方当事人信息
     */
    Client conflictCheckOpponent(String clientName);

    /**
     * 新增委托人
     * @param client 委托人信息
     * @return 操作结果
     */
    int addClient(Client client);

    /**
     * 删除委托人
     * @param id 委托人id
     * @return 操作结果
     */
    int deleteClients(int id);

    /**
     *  查询相关的案件id
     * @param clientName 委托人姓名
     * @return id list
     */
    List<Integer> findCase(String clientName);

    /**
     *  查询对方当事人相关的案件id
     * @param clientName 对方当事人姓名
     * @return id list
     */
    List<Integer>findCaseOp(String clientName);
}
