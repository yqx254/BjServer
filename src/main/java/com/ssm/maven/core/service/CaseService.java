package com.ssm.maven.core.service;

import com.ssm.maven.core.entity.Case;
import com.ssm.maven.core.entity.Client;

import java.util.List;
import java.util.Map;

/**
 * @author fstar
 */
public interface CaseService {

    /**
     *  案件列表
     * @param map 查询条件
     * @return 列表
     */
    List<Case> getCase(Map<String, Object> map);

    /**
     * 案件总数
     * @param map 查询条件
     * @return 总数
     */
    Long getCaseCount(Map<String, Object> map);

    /**
     * 新建案件
     * @param newCase 新的案件
     */
    int addCase(Case newCase, List<Client> clients);

    /**
     * 更新案件信息
     * @param  oldCase 案件信息
     */
    void updateCase(Case oldCase, List<Client> clients);

    /**
     * 获取案件详情
     * @param id 案件id
     * @return 案件详情
     */
    Case caseDetail(String id);
    /**
     *  删除案件
     * @param caseId 案件编号
     */
    void deleteCase(Integer caseId);

    /**
     * 结案
     * @param caseId 案件编号
     * @param clear 是否将相关人员清理出利冲校验
     * @return 操作结果
     */
    int solveCase(Integer caseId, Integer clear);

    /**
     *  获取所有的委托人姓名信息
     * @param caseId 案件编号
     * @return
     */
    String getAllClientName(Integer caseId);

    /**
     * 获取所有的对方当事人姓名信息
     * @param caseId 案件编号
     * @return
     */
    String getAllOpponentName(Integer caseId);
}
