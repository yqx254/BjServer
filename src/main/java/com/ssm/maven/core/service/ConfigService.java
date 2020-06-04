package com.ssm.maven.core.service;

/**
 * @author fstar
 */
public interface ConfigService {
    /**
     * 获取案号
     * @param category 类别
     * @return 案号
     */
    String setThenGetCaseCode(int category);


}
