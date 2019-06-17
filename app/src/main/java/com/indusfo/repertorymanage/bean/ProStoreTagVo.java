package com.indusfo.repertorymanage.bean;

import java.io.Serializable;

/**
 * 条码关联数据
 *
 * @author xuz
 * @date 2019/6/3 9:02 AM
 * @param
 * @return
 */
public class ProStoreTagVo implements Serializable {
    //主键id
    private Integer lProStoreTag;
    //批号
    private String vcProStoreCode;
    //产品id
    private Integer lProduct;
    //产品型号  页面
    private String vcModel;
    //每箱数量 spq
    private Integer vcNum;
    //订单号 页面
    private String vcOrder;
    //库房名称
    private String vcStoreName;
    //库位名称
    private String vcLocaName;
    //出入库标志
    private Integer lStoreMark;

    public Integer getlProStoreTag() {
        return lProStoreTag;
    }

    public void setlProStoreTag(Integer lProStoreTag) {
        this.lProStoreTag = lProStoreTag;
    }

    public String getVcProStoreCode() {
        return vcProStoreCode;
    }

    public void setVcProStoreCode(String vcProStoreCode) {
        this.vcProStoreCode = vcProStoreCode;
    }

    public Integer getlProduct() {
        return lProduct;
    }

    public void setlProduct(Integer lProduct) {
        this.lProduct = lProduct;
    }

    public String getVcModel() {
        return vcModel;
    }

    public void setVcModel(String vcModel) {
        this.vcModel = vcModel;
    }

    public Integer getVcNum() {
        return vcNum;
    }

    public void setVcNum(Integer vcNum) {
        this.vcNum = vcNum;
    }

    public String getVcOrder() {
        return vcOrder;
    }

    public void setVcOrder(String vcOrder) {
        this.vcOrder = vcOrder;
    }

    public String getVcStoreName() {
        return vcStoreName;
    }

    public void setVcStoreName(String vcStoreName) {
        this.vcStoreName = vcStoreName;
    }

    public String getVcLocaName() {
        return vcLocaName;
    }

    public void setVcLocaName(String vcLocaName) {
        this.vcLocaName = vcLocaName;
    }

    public Integer getlStoreMark() {
        return lStoreMark;
    }

    public void setlStoreMark(Integer lStoreMark) {
        this.lStoreMark = lStoreMark;
    }

    @Override
    public String toString() {
        return "ProStoreTagVo{" +
                "lProStoreTag=" + lProStoreTag +
                ", vcProStoreCode='" + vcProStoreCode + '\'' +
                ", lProduct=" + lProduct +
                ", vcModel='" + vcModel + '\'' +
                ", vcNum=" + vcNum +
                ", vcOrder='" + vcOrder + '\'' +
                ", vcStoreName='" + vcStoreName + '\'' +
                ", vcLocaName='" + vcLocaName + '\'' +
                ", lStoreMark=" + lStoreMark +
                '}';
    }
}
