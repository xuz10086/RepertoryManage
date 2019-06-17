package com.indusfo.repertorymanage.controller;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.indusfo.repertorymanage.bean.RResult;
import com.indusfo.repertorymanage.cons.IdiyMessage;
import com.indusfo.repertorymanage.cons.NetworkConst;
import com.indusfo.repertorymanage.utils.NetworkUtil;

import java.util.HashMap;


public class ProductQueryController extends BaseController {

    public ProductQueryController(Context c) {
        super(c);
    }

    @Override
    protected void handleMessage(int action, Object... values) {
        RResult rResult;
        switch (action) {
            case IdiyMessage.QUERY_PRODUCT:
                // 产品模糊查询
                rResult = queryProduct(url+NetworkConst.QUERY_PRODUCT_URL,(String)values[0],
                        (String) values[1], (String) values[2]);
                mListener.onModeChange(IdiyMessage.QUERY_PRODUCT_RESULT, rResult);
                break;
            case IdiyMessage.QUERY_PRODUCT_NEXT_PAGE:
                // 下滑
                rResult= queryProduct(url+NetworkConst.QUERY_PRODUCT_URL,(String)values[0],
                        (String) values[1], (String) values[2]);
                mListener.onModeChange(IdiyMessage.QUERY_PRODUCT_NEXT_PAGE_RESULT, rResult);
                break;

        }
    }

    /**
     * 根据型号，模糊查询产品
     *
     * @author xuz
     * @date 2019/5/31 10:09 AM
     * @param [value]
     * @return com.indusfo.repertorymanage.bean.RResult
     */
    private RResult queryProduct(String url, String vcModel, String pagesize, String pageindex) {
        HashMap<String, String> params = new HashMap<>();
        params.put("vcModel", vcModel);
        params.put("pagesize", pagesize);
        params.put("pageindex", pageindex);
        String json = NetworkUtil.doPostSetCookie(url, params, cookie);
        return JSON.parseObject(json, RResult.class);
    }
}
