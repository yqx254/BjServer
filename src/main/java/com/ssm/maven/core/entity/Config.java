package com.ssm.maven.core.entity;

import java.io.Serializable;

public class Config implements Serializable {
    private String configId;
    private String configName;
    private int configValue;
    private int configType;

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public int getConfigType() {
        return configType;
    }

    public void setConfigType(int configType) {
        this.configType = configType;
    }

    public int getConfigValue() {
        return configValue;
    }

    public void setConfigValue(int configValue) {
        this.configValue = configValue;
    }

}
