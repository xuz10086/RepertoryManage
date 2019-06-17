package com.indusfo.repertorymanage.controller;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.indusfo.repertorymanage.activity.TakeStockActivity;
import com.indusfo.repertorymanage.bean.ProStoreCheck;
import com.indusfo.repertorymanage.bean.RResult;
import com.indusfo.repertorymanage.cons.IdiyMessage;
import com.indusfo.repertorymanage.cons.NetworkConst;
import com.indusfo.repertorymanage.utils.NetworkUtil;

import java.util.HashMap;

public class TakeStockController extends BaseController {
    public TakeStockController(Context c) {
        super(c);
    }

    @Override
    protected void handleMessage(int action, Object... values) {
        RResult rResult;
        switch (action) {
            case IdiyMessage.QUERY_STORE_LOCATION:
                // 库位查询
                rResult = queryStoreroomLocation(url + NetworkConst.QUERY_STORE_LOCATION_URL);
                mListener.onModeChange(IdiyMessage.QUERY_STORE_LOCATION_RESULT, rResult);
                break;
            case IdiyMessage.QUERY_BY_TIAO_MA:
                rResult = queryByTiaoMa(url+NetworkConst.QUERY_BY_TIAO_MA2, (String) values[0], (String) values[1]);
                mListener.onModeChange(IdiyMessage.QUERY_BY_TIAO_MA_RESULT, rResult);
                break;
            case IdiyMessage.INSERST_TAKE_STOCK:
                rResult = insertTakeStock(url+NetworkConst.INSERT_TAKE_STOCK_URL, (ProStoreCheck) values[0]);
                mListener.onModeChange(IdiyMessage.INSERST_TAKE_STOCK_RESULT, rResult);
                break;
        }
    }

    /**
     * 新增盘点明细
     *
     * @author xuz
     * @date 2019/6/3 5:20 PM
     * @param [s, value]
     * @return com.indusfo.repertorymanage.bean.RResult
     */
    private RResult insertTakeStock(String url, ProStoreCheck proStoreCheck) {
        Gson gson = new Gson();
        String json = gson.toJson(proStoreCheck);
        String jsonResult = NetworkUtil.doPostSetCookieJSON(url, json, cookie);
        return JSON.parseObject(jsonResult, RResult.class);
    }

    /**
     * 通过条码查询关联信息
     *
     * @author xuz
     * @date 2019/6/3 8:55 AM
     * @param [s, value]
     * @return com.indusfo.repertorymanage.bean.RResult
     */
    private RResult queryByTiaoMa(String url, String vcProStoreCode, String lStoreId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("vcCheckDCode", vcProStoreCode);
        params.put("lStoreId", lStoreId);
        String json = NetworkUtil.doPostSetCookie(url, params, cookie);
        return JSON.parseObject(json, RResult.class);
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
