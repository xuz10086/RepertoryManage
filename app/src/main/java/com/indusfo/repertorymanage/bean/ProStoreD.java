package com.indusfo.repertorymanage.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 成品库存明细
 *
 * @author xuz
 * @date 2019/6/1 11:13 AM
 * @param
 * @return
 */
public class ProStoreD implements Serializable,Parcelable {
    //条码
    private String vcProStoreCode;
    //产品id
    private String lProduct;
    //产品型号
    private String vcModel;
    //库房id
    private String lStoreId;
    //库位id
    private String lLocaId;
    //订单号
    private String vcOrder;
    //数量
    private String vcNum;
    //库房名称
    private String vcStoreName;
    //库位名称
    private String vcLocaName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
//        if (null==lProduct) {
//            lProduct = -1;
//        }
//        if (null==lStoreId) {
//            lStoreId = -1;
//        }
//        if (null==lLocaId) {
//            lLocaId = -1;
//        }
        // 1.必须按成员变量声明的顺序封装数据，不然会出现获取数据出错
        // 2.序列化对象
        parcel.writeString(vcProStoreCode);
        parcel.writeString(lProduct);
        parcel.writeString(vcModel);
        parcel.writeString(lStoreId);
        parcel.writeString(lLocaId);
        parcel.writeString(vcOrder);
        parcel.writeString(vcNum);
        parcel.writeString(vcStoreName);
        parcel.writeString(vcLocaName);
    }

    // 1.必须实现Parcelable.Creator接口,否则在获取Person数据的时候，会报错，如下：
    // android.os.BadParcelableException:
    // Parcelable protocol requires a Parcelable.Creator object called  CREATOR on class com.um.demo.Person
    // 2.这个接口实现了从Percel容器读取Person数据，并返回Person对象给逻辑层使用
    // 3.实现Parcelable.Creator接口对象名必须为CREATOR，不如同样会报错上面所提到的错；
    // 4.在读取Parcel容器里的数据事，必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
    // 5.反序列化对象
    public static final Parcelable.Creator<ProStoreD> CREATOR = new Creator(){

        @Override
        public ProStoreD createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            ProStoreD p = new ProStoreD();
            p.setVcProStoreCode(source.readString());
            p.setlProduct(source.readString());
            p.setVcModel(source.readString());
            p.setlStoreId(source.readString());
            p.setlLocaId(source.readString());
            p.setVcOrder(source.readString());
            p.setVcNum(source.readString());
            p.setVcStoreName(source.readString());
            p.setVcLocaName(source.readString());
            return p;
        }

        @Override
        public ProStoreD[] newArray(int size) {
            // TODO Auto-generated method stub
            return new ProStoreD[size];
        }
    };

    public String getVcProStoreCode() {
        return vcProStoreCode;
    }

    public void setVcProStoreCode(String vcProStoreCode) {
        this.vcProStoreCode = vcProStoreCode;
    }

    public String getlProduct() {
        return lProduct;
    }

    public void setlProduct(String lProduct) {
        this.lProduct = lProduct;
    }

    public String getVcModel() {
        return vcModel;
    }

    public void setVcModel(String vcModel) {
        this.vcModel = vcModel;
    }

    public String getlStoreId() {
        return lStoreId;
    }

    public void setlStoreId(String lStoreId) {
        this.lStoreId = lStoreId;
    }

    public String getlLocaId() {
        return lLocaId;
    }

    public void setlLocaId(String lLocaId) {
        this.lLocaId = lLocaId;
    }

    public String getVcOrder() {
        return vcOrder;
    }

    public void setVcOrder(String vcOrder) {
        this.vcOrder = vcOrder;
    }

    public String getVcNum() {
        return vcNum;
    }

    public void setVcNum(String vcNum) {
        this.vcNum = vcNum;
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

    @Override
    public String toString() {
        return "ProStoreD{" +
                "vcProStoreCode='" + vcProStoreCode + '\'' +
                ", lProduct='" + lProduct + '\'' +
                ", vcModel='" + vcModel + '\'' +
                ", lStoreId='" + lStoreId + '\'' +
                ", lLocaId='" + lLocaId + '\'' +
                ", vcOrder='" + vcOrder + '\'' +
                ", vcNum='" + vcNum + '\'' +
                ", vcStoreName='" + vcStoreName + '\'' +
                ", vcLocaName='" + vcLocaName + '\'' +
                '}';
    }
}
