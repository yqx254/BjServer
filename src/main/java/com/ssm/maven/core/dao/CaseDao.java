package com.ssm.maven.core.dao;

import com.ssm.maven.core.entity.Case;

import java.util.List;
import java.util.Map;

/**
 * @author fstar
 */
public interface CaseDao {
    /**
     * 根据条件获取案件信息
     * @param map 查询条件
     * @return 案件
     */
    List<Case> getCase(Map<String, Object> map);

    /**
     *  根据条件获取案件数量
     * @param map 查询条件
     * @return 案件
     */
    Long getCaseCount(Map<String, Object> map);

    /**
     * 获取案件详情
     * @param id 案件id
     * @return 案件
     */
    Case caseDetail(String id);
    /**
     * 新增案件
     * @param newCase 案件实体
     * @return 插入结果
     */
    int addCase(Case newCase);
    /**
     *  更新案件信息
     * @param newCase 案件实体
     */
    void updateCase(Case newCase);

    /**
     *  结案
     * @param id 案件id
     * @return 结果
     */
    int solveCase(Integer id);

    /**
     *  删除
     * @param id 案件ID
     */
    void deleteCase(Integer id);
}
