package com.indusfo.repertorymanage.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.indusfo.repertorymanage.R;
import com.indusfo.repertorymanage.bean.Gather;
import com.indusfo.repertorymanage.bean.Location;
import com.indusfo.repertorymanage.bean.ProStore;
import com.indusfo.repertorymanage.bean.ProStoreD;
import com.indusfo.repertorymanage.bean.ProStoreTagVo;
import com.indusfo.repertorymanage.bean.RResult;
import com.indusfo.repertorymanage.bean.SpinnerIdAndValue;
import com.indusfo.repertorymanage.bean.Storeroom;
import com.indusfo.repertorymanage.broadcast.ScannerInterface;
import com.indusfo.repertorymanage.broadcast.ScannerResultReceiver;
import com.indusfo.repertorymanage.cons.AppParams;
import com.indusfo.repertorymanage.cons.IdiyMessage;
import com.indusfo.repertorymanage.controller.PutInController;
import com.indusfo.repertorymanage.fragment.HuiZongFragment;
import com.indusfo.repertorymanage.fragment.MingXiFragment;
import com.indusfo.repertorymanage.mlayout.TopBar;
import com.indusfo.repertorymanage.utils.ActivityUtils;
import com.indusfo.repertorymanage.utils.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 出库
 *
 * @param
 * @author xuz
 * @date 2019/5/20 3:27 PM
 * @return
 */
public class PutOutActivity extends BaseActivity {

    FragmentManager fm;
    Bundle bundle;

    ScannerInterface scanner;
    IntentFilter intentFilter;
    BroadcastReceiver scanReceiver;

    private static Bundle outState;
    ArrayAdapter<SpinnerIdAndValue> adapter1;
    ArrayAdapter<SpinnerIdAndValue> adapter2;

    // 入库标志
    private static final Integer PUT_IN_STORE_MARK = 1;
    // 出库标志
    private static final Integer PUT_OUT_STORE_MARK = 2;
    // 库房库位集合
    List<Storeroom> storeroomList = new ArrayList<>();
    List<Location> locationList = new ArrayList<>();
    // 产品
    String lProduct = "";
    String vcModel = "";
    // 库房
    Integer lStoreId;
    String vcStoreName = "";
    int storePostion;
    // 库位
//    String lLocationId = "";
    String vcLocationName = "";
    int locationPostion;

    // 明细集合
    List<ProStoreD> proStoreDList = new ArrayList<>();
    // 汇总集合
    List<Gather> gatherList = new ArrayList<>();

