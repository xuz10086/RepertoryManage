package com.indusfo.repertorymanage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.indusfo.repertorymanage.R;
import com.indusfo.repertorymanage.adaptor.AutoCompleteAdapter;
import com.indusfo.repertorymanage.adaptor.ProductQueryAdapter;
import com.indusfo.repertorymanage.bean.Product;
import com.indusfo.repertorymanage.bean.RResult;
import com.indusfo.repertorymanage.cons.AppParams;
import com.indusfo.repertorymanage.cons.IdiyMessage;
import com.indusfo.repertorymanage.controller.ProductQueryController;
import com.indusfo.repertorymanage.mlayout.TopBar;
import com.roamer.slidelistview.SlideListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductQueryActivity extends BaseActivity {

    //下一页初始化为0
    int nextpage = 0;
    //每一页记载多少数据
    private int number = AppParams.PAGESIZE;
    //最多有几页
    private int maxpage = AppParams.MAXPAGE;
    //用来判断是否加载完成
    private boolean loadfinish = true;
    // 用来输入间隔时间
    private long exitTime = 0;

    // 产品集合
    List<Product> productList = new ArrayList<>();
    ArrayAdapter productQueryAdapter;

    @BindView(R.id.product_query_activity_topbar)
    TopBar productQueryActivityTopbar;
    @BindView(R.id.product_model)
    EditText productModel;
    @BindView(R.id.button_clear)
    Button buttonClear;
    @BindView(R.id.product_list_view)
    SlideListView productListView;

    // listview跟脚进度视图
//    private View progressBar;

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what) {
            case IdiyMessage.QUERY_PRODUCT_RESULT:
                handleProductQueryResult(msg);
                break;
            case IdiyMessage.QUERY_PRODUCT_NEXT_PAGE_RESULT:
                handleProductQueryNextPageResult(msg);
                break;
            default:
                break;
        }
    }


    /**
     * 滑动列表，新增查询数据到listView
     *
     * @author xuz
     * @date 2019/5/31 3:32 PM
     * @param [msg]
     * @return void
     */
    private void handleProductQueryNextPageResult(Message msg) {
        RResult rResult = (RResult) msg.obj;
        if (null==rResult) {
            tip("网络错误，服务器连接断开");
            return;
        }
        String code = rResult.getCode();
        String rResultMsg = rResult.getMsg();
        String data = rResult.getData();
        if (!code.isEmpty() && "200".equals(code)) {
            if (!data.isEmpty()) {
                List<Product> list = JSON.parseArray(data, Product.class);
                for (Product product : list) {
                    productList.add(product);
                }
                productQueryAdapter.notifyDataSetChanged();

                loadfinish = true;
                //当下一页的数据加载完成之后移除改视图
//                if (productListView.getFooterViewsCount() != 0) {
//                    productListView.removeFooterView(progressBar);
//                }

            }
        } else {
            // 提示错误信息
            if (!rResultMsg.isEmpty()) {
                if (rResultMsg.equals("没有产品相关数据")) {
                    return;
                }
                tip(rResultMsg);
            }
        }
    }

    /**
     * 产品查询结果
     *
     * @author xuz
     * @date 2019/5/31 10:06 AM
     * @param [msg]
     * @return void
     */
    private void handleProductQueryResult(Message msg) {
        RResult rResult = (RResult) msg.obj;
        if (null==rResult) {
            tip("网络错误，服务器连接断开");
            return;
        }
        String code = rResult.getCode();
        String rResultMsg = rResult.getMsg();
        String data = rResult.getData();
        if (!code.isEmpty() && "200".equals(code)) {
            if (!data.isEmpty()) {
                productList = JSON.parseArray(data, Product.class);
                productQueryAdapter = new ProductQueryAdapter(this,
                        R.layout.auto_complete_item, productList);
                //添加listview的脚跟视图，这个方法必须在listview.setAdapter()方法之前，否则无法显示视图
//                productListView.addFooterView(progressBar);
                productListView.setAdapter(productQueryAdapter);
                productQueryAdapter.notifyDataSetChanged();

                // 点击后，跳转
                productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int postion, long l) {
                        Bundle bundle = new Bundle();
//                        bundle.putInt("which", IdiyMessage.QUERY_PRODUCT);
                        bundle.putString("lProduct", productList.get(postion).getlProduct() + "");
                        bundle.putString("vcModel", productList.get(postion).getVcModel());
                        Intent intent = new Intent();
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
                // 查询到的数据为空时，清空列表
                if (null!=productQueryAdapter && (null==data || data.isEmpty())) {
                    productList.clear();
                    productQueryAdapter.notifyDataSetChanged();
                }
            }
        } else {
            // 提示错误信息
            if (!rResultMsg.isEmpty()) {
                if (rResultMsg.equals("没有产品相关数据")) {
                    return;
                }
                tip(rResultMsg);
            }
            // 查询到的数据为空时，清空列表
            if (null!=productQueryAdapter && (null==data || data.isEmpty())) {
                productList.clear();
                productQueryAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_product_query, null);
        ButterKnife.bind(this, view);
        setContentView(view);

        initController();
        initUI();
    }

    private void initController() {
        mController = new ProductQueryController(this);
        mController.setIModeChangeListener(this);
    }

    private void initUI() {
        // listview中脚跟的视图
//        progressBar = this.getLayoutInflater().inflate(R.layout.progress, null);

        // 查询产品第一页的数据
        mController.sendAsynMessage(IdiyMessage.QUERY_PRODUCT, "", number +"", "1");

        productQueryActivityTopbar.setOnLeftAndRightClickListener(new TopBar.OnLeftAndRightClickListener() {
            @Override
            public void OnLeftButtonClick() {
                finish();
            }

            @Override
            public void OnRightButtonClick() {

            }

            @Override
            public void OnThridButtonClick() {

            }
        });
        

        /**
         * 列表下滑，分页查询后面数据
         *
         * @author xuz
         * @date 2019/5/31 3:55 PM
         * @param [view, firstVisibleItem, visibleItemCount, totalItemCount]
         * @return void
         */
        productListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // 得到listview最后一项的id
                int lastItemId = productListView.getLastVisiblePosition();

                // 判断用户是否滑动到最后一项，因为索引值从零开始所以要加上1
                if((lastItemId+1) == totalItemCount) {
                    /**
                     * 计算当前页，因为每一页只加载十条数据，所以总共加载的数据除以每一页的数据的个数
                     * 如果余数为零则当前页为currentPage=totalItemCount/number；
                     * 如果不能整除则当前页为(int)(totalItemCount/number)+1;
                     * 下一页则是当前页加1
                     */
                    int currentPage = totalItemCount % number;
                    if (currentPage == 0) {
                        currentPage = totalItemCount / number;
                    } else {
                        currentPage = (int) (totalItemCount / number) + 1;
                    }
                    System.out.println("当前页为：" + currentPage);
                    nextpage = currentPage + 1;
                    // 当总共的数据大于0是才加载数据
                    if (totalItemCount > 0) {
                        // 判断当前页是否超过最大页，以及上一页的数据是否加载完成
                        if (loadfinish) {
                            //添加页脚视图
//                            productListView.addFooterView(progressBar);
                            loadfinish = false;

                            String vcModel = productModel.getText().toString();
                            // 获取当前页数据
                            mController.sendAsynMessage(IdiyMessage.QUERY_PRODUCT_NEXT_PAGE,
                                    vcModel, number+"", nextpage+"");
                        }
                    }
                }
            }
        });

        // 输入框设置文本监听
        productModel.addTextChangedListener(textWatcher);
    }

    // 文本内容变化监听
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

        }

        // 监听文本变化，如果有换行符则新增一行数据
        @Override
        public void afterTextChanged(Editable editable) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - exitTime > 1.5) {
                String vcModel = productModel.getText().toString();
                mController.sendAsynMessage(IdiyMessage.QUERY_PRODUCT, vcModel, number +"", "1");
            }
            exitTime = currentTime;
        }
    };

    /**
     * 清空数据
     *  
     * @author xuz
     * @date 2019/6/4 4:50 PM
     * @param []
     * @return void
     */
    @OnClick(R.id.button_clear)
    public void onClick() {
        productModel.setText("");
//        String vcModel = productModel.getText().toString();
//        mController.sendAsynMessage(IdiyMessage.QUERY_PRODUCT, vcModel, number +"", "1");
    }

}
