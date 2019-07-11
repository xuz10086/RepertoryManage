package com.indusfo.repertorymanage.bean;

import java.io.Serializable;

/**
 * 库位
 *  
 * @author xuz
 * @date 2019/6/1 11:34 AM
 * @param 
 * @return 
 */
public class Location implements Serializable {
    /**
     * x库位id
     */
    private String lLocaId;

    /**
     * x库位名称
     */
    private String vcLocaName;

    /**
     * x库位编码
     */
    private String vcLocaCode;

    /**
     * x库房ID
     */
    private String lStoreId;

    public String getlLocaId() {
        return lLocaId;
    }

    public void setlLocaId(String lLocaId) {
        this.lLocaId = lLocaId;
    }

    public String getVcLocaName() {
        return vcLocaName;
    }

    public void setVcLocaName(String vcLocaName) {
        this.vcLocaName = vcLocaName;
    }

    public String getVcLocaCode() {
        return vcLocaCode;
    }

    public void setVcLocaCode(String vcLocaCode) {
        this.vcLocaCode = vcLocaCode;
    }

    public String getlStoreId() {
        return lStoreId;
    }

    public void setlStoreId(String lStoreId) {
        this.lStoreId = lStoreId;
    }

    @Override
    public String toString() {
        return "Location{" +
                "lLocaId='" + lLocaId + '\'' +
                ", vcLocaName='" + vcLocaName + '\'' +
                ", vcLocaCode='" + vcLocaCode + '\'' +
                ", lStoreId='" + lStoreId + '\'' +
                '}';
    }
}
