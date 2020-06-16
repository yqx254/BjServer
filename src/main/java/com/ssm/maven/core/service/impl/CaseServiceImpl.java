package com.ssm.maven.core.service.impl;

import com.ssm.maven.core.dao.CaseDao;
import com.ssm.maven.core.entity.Case;
import com.ssm.maven.core.entity.Client;
import com.ssm.maven.core.service.CaseService;
import com.ssm.maven.core.service.ClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author fstar
 */
@Service("CaseService")
public class CaseServiceImpl implements CaseService {

    @Resource
    private CaseDao caseDao;

    @Resource
    private ClientService clientService;
    /**
     * 案件列表
     *
     * @param map 查询条件
     * @return 列表
     */
    @Override
    public List<Case> getCase(Map<String, Object> map) {
        List<Case> cases = caseDao.getCase(map);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Case myCase : cases){
            myCase.setCreatedAtStr(sdf.format(new Date(myCase.getCreatedAt() * 1000)));
        }
        return  cases;
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
    @Transactional(rollbackFor = Exception.class)
    public int addCase(Case newCase, List<Client> clients) {
        int r = caseDao.addCase(newCase);
        for(Client c : clients){
            c.setCaseId(newCase.getId());
            clientService.addClient(c);
        }
        return r;
    }

    /**
     * 更新案件信息
     *
     * @param oldCase 案件信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCase(Case oldCase, List<Client> clients) {
        clientService.deleteByCase(Integer.parseInt(oldCase.getId()));
        for(Client c : clients){
            clientService.addClient(c);
        }
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
    @Transactional(rollbackFor = Exception.class)
    public void deleteCase(Integer caseId) {
        clientService.deleteByCase(caseId);
        caseDao.deleteCase(caseId);
    }

    /**
     * 结案
     *
     * @param caseId 案件编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int solveCase(Integer caseId, Integer clear) {
        int r = caseDao.solveCase(caseId);
        if(clear > 0){
            clientService.solveClient(caseId);
        }
        return  r;
    }

    /**
     * 获取所有的委托人姓名信息
     *
     * @param caseId 案件编号
     * @return 委托人姓名
     */
    @Override
    public String getAllClientName(Integer caseId) {
        return null;
    }

    /**
     * 获取所有的对方当事人姓名信息
     *
     * @param caseId 案件编号
     * @return 对方当事人姓名
     */
    @Override
    public String getAllOpponentName(Integer caseId) {
        return null;
    }


}
