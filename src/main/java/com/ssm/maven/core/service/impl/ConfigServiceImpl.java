package com.ssm.maven.core.service.impl;

import com.ssm.maven.core.dao.ConfigDao;
import com.ssm.maven.core.service.ConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author fstar
 */
@Service("ConfigService")
public class ConfigServiceImpl implements ConfigService {
    @Resource
    private ConfigDao configDao;
    /**
     * 获取案号
     *
     * @param category 类别
     * @return 案号
     */
    @Override
    public String setThenGetCaseCode(int category) {
        StringBuilder result = new StringBuilder();
        LocalDate date = LocalDate.now();
        String categoryName;
        switch (category){
            case 0:
                categoryName = "m";
                result.append("BJM");
                break;
            case 1:
                categoryName = "x";
                result.append("BJX");
                break;
            case 2:
                categoryName = "xz";
                result.append("BJXZ");
                break;
            case 3:
                categoryName = "g";
                result.append("BJG");
                break;
            case 4:
                categoryName = "q";
                result.append("BJQ");
                break;
            default:
                return null;
        }
        result.append(date.getYear());
        int month = date.getMonthValue();
        if(month != Integer.parseInt(configDao.getMonth())){
            configDao.setMonth(String.valueOf(month));
            configDao.resetSerial();
        }
        if(month < 10){
            result.append(0);
        }
        result.append(month);
        String caseSerial = String.valueOf(configDao.getCaseSerial(categoryName));
        for(int i = 0;i < 3 - caseSerial.length(); i ++){
            result.append(0);
        }
        result.append(caseSerial);
        configDao.setCaseSerial(categoryName);
        return result.toString();
    }
}
