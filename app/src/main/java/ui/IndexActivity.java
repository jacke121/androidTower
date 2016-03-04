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


public class IndexActivity extends Activity {
	/** Called when the activity is first created. */
	public static IndexActivity indexActivity;
	public static String serverIp = "192.168.1.1";
	public static String eTxtUser, eTxtPwd, message;
	EditText  main_eTxtUser;
//	ShowDialog mShowDialog;
	public static String error_message = "",remMsg;
	String msg = null;
	public static SharedPreferences sharedPreferences_userInfo;
	public  SharedPreferences share;
	 Editor editor;
	 private int isFirst;//0第一次
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		setContentView(R.layout.index);
//		mShowDialog = new ShowDialog(this);
		indexActivity = this;
		Start();
		// /在Android2.2以后必须添加以下代码
		// 本应用采用的Android4.0
		// 设置线程的策略
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork() // or
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath()
				.build());
		sharedPreferences_userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
		share=getSharedPreferences("isFirst", MODE_PRIVATE);
		if (isFirst == 0) {
			Intent i = new Intent(IndexActivity.this, MainActivity.class);
			startActivity(i);
			finish();
		} 
		// 单击取消按钮将退出程序
		// Button main_btnCancel = (Button) findViewById(R.id.main_btnCancel);
		// main_btnCancel.setOnClickListener(new btnCancelListener());
		// 登录私人微记事
		Button main_btnLogin = (Button) findViewById(R.id.main_btnLogin);
		main_btnLogin.setOnClickListener(new setListener());
		// 转到注册页面
		
		Button btnAuthorize = (Button) findViewById(R.id.btnAuthorize);
		btnAuthorize.setOnClickListener(new RegisterListener());


		// 断网情况直接进入笔记页面

		//非首次登陆直接进入笔记页面
		remMsg	=sharedPreferences_userInfo.getString("userName", "");
		if(remMsg!=null&&!remMsg.equals("")){
			Intent intent = new Intent(IndexActivity.this, NoteActivity.class);
			startActivity(intent);
			IndexActivity.this.finish();
		}
	}

	// 转到注册页面
	class RegisterListener implements OnClickListener {
		public void onClick(View v) {
			Intent intent = new Intent(IndexActivity.this, Activity_Area.class);
			startActivity(intent);
			IndexActivity.this.finish();
		}
	}


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
	 * 设置事件的监听器的方法
	 */
	class setListener implements OnClickListener {
		public void onClick(View v) {
			main_eTxtUser = (EditText) findViewById(R.id.main_eTxtUser);
			showDialog("正在登录，请稍候。。。。");
			new Thread() {
				public void run() {
					try {
						// Do some Fake-Work
						Thread.sleep(10000);
					} catch (Exception e) {
					}
					// 休眠10s之后，若数据仍在加载，则令加载窗体消失
					mMsgReciver.sendEmptyMessage(2);// 超时处理
				}
			}.start();
			new Thread() {
				public void run() {
					try {
						// Do some Fake-Work
						eTxtUser = main_eTxtUser.getText().toString().trim();
						error_message = loginRemoteService(eTxtUser, eTxtPwd);
						if (error_message.contains("成功")) {
							// 登录成功，则加载窗体消失,提示用户登录成功
							mMsgReciver.sendEmptyMessage(1);
						}
						else  {
							mMsgReciver.sendEmptyMessage(10);// 返回错误消息给用户
						}
					} catch (Exception e) {
					}
					// Dismiss the Dialog
				}
			}.start();
		}
	}


	@SuppressLint("HandlerLeak")
	private Handler mMsgReciver = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				showDialog("登陆失败重新登录");
				break;
			case 1:
				Intent intent = new Intent();
				intent.putExtra("uname", eTxtUser);
				intent.setClass(IndexActivity.this, NoteActivity.class);
				startActivity(intent);
				break;
			case 2:
				new Builder(IndexActivity.this).setTitle("温馨提示").setMessage("网络连接超时了!")
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								return;
							}
						}).create().show();
				break;
			case 10:
				showDialog(error_message);
				break;
			}
		}
	};

	/**
	 * 获取Struts2 Http 登录的请求信息
	 * 
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
	private void Start() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//		mNetworkConnectChangedReceiver = new NetworkConnectChangedReceiver();
//		getApplicationContext().registerReceiver(mNetworkConnectChangedReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		getApplicationContext().unregisterReceiver(mNetworkConnectChangedReceiver);
		super.onDestroy();
	}
	
}