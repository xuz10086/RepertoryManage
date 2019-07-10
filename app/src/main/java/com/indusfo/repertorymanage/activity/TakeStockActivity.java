package com.indusfo.repertorymanage.activity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.indusfo.repertorymanage.R;
import com.indusfo.repertorymanage.bean.CheckD;
import com.indusfo.repertorymanage.bean.Location;
import com.indusfo.repertorymanage.bean.ProStoreCheck;
import com.indusfo.repertorymanage.bean.ProStoreD;
import com.indusfo.repertorymanage.bean.RResult;
import com.indusfo.repertorymanage.bean.SpinnerIdAndValue;
import com.indusfo.repertorymanage.bean.Storeroom;
import com.indusfo.repertorymanage.broadcast.ScannerInterface;
import com.indusfo.repertorymanage.broadcast.ScannerResultReceiver;
import com.indusfo.repertorymanage.cons.AppParams;
import com.indusfo.repertorymanage.cons.IdiyMessage;
import com.indusfo.repertorymanage.controller.TakeStockController;
import com.indusfo.repertorymanage.mlayout.TopBar;
import com.indusfo.repertorymanage.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TakeStockActivity extends BaseActivity {

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
    // 批号
    String vcPiHao;
    // 产品
    Integer lProduct;
    String vcModel = "";
    // 库房
    Integer lStoreId;
    String vcStoreName = "";
    int storePostion;
    // 库位
    Integer lLocationId;
    String vcLocationName = "";
    int locationPostion;
    // 实际库位ID
    Integer lLocationRealId;
    String vcLocationRealName = "";
    // 数量
    String vcNum = "";

    // 盘点明细集合
    List<CheckD> checkDList = new ArrayList<>();

    @BindView(R.id.take_stock_activity_topbar)
    TopBar takeStockActivityTopBar;
    @BindView(R.id.ku_fang3)
    Spinner kuFang;
    @BindView(R.id.ku_wei3)
    Spinner kuWei;
    @BindView(R.id.pi_hao3)
    TextView piHao;
    @BindView(R.id.ku_wei_real3)
    TextView kuWeiReal;
    @BindView(R.id.xing_hao3)
    TextView xingHao;
    @BindView(R.id.spq3)
    TextView spq;
    @BindView(R.id.scanningTable)
    TableLayout scanningTable;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what) {
            case IdiyMessage.QUERY_STORE_LOCATION_RESULT:
                handleLocationQueryResult(msg);
                break;
            case IdiyMessage.AFTER_SCANNING:
                handleAfterScanning(msg);
                break;
            case IdiyMessage.QUERY_BY_TIAO_MA_RESULT:
                handleQueryByTiaoMa(msg);
                break;
            case IdiyMessage.INSERST_TAKE_STOCK_RESULT:
                handleInsertTakeStockResult(msg);
                break;
            default:
                break;
        }
    }

    /**
     * 盘点明细新增结果
     *
     * @author xuz
     * @date 2019/6/3 5:23 PM
     * @param [msg]
     * @return void
     */
    private void handleInsertTakeStockResult(Message msg) {
        RResult rResult = (RResult) msg.obj;
        if (null==rResult) {
            tip("网络错误，服务器连接断开");
            return;
        }
        String code = rResult.getCode();
        String rResultMsg = rResult.getMsg();
        String data = rResult.getData();
        if (!code.isEmpty() && "200".equals(code)) {
            tip("保存成功");
            // 清空数据
            checkDList.clear();
            scanningTable.removeAllViews();

        } else {
            // 提示错误信息
            if (!rResultMsg.isEmpty()) {
                tip(rResultMsg);
            }
        }
    }

    /**
     * 根据条码查询关联信息
     *  
     * @author xuz
     * @date 2019/6/3 2:16 PM
     * @param [msg]
     * @return void
     */
    private void handleQueryByTiaoMa(Message msg) {
        RResult rResult = (RResult) msg.obj;
        if (null==rResult) {
            tip("网络错误，服务器连接断开");
            return;
        }
        String code = rResult.getCode();
        String rResultMsg = rResult.getMsg();
        String data = rResult.getData();
        if (!code.isEmpty() && "200".equals(code)) {
            if (data!=null && !data.isEmpty()) {
                ProStoreD proStoreD = JSON.parseObject(data, ProStoreD.class);
                if (null==proStoreD) {
                    return;
                }
                // 获取值
                lLocationRealId = proStoreD.getlLocaId();
                vcLocationRealName = proStoreD.getVcLocaName();
                lProduct = proStoreD.getlProduct();
                vcModel = proStoreD.getVcModel();
                vcNum = proStoreD.getVcNum();
                
                // 设置值
                if (null!=vcLocationRealName
                        && !vcLocationRealName.isEmpty()) {
                    kuWeiReal.setText(vcLocationRealName);
                } else {
                    tip("错误：该批号盘点信息未维护库位");
                    return;
                }
                if (null!=vcModel && !vcModel.isEmpty()) {
                    xingHao.setText(vcModel);
                } else {
                    tip("错误：该批号盘点信息未维护产品");
                    return;
                }
                if (null!=vcNum && !vcNum.isEmpty()) {
                    spq.setText(vcNum);
                } else {
                    tip("错误：该批号盘点信息未维护SPQ数量");
                    return;
                }

                // 如果数据都全，新增行
                if (null!=lStoreId && null!=lLocationId) {
                    // 新增行
                    addRow();
                } else {
                    tip("请选择库房库位后，再进行扫码");
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
     * 新增一行表数据
     *
     * @author xuz
     * @date 2019/6/3 3:39 PM
     * @param []
     * @return void
     */
    private void addRow() {
        LayoutInflater inflater = LayoutInflater.from(TakeStockActivity.this);
        final TextView tv1 = inflater.inflate(R.layout.take_stock_table_textview, null).findViewById(R.id.textview1);
        final TextView tv2 = inflater.inflate(R.layout.take_stock_table_textview, null).findViewById(R.id.textview1);
        final TextView tv3 = inflater.inflate(R.layout.take_stock_table_textview, null).findViewById(R.id.textview1);
        final TextView tv4 = inflater.inflate(R.layout.take_stock_table_textview, null).findViewById(R.id.textview1);
        final TextView tv5 = inflater.inflate(R.layout.take_stock_table_textview, null).findViewById(R.id.textview1);

        // 获取行
        final TableRow row = getTableRow();

        // 填入数据
        tv1.setText(vcPiHao);
        tv1.setLines(1);
        tv2.setText(vcModel);
        tv2.setLines(1);
        tv3.setText(vcNum);
        tv3.setLines(1);
        tv4.setText(vcLocationName);
        tv4.setLines(1);
        tv5.setText("删除");
        tv5.setTextColor(getResources().getColor(R.color.red));
        row.addView(tv1);
        row.addView(tv2);
        row.addView(tv3);
        row.addView(tv4);
        row.addView(tv5);

        scanningTable.addView(row, new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));

        // 始终在最底部
        scrollBottom(scrollView, scanningTable);

        // 同时给盘点明细集合中加入数据
        final CheckD checkD = new CheckD();
        checkD.setVcCheckDCode(vcPiHao);
        checkD.setlProduct(lProduct+"");
        checkD.setVcModel(vcModel);
        checkD.setlStoreId(lStoreId);
        checkD.setlCheckLoca(lLocationId);
        checkD.setlReallyLoca(lLocationRealId);
        checkD.setVcNum(vcNum);
        checkD.setVcLocationName(vcLocationName);
        checkDList.add(checkD);

        // 点击后展现未显示完全的数据
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ellipsisCount = tv1.getLayout().getEllipsisCount(tv1.getLineCount() - 1);

                // ellipsisCount>0说明没有显示全部，存在省略部分
                if (ellipsisCount>0) {
                    // 展示全部
                    tv1.setMaxHeight(getResources().getDisplayMetrics().heightPixels);
                } else {
                    // 显示2行，按钮设置为点击显示全部。
                    tv1.setMaxLines(1);
                }
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ellipsisCount = tv2.getLayout().getEllipsisCount(tv2.getLineCount() - 1);

                // ellipsisCount>0说明没有显示全部，存在省略部分
                if (ellipsisCount>0) {
                    // 展示全部
                    tv2.setMaxHeight(getResources().getDisplayMetrics().heightPixels);
                } else {
                    // 显示2行，按钮设置为点击显示全部。
                    tv2.setMaxLines(1);
                }
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ellipsisCount = tv3.getLayout().getEllipsisCount(tv3.getLineCount() - 1);

                // ellipsisCount>0说明没有显示全部，存在省略部分
                if (ellipsisCount>0) {
                    // 展示全部
                    tv3.setMaxHeight(getResources().getDisplayMetrics().heightPixels);
                } else {
                    // 显示2行，按钮设置为点击显示全部。
                    tv3.setMaxLines(1);
                }
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ellipsisCount = tv4.getLayout().getEllipsisCount(tv4.getLineCount() - 1);

                // ellipsisCount>0说明没有显示全部，存在省略部分
                if (ellipsisCount>0) {
                    // 展示全部
                    tv4.setMaxHeight(getResources().getDisplayMetrics().heightPixels);
                } else {
                    // 显示2行，按钮设置为点击显示全部。
                    tv4.setMaxLines(1);
                }
            }
        });

        // 点击删除
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanningTable.removeView(row);

                checkDList.remove(checkD);
            }
        });
    }

    private void addRow2(final CheckD checkD) {
        LayoutInflater inflater = LayoutInflater.from(TakeStockActivity.this);
        final TextView tv1 = inflater.inflate(R.layout.take_stock_table_textview, null).findViewById(R.id.textview1);
        final TextView tv2 = inflater.inflate(R.layout.take_stock_table_textview, null).findViewById(R.id.textview1);
        final TextView tv3 = inflater.inflate(R.layout.take_stock_table_textview, null).findViewById(R.id.textview1);
        final TextView tv4 = inflater.inflate(R.layout.take_stock_table_textview, null).findViewById(R.id.textview1);
        final TextView tv5 = inflater.inflate(R.layout.take_stock_table_textview, null).findViewById(R.id.textview1);

        // 获取行
        final TableRow row = getTableRow();

        // 填入数据
        tv1.setText(vcPiHao);
        tv1.setLines(1);
        tv2.setText(vcModel);
        tv2.setLines(1);
        tv3.setText(vcNum);
        tv3.setLines(1);
        tv4.setText(vcLocationName);
        tv4.setLines(1);
        tv5.setText("删除");
        tv5.setTextColor(getResources().getColor(R.color.red));
        row.addView(tv1);
        row.addView(tv2);
        row.addView(tv3);
        row.addView(tv4);
        row.addView(tv5);

        scanningTable.addView(row, new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));

        // 始终在最底部
        scrollBottom(scrollView, scanningTable);

        // 同时给盘点明细集合中加入数据
