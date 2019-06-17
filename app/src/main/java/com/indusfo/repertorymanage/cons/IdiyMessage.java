package com.indusfo.repertorymanage.cons;

public class IdiyMessage {

    // 登录action
    public static final int LOGIN_ACTION = 1;
    public static final int LOGIN_ACTION_RESULT = 2;

    // 保存用户到数据库
    public static final int SAVE_USER_TODB = 3;
    public static final int SAVE_USER_TODB_RESULT = 4;
    public static final int GET_USER_FROM_DB = 5;
    public static final int GET_USER_FROM_DB_RESULT = 6;

    // 设置url
    public static final int SETTING_URL = 7;
    // 获取本地URL
    public static final int GET_URL_FROM_LOCAL = 8;
    public static final int GET_URL_FROM_LOCAL_RESULT = 9;

    // 保存cookie到本地
    public static final int SAVE_COOKIE = 10;

    // 产品查询
    public static final int QUERY_PRODUCT = 11;
    public static final int QUERY_PRODUCT_RESULT = 12;
    public static final int QUERY_PRODUCT_1 = 13;
    public static final int QUERY_PRODUCT_2 = 14;
    public static final int QUERY_PRODUCT_NEXT_PAGE = 15;
    public static final int QUERY_PRODUCT_NEXT_PAGE_RESULT = 16;

    // 库位查询
    public static final int QUERY_STORE_LOCATION = 17;
    public static final int QUERY_STORE_LOCATION_RESULT = 18;

    // 扫码成功后
    public static final int AFTER_SCANNING = 19;

    // 明细删除
    public static final int DELETE_MING_XI = 20;

    // 入库新增
    public static final int INSERT_PUT_IN = 21;
    public static final int INSERT_PUT_IN_RESULT = 22;

    // 出入库，条码查询相关其他信息
    public static final int QUERY_BY_TIAO_MA = 23;
    public static final int QUERY_BY_TIAO_MA_RESULT = 24;

    // 出库新增
    public static final int INSERT_PUT_OUT = 25;

    // 盘点新增
    public static final int INSERST_TAKE_STOCK = 26;
    public static final int INSERST_TAKE_STOCK_RESULT = 27;
}
