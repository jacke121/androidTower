package ui;

import java.util.LinkedHashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.lbg.yan01.R;
import android.provider.Settings.Secure;

public class IndexActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	public static String serverIp = "192.168.1.1";
	public static String eTxtUser, eTxtPwd, message;
	EditText  main_eTxtUser;
//	ShowDialog mShowDialog;
	public static String error_message = "",remMsg;
	String msg = null;
	public  SharedPreferences sharedPreferences_userInfo;
	 Editor editor;
	 private int isFirst;//0第一次
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		setContentView(R.layout.index);
		// /在Android2.2以后必须添加以下代码
		// 本应用采用的Android4.0
		// 设置线程的策略
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork() // or
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath()
				.build());
		sharedPreferences_userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);

		Button main_btnLogin = (Button) findViewById(R.id.main_btnLogin);
		main_btnLogin.setOnClickListener(this);
		// 转到注册页面
		
		Button btnAuthorize = (Button) findViewById(R.id.btnAuthorize);
		btnAuthorize.setOnClickListener(this);

		//非首次登陆直接进入笔记页面
		remMsg	=sharedPreferences_userInfo.getString("userName", "");
		if(remMsg!=null&&!remMsg.equals("")){
			Intent intent = new Intent(IndexActivity.this, MainActivity.class);
			startActivity(intent);
			IndexActivity.this.finish();
		}

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.main_btnLogin: {
				Intent intent = new Intent(IndexActivity.this,
						MainActivity.class);
                    /* 把bundle对象assign给Intent */
				startActivityForResult(intent, 1);
			}
			break;
			case R.id.btnAuthorize:
				String android_id = Secure.getString(getApplication().getContentResolver(), Secure.ANDROID_ID);
				showDialog(android_id);
				break;
		}}

	// 弹出警告对话框
	public void setAlertDialog(String title, String message) {
		Builder builder = new Builder(IndexActivity.this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	/**
	 * 获取Struts2 Http 登录的请求信息
	 * @param userName
	 * @param password
	 * @return
	 */
	public String loginRemoteService(String userName, String password) {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("userName", main_eTxtUser.getText().toString().trim());

		return msg;
	}
	public void showDialog(String msg) {
		// 创建提示框提醒是否登录成功
		
		Builder builder = new Builder(IndexActivity.this);
		builder.setTitle("提示").setMessage(msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}                                          
		}).create().show(); 
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		getApplicationContext().unregisterReceiver(mNetworkConnectChangedReceiver);
		super.onDestroy();
	}
	
}