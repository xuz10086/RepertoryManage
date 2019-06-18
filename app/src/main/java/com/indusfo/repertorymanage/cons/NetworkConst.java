package com.indusfo.repertorymanage.cons;

/**
 * URL常量类
 *  
 * @author xuz
 * @date 2019/1/11 9:21 AM
 */
public class NetworkConst {

    // 数据库存储数据状态，正常
    public static final String L_DATA_STATE = "1";

//    public static final String BASE_URL = "http://192.168.0.116:8080";
    public static final String BASE_URL = "";

    // 登陆
    public static final String LOGIN_URL = BASE_URL+"/doLogin";

    // 产品查询
    public static final String QUERY_PRODUCT_URL = BASE_URL+"/product/queryProduct";

    // 库房库位查询
    public static final Object QUERY_STORE_LOCATION_URL = BASE_URL + "/storeroom/queryStoreroom";

    // 获取天平的TCP连接信息
    public static final String GET_TCP_PARAMS_URL = BASE_URL + "/detection/Instrument/queryInstrument";

    // 入库新增
    public static final String INSERT_PUT_IN_URL = BASE_URL + "/pda/proStore/insertProStore";
    // 扫描查询
    public static final String QUERY_SCANNING_MSG_URL = BASE_URL + "/pda/proStoreTage/selectProStoreTag";
    // 出库新增
    public static final String INSERT_PUT_OUT_URL = BASE_URL + "/pda/proStore/insertProStoreOut";
    // 根据条码查询相关信息
    public static final String QUERY_BY_TIAO_MA = BASE_URL + "/pda/proStoreTage/selectProStoreTag";

    // 盘点
    // 根据条码查询相关信息
    public static final String QUERY_BY_TIAO_MA2 = BASE_URL + "/pda/proStoreCheck/selectProStoreCheck";
    // 新增盘点明细
    public static final String INSERT_TAKE_STOCK_URL = BASE_URL + "/pda/proStoreCheck/insertProStoreCheck";

    // App在线升级
    public static final String UPDATA_VERSION_REQ_URL = BASE_URL + "/pda/scanApp/queryScanApp2";
}
