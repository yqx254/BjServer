package com.ssm.maven.core.service.impl;

import com.ssm.maven.core.dao.CaseDao;
import com.ssm.maven.core.entity.Case;
import com.ssm.maven.core.service.CaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author fstar
 */
@Service("CaseService")
public class CaseServiceImpl implements CaseService {

    @Resource
    private CaseDao caseDao;
    /**
     * 案件列表
     *
     * @param map 查询条件
     * @return 列表
     */
    @Override
    public List<Case> getCase(Map<String, Object> map) {
        return caseDao.getCase(map);
    }

    /**
     * 案件总数
     *
     * @param map 查询条件
     * @return 总数
     */
    @Override
    public Long getCaseCount(Map<String, Object> map) {
        return caseDao.getCaseCount(map);
    }

    /**
     * 新建案件
     *
     * @param newCase 新的案件
     */
    @Override
    public int addCase(Case newCase) {
        return caseDao.addCase(newCase);
    }

    /**
     * 更新案件信息
     *
     * @param oldCase 案件信息
     */
    @Override
    public void updateCase(Case oldCase) {
        caseDao.updateCase(oldCase);
    }

    @Override
    public Case caseDetail(String id) {
        return caseDao.caseDetail(id);
    }

    /**
     * 删除案件
     *
     * @param caseId 案件编号
     */
    @Override
    public void deleteCase(Integer caseId) {
        caseDao.deleteCase(caseId);
    }

    /**
     * 结案
     *
     * @param caseId 案件编号
     */
    @Override
    public int solveCase(Integer caseId) {
        return caseDao.solveCase(caseId);
    }


}
