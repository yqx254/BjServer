package com.ssm.maven.core.entity;

import java.io.Serializable;

/**
 * @author fstar
 */
    public class Case implements Serializable {
    private String id;
    private String caseCode;
    private String clientName;
    private String opponentName;
    private String [] clientNameArr;
    private String [] opponentNameArr;
    private int [] clientIdtArr;
    private int [] opponentIdtArr;
    private int clientCount;
    private int opponentCount;
    private int category;
    private String dealer;
    private String remarks;
    private long createdAt;
    private long updatedAt;
    private int deleteFlag;
    private int createId;
    private int status;
    private String createTel;
    private String createName;
    private String createKW;

    private String categoryShow;
    private String clientNameShow;
    private String opponentNameShow;
    private String statusShow;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCaseCode(String caseCode) {
        this.caseCode = caseCode;
    }

    public String getCaseCode() {
        return caseCode;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getDealer() {
        return dealer;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setCreateId(int createId) {
        this.createId = createId;
    }

    public int getCreateId() {
        return createId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public int getClientCount() {
        return clientCount;
    }

    public void setClientCount(int clientCount) {
        this.clientCount = clientCount;
        if(clientCount > 1){
            this.setClientNameShow(this.getClientName() + " 等");
        }
        else{
            this.setClientNameShow(this.getClientName());
        }
    }

    public int getOpponentCount() {
        return opponentCount;
    }

    public void setOpponentCount(int opponentCount) {
        this.opponentCount = opponentCount;
        if(opponentCount > 1){
            this.setOpponentNameShow(this.getOpponentName() + " 等");
        }
        else{
            this.setOpponentNameShow(this.getOpponentName());
        }
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public void setCategory(int category) {
        this.category = category;
        switch (category){
            case 0:
                this.setCategoryShow("民事");
                break;
            case 1:
                this.setCategoryShow("刑事");
                break;
            case 2:
                this.setCategoryShow("行政");
                break;
            case 3:
                this.setCategoryShow("顾问");
                break;
            case 4:
                this.setCategoryShow("其他");
                break;
            default:
                break;
        }
    }

    public int getCategory() {
        return category;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateTel(String createTel) {
        this.createTel = createTel;
    }

    public String getCreateTel() {
        return createTel;
    }

    public void setCreateKW(String createKW) {
        this.createKW = createKW;
    }

    public String getCreateKW() {
        return createKW;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        if(status == 1){
            this.setStatusShow("未结案");
        }
        else{
            this.setStatusShow("已结案");
        }
    }

    public String getCategoryShow() {
        return categoryShow;
    }

    public void setCategoryShow(String categoryShow) {
        this.categoryShow = categoryShow;
    }

    public String getClientNameShow() {
        return clientNameShow;
    }

    public void setClientNameShow(String clientNameShow) {
        this.clientNameShow = clientNameShow;
    }

    public String getOpponentNameShow() {
        return opponentNameShow;
    }

    public void setOpponentNameShow(String opponentNameShow) {
        this.opponentNameShow = opponentNameShow;
    }

    public String getStatusShow() {
        return statusShow;
    }

    public void setStatusShow(String statusShow) {
        this.statusShow = statusShow;
    }

    public void setClientIdtArr(int[] clientIdtArr) {
        this.clientIdtArr = clientIdtArr;
    }

    public int[] getClientIdtArr() {
        return clientIdtArr;
    }

    public void setClientNameArr(String[] clientNameArr) {
        this.clientNameArr = clientNameArr;
    }

    public String[] getClientNameArr() {
        return clientNameArr;
    }

    public void setOpponentIdtArr(int[] opponentIdtArr) {
        this.opponentIdtArr = opponentIdtArr;
    }

    public int[] getOpponentIdtArr() {
        return opponentIdtArr;
    }

    public void setOpponentNameArr(String[] opponentNameArr) {
        this.opponentNameArr = opponentNameArr;
    }

    public String[] getOpponentNameArr() {
        return opponentNameArr;
    }
}
