package com.indusfo.repertorymanage.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 盘点
 *  
 * @author xuz
 * @date 2019/6/3 3:34 PM
 * @param 
 * @return 
 */
public class ProStoreCheck implements Serializable {

    //成品库存盘点id
    private String lStoreCheck;
    //盘点单编码
    private String vcCheckCode;
    //盘点库位id
    private String lLocaId;
    //盘点库房id
    private String lStoreId;
    //盘点人
    private Integer lCheckUser;
    //盘点时间
    private String dCheckTime;
    //是否盘点完成
    private Integer lCheckOver;
    //是否正常
    private Integer lCheckNormal;
    //说明
    private String vcRemark;
    //数据状态
    private Integer lDataState;
    //盘点明细
    private List<CheckD> checkDList;

    public String getlStoreCheck() {
        return lStoreCheck;
    }

    public void setlStoreCheck(String lStoreCheck) {
        this.lStoreCheck = lStoreCheck;
    }

    public String getVcCheckCode() {
        return vcCheckCode;
    }

    public void setVcCheckCode(String vcCheckCode) {
        this.vcCheckCode = vcCheckCode;
    }

    public String getlLocaId() {
        return lLocaId;
    }

    public void setlLocaId(String lLocaId) {
        this.lLocaId = lLocaId;
    }

    public String getlStoreId() {
        return lStoreId;
    }

    public void setlStoreId(String lStoreId) {
        this.lStoreId = lStoreId;
    }

    public Integer getlCheckUser() {
        return lCheckUser;
    }

    public void setlCheckUser(Integer lCheckUser) {
        this.lCheckUser = lCheckUser;
    }

    public String getdCheckTime() {
        return dCheckTime;
    }

    public void setdCheckTime(String dCheckTime) {
        this.dCheckTime = dCheckTime;
    }

    public Integer getlCheckOver() {
        return lCheckOver;
    }

    public void setlCheckOver(Integer lCheckOver) {
        this.lCheckOver = lCheckOver;
    }

    public Integer getlCheckNormal() {
        return lCheckNormal;
    }

    public void setlCheckNormal(Integer lCheckNormal) {
        this.lCheckNormal = lCheckNormal;
    }

    public String getVcRemark() {
        return vcRemark;
    }

    public void setVcRemark(String vcRemark) {
        this.vcRemark = vcRemark;
    }

    public Integer getlDataState() {
        return lDataState;
    }

    public void setlDataState(Integer lDataState) {
        this.lDataState = lDataState;
    }

    public List<CheckD> getCheckDList() {
        return checkDList;
    }

    public void setCheckDList(List<CheckD> checkDList) {
        this.checkDList = checkDList;
    }

    @Override
    public String toString() {
        return "ProStoreCheck{" +
                "lStoreCheck='" + lStoreCheck + '\'' +
                ", vcCheckCode='" + vcCheckCode + '\'' +
                ", lLocaId='" + lLocaId + '\'' +
                ", lStoreId='" + lStoreId + '\'' +
                ", lCheckUser=" + lCheckUser +
                ", dCheckTime='" + dCheckTime + '\'' +
                ", lCheckOver=" + lCheckOver +
                ", lCheckNormal=" + lCheckNormal +
                ", vcRemark='" + vcRemark + '\'' +
                ", lDataState=" + lDataState +
                ", checkDList=" + checkDList +
                '}';
    }
}
