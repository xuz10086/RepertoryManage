package com.indusfo.repertorymanage.controller;

import android.content.Context;
import android.net.Network;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.indusfo.repertorymanage.activity.PutInActivity;
import com.indusfo.repertorymanage.bean.ProStore;
import com.indusfo.repertorymanage.bean.RResult;
import com.indusfo.repertorymanage.bean.Storeroom;
import com.indusfo.repertorymanage.cons.IdiyMessage;
import com.indusfo.repertorymanage.cons.NetworkConst;
import com.indusfo.repertorymanage.utils.NetworkUtil;

import java.util.HashMap;

public class PutInController extends BaseController {
    public PutInController(Context c) {
        super(c);
    }

    @Override
    protected void handleMessage(int action, Object... values) {
        RResult rResult;
        switch (action) {
            case IdiyMessage.QUERY_STORE_LOCATION:
                // 库位查询
                rResult = queryStoreroomLocation(url+NetworkConst.QUERY_STORE_LOCATION_URL);
                mListener.onModeChange(IdiyMessage.QUERY_STORE_LOCATION_RESULT, rResult);
                break;
            case IdiyMessage.INSERT_PUT_IN:
                rResult = insertPutIn(url+NetworkConst.INSERT_PUT_IN_URL, (ProStore)values[0]);
                mListener.onModeChange(IdiyMessage.INSERT_PUT_IN_RESULT, rResult);
                break;
            case IdiyMessage.INSERT_PUT_OUT:
                rResult = insertPutIn(url+NetworkConst.INSERT_PUT_OUT_URL, (ProStore)values[0]);
                mListener.onModeChange(IdiyMessage.INSERT_PUT_IN_RESULT, rResult);
                break;
            case IdiyMessage.QUERY_BY_TIAO_MA:
                rResult = queryByTiaoMa(url+NetworkConst.QUERY_BY_TIAO_MA, (String) values[0], (String) values[1]);
                mListener.onModeChange(IdiyMessage.QUERY_BY_TIAO_MA_RESULT, rResult);
                break;
            default:
                break;
        }
    }

    /**
     * 通过条码查询关联信息
     *
     * @author xuz
     * @date 2019/6/3 8:55 AM
     * @param [s, value]
     * @return com.indusfo.repertorymanage.bean.RResult
     */
    private RResult queryByTiaoMa(String url, String vcProStoreCode, String lStoreMark) {
        HashMap<String, String> params = new HashMap<>();
        params.put("vcProStoreCode", vcProStoreCode);
        params.put("lStoreMark", lStoreMark);
        String json = NetworkUtil.doPostSetCookie(url, params, cookie);
        return JSON.parseObject(json, RResult.class);
    }

    /**
     * 新增库存
     *
     * @author xuz
     * @date 2019/6/1 7:12 PM
     * @param [s, value]
     * @return com.indusfo.repertorymanage.bean.RResult
     */
    private RResult insertPutIn(String url, ProStore proStore) {
        Gson gson = new Gson();
        String json = gson.toJson(proStore);
        String jsonResult = NetworkUtil.doPostSetCookieJSON(url, json, cookie);
        return JSON.parseObject(jsonResult, RResult.class);
    }

    /**
     * 库房库位查询
     *
     * @author xuz
     * @date 2019/5/31 6:40 PM
     * @param [url, lStoreId]
     * @return com.indusfo.repertorymanage.bean.RResult
     */
    private RResult queryStoreroomLocation(String url) {
        HashMap<String, String> params = new HashMap<>();
        String json = NetworkUtil.doPostSetCookie(url, params, cookie);
        return JSON.parseObject(json, RResult.class);
    }

}
