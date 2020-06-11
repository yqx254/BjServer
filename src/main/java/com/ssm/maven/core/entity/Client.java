package com.ssm.maven.core.entity;

import java.io.Serializable;

public class Client implements Serializable {
    private String clientId;
    private String caseId;
    private String clientName;
    private int identity;
    private int clientType;
    private String caseCode;
    private String realName;
    private int status;
    private long createdAt;
    private int deleteFlag;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public int getClientType() {
        return clientType;
    }

    public void setClientType(int type) {
        this.clientType = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCaseCode() {
        return caseCode;
    }

    public void setCaseCode(String caseCode) {
        this.caseCode = caseCode;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Client copy(){
        Client copy = new Client();
        copy.setCaseId(this.caseId);
        copy.setCaseCode(this.caseCode);
        copy.setRealName(this.realName);
        copy.setIdentity(this.identity);
        copy.setClientName(this.clientName);
        copy.setCreatedAt(this.createdAt);
        copy.setClientType(this.clientType);
        return copy;
    }
}
