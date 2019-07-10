package com.indusfo.repertorymanage.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Message;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.indusfo.repertorymanage.R;
import com.indusfo.repertorymanage.bean.RResult;
import com.indusfo.repertorymanage.bean.User;
import com.indusfo.repertorymanage.bean.VersionInfo;
import com.indusfo.repertorymanage.cons.IdiyMessage;
import com.indusfo.repertorymanage.cons.UpdateStatus;
import com.indusfo.repertorymanage.controller.LoginController;
import com.indusfo.repertorymanage.utils.ActivityUtils;
import com.indusfo.repertorymanage.utils.Md5Util;
import com.indusfo.repertorymanage.utils.SDCardUtils;
import com.indusfo.repertorymanage.utils.ToastUtils;
import com.indusfo.repertorymanage.utils.UpdateVersionUtil;

public class LoginActivity extends BaseActivity {


    /*
     * UI 界面
     */
    private EditText mUsernameView;
    private EditText mPasswordView;
    // 勾选框
    private CheckBox checkBox;
    // 设置设备号
    private ImageView settinUrl;

    // App更新
    private TextView updateApp, version;

    // 示例服务器地址
    private static final String EXAMPLE_URL = "36.155.115:37:8181";

    EditText tv;

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what) {
            case IdiyMessage.LOGIN_ACTION_RESULT:
                handleLoginResult(msg);
                break;
            case IdiyMessage.SAVE_USER_TODB_RESULT:
                handleSaveUserToDb((boolean) msg.obj);
                break;
            case IdiyMessage.GET_USER_FROM_DB_RESULT:
                handleGetUser(msg.obj);
                break;
            case IdiyMessage.GET_URL_FROM_LOCAL_RESULT:
                handleSetEditText(msg.obj);
            default:
                break;
        }
    }

    private void handleSetEditText(Object obj) {
        String url = (String) obj;
        tv.setText(url);
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("请输入URL");
        builder.setMessage("例如：" + EXAMPLE_URL);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setView(tv);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mController.sendAsynMessage(IdiyMessage.SETTING_URL, tv.getText().toString());
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();

    }

    private void handleGetUser(Object o) {
        if (o != null) {
            User user = (User) o;
            // 判断ifsave是否为1:勾选了记住密码
            if (user.getIfsave() == 1) {
                checkBox.setChecked(true);
                mUsernameView.setText(user.getUsername());
                mPasswordView.setText(user.getPassword());
            }
        }
    }

    private void handleSaveUserToDb(boolean ifSuccess) {
        if (ifSuccess) {
            ActivityUtils.start(this, MainActivity.class, true);
        } else {
            tip("登录异常");
        }
    }

    private void handleLoginResult(Message msg) {
        RResult rResult = (RResult) msg.obj;
        if (null==rResult) {
            tip("网络连接错误");
            return;
        }
        // 状态为200，跳转到主页
        if ("200".equals(rResult.getCode())) {

            // 保存用户信息
            String username = mUsernameView.getText().toString();
            String password = mPasswordView.getText().toString();
            String cookie = rResult.getCookie();
            int ifsave;
            if (checkBox.isChecked()) {
                ifsave = 1;
            } else {
                ifsave = 0;
            }
            String userId = "111";

            mController.sendAsynMessage(IdiyMessage.SAVE_USER_TODB, username, password, ifsave, userId);

            mController.sendAsynMessage(IdiyMessage.SAVE_COOKIE, cookie);

        } else{
            mPasswordView.setError("用户名或密码错误！");
        }

        if ("erro url".equals(rResult.getCode())) {
            tip("服务器地址错误");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initController();
        initUI();

        // 如果存在安装包，则删除
        String filePath = SDCardUtils.getRootDirectory()+"/downloadApk/app-release.apk";
        SDCardUtils.removeFile(filePath);

    }

    private void initController() {

        mController = new LoginController(this);
        mController.setIModeChangeListener(this);
    }

    private void initUI() {
        // 用户名文本输入框
        mUsernameView = (EditText) findViewById(R.id.email);
        // Password文本输入框
        mPasswordView = (EditText) findViewById(R.id.password);
        checkBox = findViewById(R.id.checkbox_pwd);
        settinUrl = findViewById(R.id.setting_url);
        // app更新
        updateApp = findViewById(R.id.update_app);
        // app版本显示
        version = findViewById(R.id.version);

        tv = new EditText(LoginActivity.this);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;


        // 获取版本号并设置
//        String versionName = AppUtils.getVersionName(LoginActivity.this);
//        version.setText(versionName);

        // 给Password文本框添加监听事件
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    //attemptLogin();
                    return true;
                }
                return false;
            }
        });

        // 点击后，编辑URL地址
        settinUrl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mController.sendAsynMessage(IdiyMessage.GET_URL_FROM_LOCAL);
                // 上一个编辑框已经存在了parent，重新创建一个编辑框
                tv = new EditText(LoginActivity.this);
            }
        });

        mController.sendAsynMessage(IdiyMessage.GET_USER_FROM_DB, 0);

        // 进入登陆界面，校验是否有新版本
        UpdateVersionUtil.checkVersion(LoginActivity.this, new UpdateVersionUtil.UpdateListener() {

            @Override
            public void onUpdateReturned(int updateStatus, VersionInfo versionInfo) {
                //判断回调过来的版本检测状态
                switch (updateStatus) {
                    case UpdateStatus.YES:
                        //弹出更新提示
                        UpdateVersionUtil.showDialog(LoginActivity.this, versionInfo);
                        break;
                    default:
                        break;
                }
            }
        });

        // 更新App
        updateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AppUtils.verifyStoragePermissions(LoginActivity.this);
                //访问服务器 试检测是否有新版本发布
                UpdateVersionUtil.checkVersion(LoginActivity.this, new UpdateVersionUtil.UpdateListener() {

                    @Override
                    public void onUpdateReturned(int updateStatus, VersionInfo versionInfo) {
                        //判断回调过来的版本检测状态
                        switch (updateStatus) {
                            case UpdateStatus.YES:
                                //弹出更新提示
                                UpdateVersionUtil.showDialog(LoginActivity.this, versionInfo);
                                break;
                            case UpdateStatus.NO:
                                //没有新版本
                                ToastUtils.showToast(getApplicationContext(), "已经是最新版本了!");
                                break;
                            case UpdateStatus.NOWIFI:
                                //当前是非wifi网络
                                ToastUtils.showToast(getApplicationContext(), "只有在wifi下更新！");
                                break;
                            case UpdateStatus.ERROR:
                                //检测失败
                                ToastUtils.showToast(getApplicationContext(), "检测失败，请稍后重试！");
                                break;
                            case UpdateStatus.TIMEOUT:
                                //链接超时
                                ToastUtils.showToast(getApplicationContext(), "链接超时，请检查网络设置!");
                                break;
                        }
                    }

                });
            }
        });

    }

    /**
     * 点击登录按钮
     * 输入框值判断，发送网络请求来请求服务器
     *
     * @author xuz
     * @date 2019/1/4 9:20 AM
     * @param [view]
     * @return void
     */
    public void loginClick(View view) {

        // 在尝试登录时存储值。
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        // 加密
        String md5pwd = Md5Util.md5(password);
        if (ifValueWasEmpty(username, password)) {
            tip("请输入账号密码");
        }

        // 发送网络请求
        mController.sendAsynMessage(IdiyMessage.LOGIN_ACTION, username, md5pwd);
    }
}
