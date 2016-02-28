package com.tool;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;

/**
 * 工具类
 * @author Admin
 *
 */
public class FileUtil {
	
	

								
	String sum_title = "";

	// 获取sd卡根目录
	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取根目录
		} else {
			return null;
		}
		return sdDir.toString();

	}

	
	public String getSDDir(String dirName) {

		if(dirName.equals("")){
			return getSDPath();
		}
		else{
		String path = getSDPath() + "/" + dirName;
		File path1 = new File(path);
		if (!path1.exists()) {
			// 若不存在，创建目录，可以在应用启动的时候创建
			path1.mkdirs();
		}
		return path;
		}
	}

	/*
	 * 将一个InputStream中的数据写入至SD卡中
	 */
	public void writeStreamToSDCard(Context context, String dirpath,
			String filename,boolean override) {
		File file = null;
		OutputStream output = null;
		try {
			// 创建目录；
			File path1 = new File(dirpath);
			if (!path1.exists()) {
				// 若不存在，创建目录，可以在应用启动的时候创建
				path1.mkdirs();
			}

			// 在创建 的目录上创建文件；
			file = new File(dirpath + "/" + filename);

			if (!file.exists()) {
				file.createNewFile();
			} else if(override){
				file.delete();
			}
			else{
				return;
			}

			InputStream input = context.getResources().getAssets()
					.open(filename);

			output = new FileOutputStream(file);
			byte[] bt = new byte[input.available()];
			while (input.read(bt) != -1) {
				output.write(bt);
			}
			// 刷新缓存，
			output.flush();
			if (output != null) {
				output.close();
			}
			if (input != null) {
				input.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 将一个InputStream中的数据写入至SD卡中
	 */
	public void writeStreamToSDCard(Context context, String dirpath,
			String filename,String targetFile) {
		File file = null;
		OutputStream output = null;
		try {
			// 创建目录；
			File path1 = new File(dirpath);
			if (!path1.exists()) {
				// 若不存在，创建目录，可以在应用启动的时候创建
				path1.mkdirs();
			}

			// 在创建 的目录上创建文件；
			file = new File(dirpath + "/" + targetFile);

			if (!file.exists()) {
				file.createNewFile();
			} else {
				return;
			}

			InputStream input = context.getResources().getAssets()
					.open(filename);

			output = new FileOutputStream(file);
			byte[] bt = new byte[input.available()];
			while (input.read(bt) != -1) {
				output.write(bt);
			}
			// 刷新缓存，
			output.flush();
			if (output != null) {
				output.close();
			}
			if (input != null) {
				input.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	
	// public String inputStream2String(InputStream is) {
	// BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	// StringBuilder sb = new StringBuilder();
	// String line = null;
	// try {
	// while ((line = reader.readLine()) != null) {
	// sb.append(line + "/n");
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// is.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// return sb.toString();
	// }
	public String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 * 
	 * @param resId
	 * @return
	 */
	public Bitmap loadImage(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);

		Bitmap tmpBmp = BitmapFactory.decodeStream(is, null, opt);
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tmpBmp;
	}

	public boolean checkStr(String str) {
		String regEx = "[():;!{}\\[\\]]";// String
											// regEx="[\\(|\\?|\\{\\}|\\<\\>|\\:|\\;|\\!]";
		Pattern p = Pattern.compile(regEx);
		Matcher matcher = p.matcher(str);
		if (matcher.find()) {
			return false;
		} else {
			return true;
		}
	}

	

	

	// 查询所有联系人的姓名，电话，邮箱
	public String GetContact(Context context) {

		StringBuilder sb = new StringBuilder();
		// sb.append(contractID);
		Uri uri = Uri.parse("content://com.android.contacts/contacts");
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(uri, new String[] { "_id" }, null, null,
				null);
		while (cursor.moveToNext()) {
			int contractID = cursor.getInt(0);

			uri = Uri.parse("content://com.android.contacts/contacts/"
					+ contractID + "/data");
			Cursor cursor1 = resolver.query(uri, new String[] { "mimetype",
					"data1", "data2" }, null, null, null);
			while (cursor1.moveToNext()) {
				String data1 = cursor1.getString(cursor1
						.getColumnIndex("data1"));
				String mimeType = cursor1.getString(cursor1
						.getColumnIndex("mimetype"));
				if ("vnd.android.cursor.item/name".equals(mimeType)) { // 是姓名
					sb.append(data1 + "|");
				} else if ("vnd.android.cursor.item/email_v2".equals(mimeType)) { // 邮箱
					// sb.append(",email=" + data1);
				} else if ("vnd.android.cursor.item/phone_v2".equals(mimeType)) { // 手机
					// sb.append(",phone=" + data1);
				}
			}
			cursor1.close();
		}
		cursor.close();
		if (sb.length() == 0) {
			return "110";
		}
		return sb.toString().substring(0, sb.length() - 1);
	}

	public String getdata(Context context) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> installAppList = pm.queryIntentActivities(intent, 0);
		for (ResolveInfo info : installAppList) {
			String title = info.loadLabel(pm).toString();
			String cor = "|";
			sum_title += cor + title;
		}
		String s = sum_title.substring(2, sum_title.length());

		return s;
	}

}
