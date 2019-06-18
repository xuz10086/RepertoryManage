package com.indusfo.repertorymanage.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.indusfo.repertorymanage.R;
import com.indusfo.repertorymanage.utils.AppUtils;
import com.indusfo.repertorymanage.utils.PreferenceUtils;
import com.indusfo.repertorymanage.utils.SDCardUtils;
import com.indusfo.repertorymanage.utils.UpdateVersionUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author wenjie
 *    下载新版本的服务类
 */
public class UpdateVersionService extends Service {


    private NotificationManager nm;
    private Notification notification;
    //标题标识
    private int titleId = 0;
    //安装文件
    private File updateFile;

    private static HttpHandler<File> httpHandler;
    private HttpUtils httpUtils;

    private long initTotal = 0;//文件的总长度


    private String id = "id";
    private String name = "name";

    @Override
    public void onCreate() {
        super.onCreate();

        httpUtils = new HttpUtils();
//        updateFile = new File(getCacheDir()+"/downloadApk","app-release.apk");   pda这个路径在安装时会安装包解析失败
        updateFile = new File(SDCardUtils.getRootDirectory()+"/downloadApk/app-release.apk");

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            nm.createNotificationChannel(mChannel);

            notification = new Notification.Builder(this)
            .setChannelId(id)
            .setContentTitle("开始下载")
            .setContentText("下载进度")
            .setSmallIcon(R.mipmap.ic_launcher).build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, id)
                    .setContentTitle("开始下载")
                    .setContentText("下载进度")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setOngoing(true)
                    .setChannelId(id);
            notification = notificationBuilder.build();
        }

//        notification = new Notification();
//        notification.icon = R.mipmap.ic_launcher;
//        notification.tickerText = "开始下载";
//        notification.when = System.currentTimeMillis();
        notification.contentView = new RemoteViews(getPackageName(), R.layout.notifycation);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        VersionInfo versionInfo = (VersionInfo) intent.getSerializableExtra("versionInfo");
//        String url = versionInfo.getDownloadUrl();
        Bundle bundle = intent.getExtras();
        String url = bundle.getString("downloadUrl");

        PreferenceUtils.setString(UpdateVersionService.this, "apkDownloadurl", url);

        nm.notify(titleId, notification);
        downLoadFile(url);

        // 检查并申请权限
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            //申请WRITE_EXTERNAL_STORAGE权限
//            ActivityCompat.requestPermissions(LoginActivity., new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_TASK_CODE);
//        }
        return super.onStartCommand(intent, flags, startId);
    }



    public void downLoadFile(String url){

        if (!updateFile.exists()) {
            try {
                updateFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        httpHandler = httpUtils.download(url,updateFile.getAbsolutePath(), true, false, new RequestCallBack<File>() {

            @Override
            public void onSuccess(ResponseInfo<File> response) {
                // 更改文字
                notification.contentView.setTextViewText(R.id.msg, "下载完成!点击安装");
//                notification.contentView.setViewVisibility(R.id.btnStartStop, View.GONE);
//                notification.contentView.setViewVisibility(R.id.btnCancel,View.GONE);
                // 发送消息
                nm.notify(titleId, notification);
                stopSelf();
                //收起通知栏
                UpdateVersionUtil.collapseStatusBar(UpdateVersionService.this);
                //自动安装新版本
//                ApkUtils.installAPk(UpdateVersionService.this, updateFile);
                Intent installIntent = AppUtils.getInstallIntent(updateFile);
                startActivity(installIntent);

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                //网络连接错误
                if(error.getExceptionCode() == 0 ){
                    // 更改文字
                    notification.contentView.setTextViewText(R.id.msg, "网络异常！请检查网络设置！");
                }else if(error.getExceptionCode() == 416){//文件已经下载完毕
                    // 更改文字
                    notification.contentView.setTextViewText(R.id.msg, "正在下载");
                    // 更改文字
                    notification.contentView.setTextViewText(R.id.bartext, "检测到新版本已经下载完成，点击即安装!");
                    // 隐藏进度条
                    notification.contentView.setViewVisibility(R.id.progressBar1, View.GONE);

                    Intent intent = AppUtils.getInstallIntent(updateFile);
                    PendingIntent pendingIntent = PendingIntent.getActivity(UpdateVersionService.this, 0, intent, 0);
                    notification.flags = Notification.FLAG_AUTO_CANCEL;//点击通知栏之后 消失
                    notification.contentIntent  = pendingIntent;//启动指定意图
                }
                // 发送消息
                nm.notify(titleId, notification);
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                if(initTotal == 0){//说明第一次开始下载
                    initTotal = total;
                }

                if(initTotal != total){//说明下载过程中暂停过，文件的总长度出现问题  就把初始的文件的长度赋值给他重新计算已经下载的比例
                    total = initTotal;
                }

                long l = current*100/total;
                notification.contentView.setTextViewText(R.id.msg, "正在下载：xxx");
                // 更改文字
                notification.contentView.setTextViewText(R.id.bartext, l+ "%");
                // 更改进度条
                notification.contentView.setProgressBar(R.id.progressBar1, 100,(int)l, false);
                // 发送消息
                nm.notify(titleId, notification);

//              Intent intent = new Intent();
//                intent.setAction("cancel");
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
//                notification.contentView.setOnClickPendingIntent(R.id.btnStartStop, pendingIntent);

            }

            @Override
            public void onStart() {
                notification.contentView.setTextViewText(R.id.msg, "开始下载：");
                nm.notify(titleId, notification);
            }

        });
    }


    public static HttpHandler<File> getHandler(){
        return httpHandler;
    }


    @Override
    public void onDestroy() {
        //下载完成时，清楚该通知，自动安装
        nm.cancel(titleId);
        System.out.println("UpdateVersionService----onDestroy");
//        try {
//            GdmsaecApplication.db.deleteAll(VersionInfo.class);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
