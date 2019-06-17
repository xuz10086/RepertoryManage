package com.indusfo.repertorymanage.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.indusfo.repertorymanage.R;
import com.indusfo.repertorymanage.mlayout.TopBar;
import com.indusfo.repertorymanage.utils.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;

    @BindView(R.id.main_activity_topbar)
    TopBar mainActivityTopbar;
    @BindView(R.id.put_out)
    LinearLayout putOut;
    @BindView(R.id.put_in)
    LinearLayout putIn;
    @BindView(R.id.take_stock)
    LinearLayout takeStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        ButterKnife.bind(this, view);
        setContentView(view);

        initUI();
    }

    private void initUI() {

    }

    @OnClick({R.id.put_out, R.id.put_in, R.id.take_stock})
    public void onClick(View view) {
        switch (view.getId()) {
            // 跳转到出库界面
            case R.id.put_out:
                ActivityUtils.start(this, PutOutActivity.class, false);
                break;
            // 跳转到入库界面
            case R.id.put_in:
                ActivityUtils.start(this, PutInActivity.class, false);
                break;
            // 跳转到盘点界面
            case R.id.take_stock:
                ActivityUtils.start(this, TakeStockActivity.class, false);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                tip("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
