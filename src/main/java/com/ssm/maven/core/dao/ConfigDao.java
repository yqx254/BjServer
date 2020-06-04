package com.ssm.maven.core.dao;

/**
 * @author fstar
 */
public interface ConfigDao {
    /**
     * 获取月份值
     * @return 月份值
     */
    String getMonth();

    /**
     * 更新月份
     * @param month 当前月份
     */
    void setMonth(String month);

    /**
     * 每月固定重置案号
     */
    void resetSerial();

    /**
     *  获取案号的数字部分
     * @param configName 类型
     * @return 配置好的编号
     */
    int getCaseSerial(String configName);

    /**
     * 案号自增
     * @param configName 类型
     */
    void setCaseSerial(String configName);
}
