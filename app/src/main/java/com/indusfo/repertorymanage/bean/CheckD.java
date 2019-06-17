package com.indusfo.repertorymanage.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * 盘点明细
 *
 * @author xuz
 * @date 2019/6/3 3:34 PM
 * @param
 * @return
 */
public class CheckD implements Serializable,Parcelable {
    //盘点明细id
    private Integer lCheckD;
    //盘点单id
    private Integer lStoreCheck;
    //批号
    private String vcCheckDCode;
    //产品id
    private String lProduct;
    //产品型号
    private String vcModel;
    //库房id
    private Integer lStoreId;
    //盘点库位id
    private Integer lCheckLoca;
    //实际库位
    private Integer lReallyLoca;
    //数量
    private String vcNum;
    //是否正常
    private Integer lNormal;
    //移库人id
    private Integer lMoveUser;
    //移库时间
    private String dMoveTime;
    //说明
    private String vcRemark;
    //数据状态
    private Integer lDataState;
    // 库位名
    private String vcLocationName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // 1.必须按成员变量声明的顺序封装数据，不然会出现获取数据出错
        // 2.序列化对象
        parcel.writeInt(lCheckD);
        parcel.writeInt(lStoreCheck);
        parcel.writeString(vcCheckDCode);
        parcel.writeString(lProduct);
        parcel.writeString(vcModel);
        parcel.writeInt(lStoreId);
        parcel.writeInt(lCheckLoca);
        parcel.writeInt(lReallyLoca);
        parcel.writeString(vcNum);
        parcel.writeInt(lNormal);
        parcel.writeInt(lMoveUser);
        parcel.writeString(dMoveTime);
        parcel.writeString(vcRemark);
        parcel.writeInt(lDataState);
        parcel.writeString(vcLocationName);
    }

    // 1.必须实现Parcelable.Creator接口,否则在获取Person数据的时候，会报错，如下：
    // android.os.BadParcelableException:
    // Parcelable protocol requires a Parcelable.Creator object called  CREATOR on class com.um.demo.Person
    // 2.这个接口实现了从Percel容器读取Person数据，并返回Person对象给逻辑层使用
    // 3.实现Parcelable.Creator接口对象名必须为CREATOR，不如同样会报错上面所提到的错；
    // 4.在读取Parcel容器里的数据事，必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
    // 5.反序列化对象
    public static final Parcelable.Creator<CheckD> CREATOR = new Creator(){

        @Override
        public CheckD createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            CheckD p = new CheckD();
            p.setlCheckD(source.readInt());
            p.setlStoreCheck(source.readInt());
            p.setVcCheckDCode(source.readString());
            p.setlProduct(source.readString());
            p.setVcModel(source.readString());
            p.setlStoreId(source.readInt());
            p.setlCheckLoca(source.readInt());
            p.setlReallyLoca(source.readInt());
            p.setVcNum(source.readString());
            p.setlNormal(source.readInt());
            p.setlMoveUser(source.readInt());
            p.setdMoveTime(source.readString());
            p.setVcRemark(source.readString());
            p.setlDataState(source.readInt());
            p.setVcLocationName(source.readString());
            return p;
        }

        @Override
        public CheckD[] newArray(int size) {
            // TODO Auto-generated method stub
            return new CheckD[size];
        }
    };

    public Integer getlCheckD() {
        return lCheckD;
    }

    public void setlCheckD(Integer lCheckD) {
        this.lCheckD = lCheckD;
    }

    public Integer getlStoreCheck() {
        return lStoreCheck;
    }

    public void setlStoreCheck(Integer lStoreCheck) {
        this.lStoreCheck = lStoreCheck;
    }

    public String getVcCheckDCode() {
        return vcCheckDCode;
    }

    public void setVcCheckDCode(String vcCheckDCode) {
        this.vcCheckDCode = vcCheckDCode;
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

    public Integer getlStoreId() {
        return lStoreId;
    }

    public void setlStoreId(Integer lStoreId) {
        this.lStoreId = lStoreId;
    }

    public Integer getlCheckLoca() {
        return lCheckLoca;
    }

    public void setlCheckLoca(Integer lCheckLoca) {
        this.lCheckLoca = lCheckLoca;
    }

    public Integer getlReallyLoca() {
        return lReallyLoca;
    }

    public void setlReallyLoca(Integer lReallyLoca) {
        this.lReallyLoca = lReallyLoca;
    }

    public String getVcNum() {
        return vcNum;
    }

    public void setVcNum(String vcNum) {
        this.vcNum = vcNum;
    }

    public Integer getlNormal() {
        return lNormal;
    }

    public void setlNormal(Integer lNormal) {
        this.lNormal = lNormal;
    }

    public Integer getlMoveUser() {
        return lMoveUser;
    }

    public void setlMoveUser(Integer lMoveUser) {
        this.lMoveUser = lMoveUser;
    }

    public String getdMoveTime() {
        return dMoveTime;
    }

    public void setdMoveTime(String dMoveTime) {
        this.dMoveTime = dMoveTime;
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

    public String getVcLocationName() {
        return vcLocationName;
    }

    public void setVcLocationName(String vcLocationName) {
        this.vcLocationName = vcLocationName;
    }

    @Override
    public String toString() {
        return "CheckD{" +
                "lCheckD=" + lCheckD +
                ", lStoreCheck=" + lStoreCheck +
                ", vcCheckDCode='" + vcCheckDCode + '\'' +
                ", lProduct='" + lProduct + '\'' +
                ", vcModel='" + vcModel + '\'' +
                ", lStoreId=" + lStoreId +
                ", lCheckLoca=" + lCheckLoca +
                ", lReallyLoca=" + lReallyLoca +
                ", vcNum='" + vcNum + '\'' +
                ", lNormal=" + lNormal +
                ", lMoveUser=" + lMoveUser +
                ", dMoveTime='" + dMoveTime + '\'' +
                ", vcRemark='" + vcRemark + '\'' +
                ", lDataState=" + lDataState +
                ", vcLocationName='" + vcLocationName + '\'' +
                '}';
    }
}