//        final CheckD checkD = new CheckD();
//        checkD.setVcCheckDCode(vcPiHao);
//        checkD.setlProduct(lProduct+"");
//        checkD.setVcModel(vcModel);
//        checkD.setlStoreId(lStoreId);
//        checkD.setlCheckLoca(lLocationId);
//        checkD.setlReallyLoca(lLocationRealId);
//        checkD.setVcNum(vcNum);
//        checkDList.add(checkD);

        // 点击后展现未显示完全的数据
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ellipsisCount = tv1.getLayout().getEllipsisCount(tv1.getLineCount() - 1);

                // ellipsisCount>0说明没有显示全部，存在省略部分
                if (ellipsisCount>0) {
                    // 展示全部
                    tv1.setMaxHeight(getResources().getDisplayMetrics().heightPixels);
                } else {
                    // 显示2行，按钮设置为点击显示全部。
                    tv1.setMaxLines(1);
                }
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ellipsisCount = tv2.getLayout().getEllipsisCount(tv2.getLineCount() - 1);

                // ellipsisCount>0说明没有显示全部，存在省略部分
                if (ellipsisCount>0) {
                    // 展示全部
                    tv2.setMaxHeight(getResources().getDisplayMetrics().heightPixels);
                } else {
                    // 显示2行，按钮设置为点击显示全部。
                    tv2.setMaxLines(1);
                }
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ellipsisCount = tv3.getLayout().getEllipsisCount(tv3.getLineCount() - 1);

                // ellipsisCount>0说明没有显示全部，存在省略部分
                if (ellipsisCount>0) {
                    // 展示全部
                    tv3.setMaxHeight(getResources().getDisplayMetrics().heightPixels);
                } else {
                    // 显示2行，按钮设置为点击显示全部。
                    tv3.setMaxLines(1);
                }
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ellipsisCount = tv4.getLayout().getEllipsisCount(tv4.getLineCount() - 1);

                // ellipsisCount>0说明没有显示全部，存在省略部分
                if (ellipsisCount>0) {
                    // 展示全部
                    tv4.setMaxHeight(getResources().getDisplayMetrics().heightPixels);
                } else {
                    // 显示2行，按钮设置为点击显示全部。
                    tv4.setMaxLines(1);
                }
            }
        });

        // 点击删除
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanningTable.removeView(row);

                checkDList.remove(checkD);
            }
        });
    }

    /**
     * 定位到最底部
     *  
     * @author xuz
     * @date 2019/6/3 4:20 PM
     * @param [scrollView, inner]
     * @return void
     */
    private void scrollBottom(final ScrollView scrollView, final View inner) {
        Handler handler = new Handler();
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (scrollView == null || inner == null) {
                    return;
                }
                // 内层高度超过外层
                int offset = inner.getMeasuredHeight()
                        - scrollView.getMeasuredHeight();
                if (offset < 0) {
                    offset = 0;
                }
                scrollView.scrollTo(0, offset);
            }
        });
    }

    /**
     * 设置行格式
     *
     * @author xuz
     * @date 2019/6/3 3:44 PM
     * @param []
     * @return android.widget.TableRow
     */
    @NonNull
    private TableRow getTableRow() {
        // 创建行
        final TableRow row = new TableRow(TakeStockActivity.this);
        row.setGravity(Gravity.CENTER);
        row.setBackgroundColor(Color.WHITE);
        row.setPadding( 1, 1, 1, 1);
        return row;
    }

    /**
     * 扫码成功后
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
        for (CheckD checkD : checkDList) {
            if (scanResult.equals(checkD.getVcCheckDCode())) {
                tip("条码已扫描过，请不要重复扫描");
                return;
            }
        }

        // 中间列表数据清空
        lLocationRealId = null;
        lProduct = null;
        vcModel = "";
        vcNum = "";

        kuWeiReal.setText("");
        xingHao.setText("");
        spq.setText("");
        vcPiHao = scanResult;
        piHao.setText(scanResult);


        // 发送请求，根据条码查询连带数据
        mController.sendAsynMessage(IdiyMessage.QUERY_BY_TIAO_MA, scanResult, lStoreId+"");

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
        if (null==rResult) {
            tip("网络错误，服务器连接断开");
            return;
        }
        String code = rResult.getCode();
        String rResultMsg = rResult.getMsg();
        String data = rResult.getData();
        if (!code.isEmpty() && "200".equals(code)) {
            if (data!=null && !data.isEmpty()) {
                storeroomList = JSON.parseArray(data, Storeroom.class);
                // Spinner列表设置
                SpinnerView();
                SpinnerView2();

                // 回显库房库位
                if (outState != null) {
                    kuFang.setAdapter(adapter1);
                    kuFang.setSelection(storePostion, true);
                    SpinnerView2();
                    kuWei.setAdapter(adapter2);
                    kuWei.setSelection(locationPostion, true);

                    for (CheckD checkD : checkDList) {
                        vcPiHao = checkD.getVcCheckDCode();
                        vcModel = checkD.getVcModel();
                        vcNum = checkD.getVcNum();
                        vcLocationName = checkD.getVcLocationName();

                        addRow2(checkD);
                    }
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
            Integer storeId = storeroom.getlStoreId();
            String vcStoreName = storeroom.getVcStoreName();
            // 现在只有成品库
            if (vcStoreName.equals("成品库")) {
                SpinnerIdAndValue spinnerIdAndValue = new SpinnerIdAndValue(storeId, vcStoreName);
                spinnerIdAndValueList1.add(spinnerIdAndValue);
                locationList = storeroom.getListLocation();
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
        kuFang.setAdapter(adapter1);
        // 设置默认值
        kuFang.setVisibility(View.VISIBLE);

        kuFang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int postion, long id) {
                // 现在只有成品库

                // 获取当前的库位名和ID
                vcStoreName = spinnerIdAndValueList1.get(postion).getValue();
                lStoreId = spinnerIdAndValueList1.get(postion).getId();

                // 给库位设置ID
                if (null != lStoreId) {
                    kuFang.setId(lStoreId);
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
     * @author xuz
     * @date 2019/6/3 11:02 AM
     * @param []
     * @return void
     */
    private void SpinnerView2() {
        // --------------------------- 库位Spinner -----------------------//
        // 封装Spinner列表集合
        final List<SpinnerIdAndValue> spinnerIdAndValueList2 = new ArrayList<>();
        SpinnerIdAndValue spinnerIdAndValue2 = new SpinnerIdAndValue(null, "请选择");
        spinnerIdAndValueList2.add(spinnerIdAndValue2);
        for (Location location : locationList) {
            Integer locaId = location.getlLocaId();
            String vcLocaName = location.getVcLocaName();

            SpinnerIdAndValue spinnerIdAndValue = new SpinnerIdAndValue(locaId, vcLocaName);
            spinnerIdAndValueList2.add(spinnerIdAndValue);
        }

        adapter2 =
                new ArrayAdapter<SpinnerIdAndValue>(this, android.R.layout.simple_spinner_item, spinnerIdAndValueList2);
        // 设置下拉列表风格
        adapter2.setDropDownViewResource(R.layout.auto_complete_item);
        // 将adapter添加到spinner中
        kuWei.setAdapter(adapter2);
        // 设置默认值
        kuWei.setVisibility(View.VISIBLE);

        kuWei.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int postion, long id) {
                // 获取当前的库位名和ID
                vcLocationName = spinnerIdAndValueList2.get(postion).getValue();
                lLocationId = spinnerIdAndValueList2.get(postion).getId();

                // 给库位设置ID
                if (null != lLocationId) {
                    kuWei.setId(lLocationId);
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
        View view = LayoutInflater.from(this).inflate(R.layout.activity_take_stock, null);
        ButterKnife.bind(this, view);
        setContentView(view);

        initController();
//        initScanner();
        initUI();
    }

    private void initController() {
        mController = new TakeStockController(this);
        mController.setIModeChangeListener(this);
    }

    private void initUI() {
        // 查询库房库位集合
        mController.sendAsynMessage(IdiyMessage.QUERY_STORE_LOCATION);

        takeStockActivityTopBar.setOnLeftAndRightClickListener(new TopBar.OnLeftAndRightClickListener() {
            @Override
            public void OnLeftButtonClick() {
                ActivityUtils.start(TakeStockActivity.this, MainActivity.class, false);
            }

            @Override
            public void OnRightButtonClick() {
                if (checkDList.size() == 0) {
                    tip("保存失败：没有盘点明细信息");
                    return;
                }
                ProStoreCheck proStoreCheck = new ProStoreCheck();
                proStoreCheck.setlStoreId(lStoreId);
                proStoreCheck.setlLocaId(lLocationId);
                proStoreCheck.setCheckDList(checkDList);
                proStoreCheck.setlCheckOver(1);
                mController.sendAsynMessage(IdiyMessage.INSERST_TAKE_STOCK, proStoreCheck);
            }

            @Override
            public void OnThridButtonClick() {

            }
        });

        if (outState != null) {
            vcPiHao = outState.getString("piHao");
            piHao.setText(vcPiHao);
            lProduct = outState.getInt("lProduct");
            vcModel = outState.getString("vcModel");
            xingHao.setText(vcModel);
            vcNum = outState.getString("spq");
            spq.setText(vcNum);
            lLocationId = outState.getInt("lLocationRealId");
            vcLocationRealName = outState.getString("vcLocationRealName");
            kuWeiReal.setText(vcLocationRealName);
            storePostion = outState.getInt("storePostion");
            locationPostion = outState.getInt("locationPostion");
            checkDList = outState.getParcelableArrayList("checkDList");
        }
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
            outState.putInt("lProduct", lProduct);
        }
        outState.putString("piHao", piHao.getText().toString());
        if (null!=lLocationRealId) {
            outState.putInt("lLocationRealId",lLocationRealId);
        }
        outState.putString("vcLocationRealName",vcLocationRealName);
        outState.putString("vcModel", vcModel);
        outState.putString("spq", spq.getText().toString());
        if (null!=lStoreId) {
            outState.putInt("storePostion", kuFang.getSelectedItemPosition());
        }
        if (null!=lLocationId) {
            outState.putInt("locationPostion", kuWei.getSelectedItemPosition());
        }
        outState.putParcelableArrayList("checkDList", (ArrayList<? extends Parcelable>) checkDList);
    }
}
