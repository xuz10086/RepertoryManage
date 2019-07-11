package com.indusfo.repertorymanage.bean;


import java.io.Serializable;
import java.util.List;

/**
 * 库房
 *
 * @author xuz
 * @date 2019/6/1 11:10 AM
 * @param
 * @return
 */
public class Storeroom implements Serializable {
    /**
     * x库房id
     */
    private String lStoreId;

    /**
     * x库房编号
     */
    private String vcStoreCode;

    /**
     * x库房名称
     */
    private String vcStoreName;

    private List<Location> listLocation;

    public String getlStoreId() {
        return lStoreId;
    }

    public void setlStoreId(String lStoreId) {
        this.lStoreId = lStoreId;
    }

    public String getVcStoreCode() {
        return vcStoreCode;
    }

    public void setVcStoreCode(String vcStoreCode) {
        this.vcStoreCode = vcStoreCode;
    }

    public String getVcStoreName() {
        return vcStoreName;
    }

    public void setVcStoreName(String vcStoreName) {
        this.vcStoreName = vcStoreName;
    }

    public List<Location> getListLocation() {
        return listLocation;
    }

    public void setListLocation(List<Location> listLocation) {
        this.listLocation = listLocation;
    }

    @Override
    public String toString() {
        return "Storeroom{" +
                "lStoreId='" + lStoreId + '\'' +
                ", vcStoreCode='" + vcStoreCode + '\'' +
                ", vcStoreName='" + vcStoreName + '\'' +
                ", listLocation=" + listLocation +
                '}';
    }
}
