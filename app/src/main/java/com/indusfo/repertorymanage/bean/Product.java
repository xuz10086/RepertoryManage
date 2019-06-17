package com.indusfo.repertorymanage.bean;

import java.io.Serializable;

/**
 * 产品
 *
 * @author xuz
 * @date 2019/5/31 9:00 AM
 * @param
 * @return
 */
public class Product implements Serializable {

    // 产品id
    private Integer lProduct;
    // 产品名称
    private String vcProductName;
    // 规格型号
    private String vcModel;

    public Integer getlProduct() {
        return lProduct;
    }

    public void setlProduct(Integer lProduct) {
        this.lProduct = lProduct;
    }

    public String getVcProductName() {
        return vcProductName;
    }

    public void setVcProductName(String vcProductName) {
        this.vcProductName = vcProductName;
    }

    public String getVcModel() {
        return vcModel;
    }

    public void setVcModel(String vcModel) {
        this.vcModel = vcModel;
    }

    @Override
    public String toString() {
        return "Product{" +
                "lProduct=" + lProduct +
                ", vcProductName='" + vcProductName + '\'' +
                ", vcModel='" + vcModel + '\'' +
                '}';
    }
}
