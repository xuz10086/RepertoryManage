package com.indusfo.repertorymanage.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Gather implements Serializable,Parcelable {

    // 型号
    private String vcModel;
    // 数量
    private Integer sum;
    // 箱数
    private Integer cartons;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // 1.必须按成员变量声明的顺序封装数据，不然会出现获取数据出错
        // 2.序列化对象
        parcel.writeString(vcModel);
        parcel.writeInt(sum);
        parcel.writeInt(cartons);
    }

    // 1.必须实现Parcelable.Creator接口,否则在获取Person数据的时候，会报错，如下：
    // android.os.BadParcelableException:
    // Parcelable protocol requires a Parcelable.Creator object called  CREATOR on class com.um.demo.Person
    // 2.这个接口实现了从Percel容器读取Person数据，并返回Person对象给逻辑层使用
    // 3.实现Parcelable.Creator接口对象名必须为CREATOR，不如同样会报错上面所提到的错；
    // 4.在读取Parcel容器里的数据事，必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
    // 5.反序列化对象
    public static final Parcelable.Creator<Gather> CREATOR = new Creator(){

        @Override
        public Gather createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            Gather p = new Gather();
            p.setVcModel(source.readString());
            p.setSum(source.readInt());
            p.setCartons(source.readInt());

            return p;
        }

        @Override
        public Gather[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Gather[size];
        }
    };

    public String getVcModel() {
        return vcModel;
    }

    public void setVcModel(String vcModel) {
        this.vcModel = vcModel;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public Integer getCartons() {
        return cartons;
    }

    public void setCartons(Integer cartons) {
        this.cartons = cartons;
    }
}
