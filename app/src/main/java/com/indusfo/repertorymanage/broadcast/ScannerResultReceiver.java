package com.indusfo.repertorymanage.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.indusfo.repertorymanage.activity.PutInActivity;
import com.indusfo.repertorymanage.cons.AppParams;
import com.indusfo.repertorymanage.cons.IdiyMessage;
import com.indusfo.repertorymanage.listener.IModeChangeListener;

/**
 * 扫描结果广播接受
 *
 * @author xuz
 * @date 2019/5/30 3:55 PM
 * @param
 * @return
 */
public class ScannerResultReceiver extends BroadcastReceiver {
    Context mContext;
    IModeChangeListener mListener;

    public ScannerResultReceiver(Context c) {
        this.mContext = c;
    }

    // 注册监听
    public void setIModeChangeListener (IModeChangeListener iModeChangeListener) {
        this.mListener = iModeChangeListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // 接受扫描结果
        final String scanResult = intent.getStringExtra("value");

        if (intent.getAction().equals(AppParams.RES_ACTION)) {
            // 获取扫描结果
            if (scanResult.length()>0) {
//                //如果条码长度>0，解码成功。如果条码长度等于0解码失败。
//                tvScanResult.setText(scanResult + "\n");
                mListener.onModeChange(IdiyMessage.AFTER_SCANNING, scanResult);
            } else {
                /**扫描失败提示使用有两个条件：
                 1，需要先将扫描失败提示接口打开只能在广播模式下使用，其他模式无法调用。
                 2，通过判断条码长度来判定是否解码成功，当长度等于0时表示解码失败。
                 * */
                Toast.makeText(mContext, "解码失败！", Toast.LENGTH_SHORT).show();
            }
        }

        // 注销广播
//        context.unregisterReceiver(this);
    }
}
