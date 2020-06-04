package com.ssm.maven.core.service;

import com.ssm.maven.core.entity.Case;

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
    int addCase(Case newCase);

    /**
     * 更新案件信息
     * @param  oldCase 案件信息
     */
    void updateCase(Case oldCase);

    /**
     *  删除案件
     * @param caseId 案件编号
     */
    void deleteCase(Integer caseId);

    /**
     * 结案
     * @param caseId 案件编号
     * @return 操作结果
     */
    int solveCase(Integer caseId);
}
