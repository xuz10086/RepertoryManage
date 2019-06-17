package com.indusfo.repertorymanage.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 成品库存信息
 *  
 * @author xuz
 * @date 2019/6/1 11:10 AM
 * @param 
 * @return
 */
public class ProStore implements Serializable {

    //出入库标志
    private Integer lStoreMark;
    //汇总数量
    private Integer summary;
    //成品库存明细
    private List<ProStoreD> proStoreDList;

    public Integer getlStoreMark() {
        return lStoreMark;
    }

    public void setlStoreMark(Integer lStoreMark) {
        this.lStoreMark = lStoreMark;
    }

    public Integer getSummary() {
        return summary;
    }

    public void setSummary(Integer summary) {
        this.summary = summary;
    }

    public List<ProStoreD> getProStoreDList() {
        return proStoreDList;
    }

    public void setProStoreDList(List<ProStoreD> proStoreDList) {
        this.proStoreDList = proStoreDList;
    }

    @Override
    public String toString() {
        return "ProStore{" +
                "lStoreMark=" + lStoreMark +
                ", summary=" + summary +
                ", proStoreDList=" + proStoreDList +
                '}';
    }
}
