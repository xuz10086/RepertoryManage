package com.indusfo.repertorymanage.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;

/**
 * 
 * @项目名: 	gdmsaec-app
 * @包名:	com.winfo.gdmsaec.app.utils
 * @类名:	SDCardUtils
 * @创建者:	yanfeijun
 * @创建时间:	2015-10-20	下午3:38:10 
 * @描述:	SD卡工具类
 * 
 * @svn版本:	$Rev$
 * @更新人:	$Author$
 * @更新时间:	$Date$
 * @更新描述:	TODO
 */
public class SDCardUtils {
	
	public static String getPath(){
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	} 
	/**
	 * 获取SD卡的状态
	 * @return
	 */
	public static String getState(){
		return Environment.getExternalStorageState();
	}

	/**
	 * SD卡是否可用
	 * @return 只有当SD卡已经安装并且准备好了才返回true
	 */
	public static boolean isAvailable(){
		return getState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取SD卡的根目录
	 * @return null：不存在SD卡
	 */
	public static File getRootDirectory(){
		return isAvailable()?Environment.getExternalStorageDirectory():null;
	}

	/**
	 * 获取SD卡的根路径
	 * @return null：不存在SD卡
	 */
	public static String getRootPath(){
		File rootDirectory = getRootDirectory();
		return rootDirectory != null ?rootDirectory.getPath():null;
	}

	/**
	 * 获取SD卡总的容量
	 * @return 总容量；-1：SD卡不可用
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static long getTotalSize(){
		if(isAvailable()){
			StatFs statFs = new StatFs(getRootDirectory().getPath());
			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1){
				return statFs.getBlockCount() * statFs.getBlockSize();
			}else{
				return statFs.getBlockCount() * statFs.getBlockSize();
			}
		}else{
			return -1;
		}
	}

	/**
	 * 获取SD卡中可用的容量
	 * @return 可用的容量；-1：SD卡不可用
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static long getAvailableSize(){
		if(isAvailable()){
			StatFs statFs = new StatFs(getRootDirectory().getPath());
			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1){
				return statFs.getAvailableBlocks() * statFs.getBlockSize();
			}else{
				return statFs.getAvailableBlocks() * statFs.getBlockSize();
			}
		}else{
			return -1;
		}
	}

	/**
	 * 删除单个文件
	 *
	 * @author xuz
	 * @date 2019/4/1 10:07 AM
	 * @param [path]
	 * @return boolean
	 */
	public static boolean removeFile(String path){

		File file = new File(path);
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				Log.e("--Method--", "Copy_Delete.deleteSingleFile: 删除文件" + path + "成功！");
				return true;
			} else {
				Log.e("--Method--", "Copy_Delete.deleteSingleFile: 删除文件" + path + "失败！");
//				Toast.makeText(getApplicationContext(), "删除文件" + filePath$Name + "失败！", Toast.LENGTH_SHORT).show();
				return false;
			}
		} else {
			Log.e("--Method--", "Copy_Delete.deleteSingleFile: 删除文件" + path + "不存在！");
//			Toast.makeText(getApplicationContext(), "删除文件失败：" + filePath$Name + "不存在！", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

}