    @BindView(R.id.put_in_switch2)
    RelativeLayout putInSwitch;
    @BindView(R.id.ming_xi2)
    TextView mingXi;
    @BindView(R.id.hui_zong2)
    TextView huiZong;
    @BindView(R.id.tiao_ma2)
    TextView tiaoMa;
    @BindView(R.id.xing_hao2)
    TextView xingHao;
    @BindView(R.id.spq2)
    EditText spq;
    @BindView(R.id.ding_dan2)
    EditText dingDan;
    @BindView(R.id.ku_wei2)
    Spinner kuWei2;
    @BindView(R.id.ku_fang2)
    Spinner kuFang2;
    @BindView(R.id.put_out_activity_topbar)
    TopBar putOutActivityTopbar;
    @BindView(R.id.keyboard2)
    ImageView keyboard;

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what) {
            case IdiyMessage.QUERY_STORE_LOCATION_RESULT:
                handleLocationQueryResult(msg);
                break;
            case IdiyMessage.AFTER_SCANNING:
                handleAfterScanning(msg);
                break;
            case IdiyMessage.INSERT_PUT_IN_RESULT:
                handleInsertPutIn(msg);
                break;
            case IdiyMessage.QUERY_BY_TIAO_MA_RESULT:
                handleQueryByTiaoMaResult(msg);
                break;
            default:
                break;
        }
    }

    /**
     * 获取条码扫描结果，查询条码的关联信息
     *
     * @param [msg]
     * @return void
     * @author xuz
     * @date 2019/6/3 9:00 AM
     */
    private void handleQueryByTiaoMaResult(Message msg) {
        RResult rResult = (RResult) msg.obj;
        if (null == rResult) {
            tip("网络错误，服务器连接断开");
            return;
        }
        String code = rResult.getCode();
        String rResultMsg = rResult.getMsg();
        String data = rResult.getData();
        if (!code.isEmpty() && "200".equals(code)) {
            if (null != data && !data.isEmpty()) {
                ProStoreTagVo proStoreTagVo = JSON.parseObject(data, ProStoreTagVo.class);
                // 获取信息
                lProduct = proStoreTagVo.getlProduct();
                vcModel = proStoreTagVo.getVcModel();
                String vcOrder = proStoreTagVo.getVcOrder();
                Integer vcNum = proStoreTagVo.getVcNum();

                if (vcModel != null && !vcModel.isEmpty()) {
                    xingHao.setText(vcModel);
                }
                if (null != vcOrder && !vcOrder.isEmpty()) {
                    dingDan.setText(vcOrder);
                    // 设置输入框光标到文本末尾
                    dingDan.setSelection(dingDan.getText().length());
                }
                if (null != vcNum) {
                    spq.setText(vcNum + "");
                    // 设置输入框光标到文本末尾
                    spq.setSelection(spq.getText().length());
                }

            }
            // 如果数据完整，则新增行并更新UI
            String spqStr = spq.getText().toString();
            String dingDanStr = dingDan.getText().toString();
            if (vcModel != null && !vcModel.isEmpty()
                    && !spqStr.isEmpty() && !dingDanStr.isEmpty()
                    && null != lStoreId && null != lLocationId) {
                addRowAndUpdateUI();
            } else {
                tip("请填写完出库信息，再进行扫码");
            }
        } else {
            // 提示错误信息
            if (!rResultMsg.isEmpty()) {
                tip(rResultMsg);
            }
        }
    }

    /**
     * 新增入库信息
     *
     * @param [msg]
     * @return void
     * @author xuz
     * @date 2019/6/1 7:21 PM
     */
    private void handleInsertPutIn(Message msg) {
        RResult rResult = (RResult) msg.obj;
        if (null == rResult) {
            tip("网络错误，服务器连接断开");
            return;
        }
        String code = rResult.getCode();
        String rResultMsg = rResult.getMsg();
        String data = rResult.getData();
        if (!code.isEmpty() && "200".equals(code)) {
            tip("保存成功");
            // 清空数据
            proStoreDList.clear();
            gatherList.clear();
            tiaoMa.setText("");
            lProduct = null;
            vcModel = "";
            xingHao.setText("");
            spq.setText("");
            dingDan.setText("");
            kuWei2.setAdapter(adapter2);
            kuWei2.setSelection(0, true);

            MingXiFragment mingXiFragment = (MingXiFragment) fm.findFragmentByTag("left");
            initMingXiData(mingXiFragment, proStoreDList);
            mingXiFragment.UiChange();

            HuiZongFragment huiZongFragment = (HuiZongFragment) fm.findFragmentByTag("right");
            initHuiZongData(huiZongFragment, gatherList);
            huiZongFragment.UiChange();
        } else {
            // 提示错误信息
            if (!rResultMsg.isEmpty()) {
                tip(rResultMsg);
            }
        }
    }

    /**
     * 扫码成功后，生成一行明细数据
     *
     * @param []
     * @return void
     * @author xuz
     * @date 2019/6/1 9:42 AM
     */
    private void handleAfterScanning(Message msg) {
        String scanResult = (String) msg.obj;

        if (null==lStoreId || lStoreId.isEmpty() || null==lLocationId || lLocationId.isEmpty()) {
            tip("请先选择库房库位，再进行扫码");
            return;
        }

        // 条码不可重复
        for (ProStoreD proStoreD : proStoreDList) {
            if (scanResult.equals(proStoreD.getVcProStoreCode())) {
                tip("条码已扫描过，请不要重复扫描");
                return;
            }
        }

        tiaoMa.setText(scanResult);

        // 发送请求，根据条码查询连带数据
        mController.sendAsynMessage(IdiyMessage.QUERY_BY_TIAO_MA, scanResult, PUT_OUT_STORE_MARK + "");

    }

    /**
     * 扫描结果且完整，新增一行，并且更新UI
     *
     * @return void
     * @author xuz
     * @date 2019/6/3 9:33 AM
     */
    private void addRowAndUpdateUI() {
        String s = spq.getText().toString();
        Integer num;
        if (StringUtils.isInteger(s)) {
            num = Integer.valueOf(s);
        } else {
            tip("SPQ数值格式错误");
            return;
        }

        ProStoreD proStoreD = new ProStoreD();
        proStoreD.setlStoreId(lStoreId);
        proStoreD.setlLocaId(lLocationId);
        proStoreD.setlProduct(lProduct);
        proStoreD.setVcModel(vcModel);
        proStoreD.setVcNum(s);
        proStoreD.setVcOrder(dingDan.getText().toString());
        proStoreD.setVcProStoreCode(tiaoMa.getText().toString());
        proStoreD.setVcStoreName(vcStoreName);
        proStoreD.setVcLocaName(vcLocationName);
        proStoreDList.add(proStoreD);

        // 数据汇总
        if (gatherList.size() == 0) {
            Gather gather = new Gather();
            gather.setVcModel(vcModel);
            gather.setCartons(1);
            gather.setSum(num);
            gatherList.add(gather);
        } else {
            for (Iterator<Gather> iterator = gatherList.iterator(); iterator.hasNext(); ) {
                Gather gather = iterator.next();
                Integer sum = gather.getSum();
                Integer cartons = gather.getCartons();
                // 相同产品型号则进行汇总
                if (vcModel.equals(gather.getVcModel())) {
                    gather.setSum(sum + num);
                    gather.setCartons(cartons + 1);
                    break;
                }
                // 遍历到最后一条仍然没有相同的型号，则新增
                if (!iterator.hasNext()) {
                    Gather gather1 = new Gather();
                    gather1.setVcModel(vcModel);
                    gather1.setSum(num);
                    gather1.setCartons(1);
                    gatherList.add(gather1);
                }
                continue;
            }
        }

        MingXiFragment mingXiFragment = (MingXiFragment) fm.findFragmentByTag("left");
        // 数据传递给fragment中处理
        initMingXiData(mingXiFragment, proStoreDList);
        // 通知fragment UI更新
        mingXiFragment.UiChange();

        HuiZongFragment huiZongFragment = (HuiZongFragment) fm.findFragmentByTag("right");
        initHuiZongData(huiZongFragment, gatherList);
        // 通知fragment UI更新
        huiZongFragment.UiChange();
    }

    /**
     * 库房库位查询结果
     *
     * @param [msg]
     * @return void
     * @author xuz
     * @date 2019/5/31 10:06 AM
     */
    private void handleLocationQueryResult(Message msg) {
        RResult rResult = (RResult) msg.obj;
        if (null == rResult) {
            tip("网络错误，服务器连接断开");
            return;
        }
        String code = rResult.getCode();
        String rResultMsg = rResult.getMsg();
        String data = rResult.getData();
        if (!code.isEmpty() && "200".equals(code)) {
            if (data != null && !data.isEmpty()) {
                storeroomList = JSON.parseArray(data, Storeroom.class);
                // Spinner列表设置
                SpinnerView();
                SpinnerView2();

                // 回显库房库位
                if (outState != null) {
                    kuFang2.setAdapter(adapter1);
                    kuFang2.setSelection(storePostion, true);
                    SpinnerView2();
                    kuWei2.setAdapter(adapter2);
                    kuWei2.setSelection(locationPostion, true);

                    MingXiFragment mingXiFragment = (MingXiFragment) fm.findFragmentByTag("left");
                    // 数据传递给fragment中处理
                    initMingXiData(mingXiFragment, proStoreDList);
                    // 通知fragment UI更新
                    mingXiFragment.UiChange();

                    HuiZongFragment huiZongFragment = (HuiZongFragment) fm.findFragmentByTag("right");
                    initHuiZongData(huiZongFragment, gatherList);
                    // 通知fragment UI更新
                    huiZongFragment.UiChange();
                }
            }
        } else {
            // 提示错误信息
            if (!rResultMsg.isEmpty()) {
                tip(rResultMsg);
            }
        }
    }

    /**
     * Spinner列表展现，Spinner点击事件
     *
     * @param []
     * @return void
     * @author xuz
     * @date 2019/5/31 7:55 PM
     */
    private void SpinnerView() {
        // --------------------------- 库房Spinner -----------------------//
        // 封装Spinner列表集合
        final List<SpinnerIdAndValue> spinnerIdAndValueList1 = new ArrayList<>();
        for (Storeroom storeroom : storeroomList) {
            String storeId = storeroom.getlStoreId();
            String vcStoreName = storeroom.getVcStoreName();
            // 现在只有成品库
            if (vcStoreName.equals("成品库")) {
                SpinnerIdAndValue spinnerIdAndValue = new SpinnerIdAndValue(storeId, vcStoreName);
                spinnerIdAndValueList1.add(spinnerIdAndValue);
                locationList = storeroom.getListLocation();
//                lStoreId = storeId;
                // 库位Spinner生成
//                SpinnerView2();
                break;
            }
        }
        adapter1 =
                new ArrayAdapter<SpinnerIdAndValue>(this, android.R.layout.simple_spinner_item, spinnerIdAndValueList1);
        // 设置下拉列表风格
        adapter1.setDropDownViewResource(R.layout.auto_complete_item);
        // 将adapter添加到spinner中
        kuFang2.setAdapter(adapter1);
        // 设置默认值
        kuFang2.setVisibility(View.VISIBLE);

        kuFang2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int postion, long id) {
                // 现在只有成品库

                // 获取当前的库位名和ID
                vcStoreName = spinnerIdAndValueList1.get(postion).getValue();
                lStoreId = spinnerIdAndValueList1.get(postion).getId();

                // 给库位设置ID
                if (null != lStoreId && !lStoreId.isEmpty()) {
                    kuFang2.setId(Integer.valueOf(lStoreId));
                }

                // 选择库房后，可以确定出库位
//                locationList = storeroomList.get(postion).getListLocation();
                // 库位Spinner生成
//                SpinnerView2();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


    }

    /**
     * 库位Spinner，Spinner点击事件
     *
     * @param []
     * @return void
     * @author xuz
     * @date 2019/6/3 11:02 AM
     */
    private void SpinnerView2() {
        // --------------------------- 库位Spinner -----------------------//
        // 封装Spinner列表集合
        final List<SpinnerIdAndValue> spinnerIdAndValueList2 = new ArrayList<>();
        SpinnerIdAndValue spinnerIdAndValue2 = new SpinnerIdAndValue(null, "请选择");
        spinnerIdAndValueList2.add(spinnerIdAndValue2);
        for (Location location : locationList) {
            String locaId = location.getlLocaId();
            String vcLocaName = location.getVcLocaName();

            SpinnerIdAndValue spinnerIdAndValue = new SpinnerIdAndValue(locaId, vcLocaName);
            spinnerIdAndValueList2.add(spinnerIdAndValue);
        }

        adapter2 =
                new ArrayAdapter<SpinnerIdAndValue>(this, android.R.layout.simple_spinner_item, spinnerIdAndValueList2);
        // 设置下拉列表风格
        adapter2.setDropDownViewResource(R.layout.auto_complete_item);
        // 将adapter添加到spinner中
        kuWei2.setAdapter(adapter2);
        // 设置默认值
        kuWei2.setVisibility(View.VISIBLE);

        kuWei2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int postion, long id) {
                // 获取当前的库位名和ID
                vcLocationName = spinnerIdAndValueList2.get(postion).getValue();
                lLocationId = spinnerIdAndValueList2.get(postion).getId();

                // 给库位设置ID
                if (null != lLocationId && !lLocationId.isEmpty()) {
                    kuWei2.setId(Integer.valueOf(lLocationId));
                    // 没有明细时，在选择库位后，数据完整的情况下自动新增一行明细
                    if (proStoreDList.size()==0 && null!=vcModel && !spq.getText().toString().isEmpty()
                            && !tiaoMa.getText().toString().isEmpty()) {
                        // 如果是第一次，则新增一行数据
//                        addRowAndUpdateUI();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_put_out, null);
        ButterKnife.bind(this, view);
        setContentView(view);

        initController();
        initFragment();
//        initScanner();
        initUI();
    }

    private void initController() {
        mController = new PutInController(this);
        mController.setIModeChangeListener(this);
    }

    private void initFragment() {
        bundle = new Bundle();

        Fragment mingXiFragment = new MingXiFragment();
        initMingXiData(mingXiFragment, proStoreDList);
        Fragment huiZongFragment = new HuiZongFragment();
        initHuiZongData(huiZongFragment, gatherList);

        // 打开页面时，将明细的页面(fragment)添加到activity中
        fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.put_in_switch2, huiZongFragment, "right");
        transaction.add(R.id.put_in_switch2, mingXiFragment, "left");
        transaction.commit();
        // 明细显示，汇总隐藏
        fm.beginTransaction().show(mingXiFragment).commit();
        fm.beginTransaction().hide(huiZongFragment).commit();
        mingXi.setTextColor(getResources().getColor(R.color.textColor7));
    }

    private void initUI() {

        // 库房目前默认设置为成品库
//        vcStoreName = "成品库";
//        lStoreId = 7;
//        kuFang.setText(vcStoreName);

        // 查询库房库位集合
        mController.sendAsynMessage(IdiyMessage.QUERY_STORE_LOCATION);

        putOutActivityTopbar.setOnLeftAndRightClickListener(new TopBar.OnLeftAndRightClickListener() {
            @Override
            public void OnLeftButtonClick() {
                ActivityUtils.start(PutOutActivity.this, MainActivity.class, false);
            }

            @Override
            public void OnRightButtonClick() {
                if (null == proStoreDList || proStoreDList.size() == 0) {
                    tip("保存失败：没有出库明细");
                    return;
                }
                ProStore proStore = new ProStore();
                proStore.setlStoreMark(PUT_OUT_STORE_MARK);
                proStore.setProStoreDList(proStoreDList);
                // 保存入库明细
                mController.sendAsynMessage(IdiyMessage.INSERT_PUT_OUT, proStore);
            }

            @Override
            public void OnThridButtonClick() {

            }
        });

        // 设置文本框不弹出软键盘
        spq.setInputType(InputType.TYPE_NULL);
        dingDan.setInputType(InputType.TYPE_NULL);

        // 订单输入框失去焦点，则隐藏软键盘
        dingDan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                }
            }
        });

        // 如果不为空，回显上个activity的数据
        if (outState != null) {
            tiaoMa.setText(outState.getString("tiaoMa"));
            lProduct = outState.getInt("lProduct");
            vcModel = outState.getString("vcModel");
            xingHao.setText(vcModel);
            spq.setText(outState.getString("spq"));
            // 设置输入框光标到文本末尾
            spq.setSelection(spq.getText().length());
            dingDan.setText(outState.getString("dingDan"));
            // 设置输入框光标到文本末尾
            dingDan.setSelection(dingDan.getText().length());
            storePostion = outState.getInt("storePostion");
            locationPostion = outState.getInt("locationPostion");
            proStoreDList = outState.getParcelableArrayList("proStoreDList");
            gatherList = outState.getParcelableArrayList("gatherList");

        }
        
    }

    /**
     * 回显其他页传来的数据
     *
     * @param [requestCode, resultCode, data]
     * @return void
     * @author xuz
     * @date 2019/5/31 2:56 PM
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case IdiyMessage.QUERY_PRODUCT_2:
                if (null == data) {
                    return;
                }
                Bundle bundle = data.getExtras();
                String str = bundle.getString("lProduct");
                if (null != str && !str.isEmpty()) {
                    lProduct = str;
                }
                vcModel = bundle.getString("vcModel");
                xingHao.setText(vcModel);
                break;
            default:
                break;
        }

    }

    /**
     * 将数据存放在bundle中传递
     *
     * @param [mingXi]
     * @return void
     * @author xuz
     * @date 2019/5/17 9:45 AM
     */
    private void initHuiZongData(Fragment huiZongFragment, List<Gather> gatherList) {
        bundle.putParcelableArrayList("list2", (ArrayList<? extends Parcelable>) gatherList);
        huiZongFragment.setArguments(bundle);
    }

    /**
     * 将数据存放在bundle中传递
     *
     * @param [mingXi]
     * @return void
     * @author xuz
     * @date 2019/5/17 9:45 AM
     */
    private void initMingXiData(Fragment mingXiFragment, List<ProStoreD> proStoreDList) {
        bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) proStoreDList);
        bundle.putParcelableArrayList("list2", (ArrayList<? extends Parcelable>) gatherList);
        mingXiFragment.setArguments(bundle);
    }

    /**
     * 点击键盘图标，弹出软键盘
     *  
     * @author xuz
     * @date 2019/6/5 10:32 AM
     * @param []
     * @return void
     */
    @OnClick(R.id.keyboard2)
    public void onClick() {
        // 取消软键盘的禁用
//                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        // 订单输入框获取焦点
        dingDan.requestFocus();

        // 如果软键盘已弹出则隐藏，反之弹出
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    
    /**
     * 切换到明细
     *
     * @param [view]
     * @return void
     * @author xuz
     * @date 2019/5/17 10:14 AM
     */
    @OnClick(R.id.ming_xi2)
    public void onClickMingXi() {
        Fragment huiZongFragment = fm.findFragmentByTag("right");
        // 隐藏汇总页面
        if (null != huiZongFragment && huiZongFragment.isVisible() && huiZongFragment.isAdded()) {
            fm.beginTransaction().hide(huiZongFragment).commit();
            huiZong.setTextColor(getResources().getColor(R.color.white));
        }

        // 显示明细页面
        Fragment mingXiFragment = fm.findFragmentByTag("left");
        if (null != mingXiFragment && !mingXiFragment.isVisible()) {
            fm.beginTransaction().show(mingXiFragment).commit();
            mingXi.setTextColor(getResources().getColor(R.color.textColor7));
        }

    }

    /**
     * 切换到汇总
     *
     * @param [view]
     * @return void
     * @author xuz
     * @date 2019/5/17 10:07 AM
     */
    @OnClick(R.id.hui_zong2)
    public void onClickHuiZong() {
        Fragment mingXiFragment = fm.findFragmentByTag("left");
        // 如果明细页面显示中，则隐藏
        if (null != mingXiFragment && mingXiFragment.isVisible() && mingXiFragment.isAdded()) {
            fm.beginTransaction().hide(mingXiFragment).commit();
            mingXi.setTextColor(getResources().getColor(R.color.white));
        }

        // 如果汇总页面存在则显示，否则添加到activity中
        HuiZongFragment huiZongFragment = (HuiZongFragment) fm.findFragmentByTag("right");
        if (null != huiZongFragment && !huiZongFragment.isVisible()) {
            initHuiZongData(huiZongFragment, gatherList);
            huiZongFragment.UiChange();
            fm.beginTransaction().show(huiZongFragment).commit();
            huiZong.setTextColor(getResources().getColor(R.color.textColor7));
        }
//        else {
//            huiZongFragment = new HuiZongFragment();
//            initHuiZongData(huiZongFragment);
//            FragmentTransaction transaction = fm.beginTransaction();
//            transaction.add(R.id.put_in_switch, huiZongFragment, "right");
//            transaction.commit();
//            mingXi.setTextColor(getResources().getColor(R.color.white));
//        }

    }

    /**
     * 跳转到产品查询界面
     *
     * @param []
     * @return void
     * @author xuz
     * @date 2019/5/31 9:59 AM
     */
    @OnClick(R.id.xing_hao2)
    public void onTextChangedMingXi() {
        Intent intent = new Intent(PutOutActivity.this, ProductQueryActivity.class);
        startActivityForResult(intent, IdiyMessage.QUERY_PRODUCT_2);
    }

    // -------------------------------------- 扫描功能区 ----------------------------------------

    /**
     * 扫描初始化
     *
     * @param []
     * @return void
     * @author xuz
     * @date 2019/5/30 3:53 PM
     */
    private void initScanner() {
        scanner = new ScannerInterface(this);

        /**设置扫描结果的输出模式，参数为0和1：
         * 0为模拟输出，同时广播扫描数据（在光标停留的地方输出扫描结果同时广播扫描数据）;
         * 1为广播输出（只广播扫描数据）；
         * 2为模拟按键输出；
         * */
        scanner.setOutputMode(1);

        //扫描失败蜂鸣反馈
        scanner.enableFailurePlayBeep(true);

        //扫描结果的意图过滤器action一定要使用"android.intent.action.SCANRESULT"
        intentFilter = new IntentFilter();
        intentFilter.addAction(AppParams.RES_ACTION);

        scanReceiver = new ScannerResultReceiver(this);
        // 注册监听
        ((ScannerResultReceiver) scanReceiver).setIModeChangeListener(this);
        //注册广播接受者
        registerReceiver(scanReceiver, intentFilter);


        //注册广播接受者
//        scanReceiver = new ScannerResultReceiver(this, tiaoMa);
//        registerReceiver(scanReceiver, intentFilter);
    }

    /**
     * 注销广播
     *
     * @param []
     * @return void
     * @author xuz
     * @date 2019/6/1 4:31 PM
     */
//    @Override
//    protected void onDestroy() {
//        //注销广播
//        this.unregisterReceiver(scanReceiver);
//        super.onDestroy();
//    }


    @Override
    protected void onStart() {
        initScanner();
        super.onStart();
    }

    /**
     * 注销广播
     * 因为这里的Activity一直没有销毁，所以还是在onStop中进行注销，
     * 为了不影响到其他界面的扫码功能
     *
     * @author xuz
     * @date 2019/6/10 2:25 PM
     * @param []
     * @return void
     */
    @Override
    protected void onStop() {
        //注销广播
        this.unregisterReceiver(scanReceiver);
        super.onStop();
    }

    // -------------------------------------- 扫描功能区 ----------------------------------------


    @Override
    protected void onPause() {
        super.onPause();
        if (outState != null) {
            outState.clear();
            outState = null;
        }
        outState = new Bundle();

        if (null!=lProduct) {
            outState.putString("lProduct", lProduct);
        }
        outState.putString("vcModel", vcModel);
        outState.putString("tiaoMa", tiaoMa.getText().toString());
        outState.putString("spq", spq.getText().toString());
        outState.putString("dingDan", dingDan.getText().toString());
        if (null!=lStoreId) {
            outState.putInt("storePostion", kuFang2.getSelectedItemPosition());
        }
        if (null!=lLocationId) {
            outState.putInt("locationPostion", kuWei2.getSelectedItemPosition());
        }
        outState.putParcelableArrayList("proStoreDList", (ArrayList<? extends Parcelable>) proStoreDList);
        outState.putParcelableArrayList("gatherList", (ArrayList<? extends Parcelable>) gatherList);
    }
}
