package ui;

import java.util.LinkedHashMap;
import java.util.Map;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.lbg.yan01.R;

import webClient.DialogFactory;

/**
 * 在现在开发的项目中采用Android+REST WebService服务方式开发的手机平台很少采用
 *  soap协议这种方式，主要soap协议解析问题，增加了代码量。
 *  
 *  
 * 在android中有时候我们不需要用到本机的SQLite数据库提供数据，更多的时候是从网络上获取数据，
 * 那么Android怎么从服务器端获取数据呢？有很多种，归纳起来有
 *	一：基于Http协议获取数据方法。
 *	二：基于SAOP协议获取数据方法
 *
 *备注：在网络有关的问题最好添加以下两项：
 *   1.线程和虚拟机策略
 *   ///在Android2.2以后必须添加以下代码
*			//本应用采用的Android4.0
*			//设置线程的策略
*			 StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()   
*	         .detectDiskReads()   
*	         .detectDiskWrites()   
*	         .detectNetwork()   // or .detectAll() for all detectable problems   
*	         .penaltyLog()   
*	         .build());   
*			//设置虚拟机的策略
*		  StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()   
*			         .detectLeakedSqlLiteObjects()   
*			         //.detectLeakedClosableObjects()   
*			         .penaltyLog()   
*			         .penaltyDeath()   
*			         .build());
*	   2.可以访问网络的权限：
*	    即AndroidManifest.xml中配置：
*	     <uses-permission android:name="android.permission.INTERNET"/>
 *
 * 
 * @author longgangbai
 * 
 *
 */
public class Activity_Area extends Activity {
	 private static  String processURL="http://192.168.1.63:8080/NoteServer2/users_register.action";
	 public static EditText reg_username, reg_password, reg_repassword, edtValidCode,
		register_npwd, register_rpwd;
	 Button regButton;
	 ImageView title_btn_sequence;
	 String msg=null;
		public static Dialog mdlg;
		public static String name, pwd, rpwd, registerMessage, uuid, uuid_psw,message;
		public static Activity_Area activityArea;
		public String msg_show ;
	    /**
	     *  Called when the activity is first created.
	     */
	    @SuppressLint("NewApi")
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	    	  super.onCreate(savedInstanceState);
	    	  
	    	  requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
	    	  
	    	///在Android2.2以后必须添加以下代码
			//本应用采用的Android4.0
			//设置线程的策略
	    	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  
	    	.detectDiskReads()  
	    	.detectDiskWrites()  
	    	.detectNetwork()   // or .detectAll() for all detectable problems  
	    	.penaltyLog()  
	    	.build());  
	    	StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()  
	    	.detectLeakedSqlLiteObjects()  
	    	.detectLeakedClosableObjects()  
	    	.penaltyLog()  
	    	.penaltyDeath()  
	    	.build());  
	    	activityArea = this;
	    	setContentView(R.layout.area_detail);
	        //设置初始化视图
	        initView();
	        //设置事件监听器方法
	        setListener();
	      
        }
	    @SuppressLint("HandlerLeak")
	 	public  Handler mMsgReciver = new Handler() {
	 		@Override
	 		public void handleMessage(Message msg) {
	 			switch (msg.what) {
	 			case 0:
	 				if (mdlg != null) {
	 					mdlg.dismiss();
	 					mdlg = null;
	 					showregisterMessage("注册失败重新注册");
	 				}
	 				break;
	 			case 12:
	 				if (mdlg != null) {
	 					mdlg.dismiss();
	 					new Builder(Activity_Area.this)
	 							.setTitle("温馨提示")
	 							.setMessage("网络超时了!")
	 							.setPositiveButton("确定",
	 									new DialogInterface.OnClickListener() {
	 										public void onClick(
	 												DialogInterface dialog,
	 												int which) {
	 											return;
	 										}
	 									}).create().show();
	 				}
	 				break;
	 			case 13:
	 				if (mdlg != null) {
	 					mdlg.dismiss();
	 					mdlg = null;
	 					
	 					if (msg_show!=null&&msg_show.equals("恭喜您注册成功")) {
	 						//用户存储
	 					
	 						showregisterMessage(msg_show);
	 					}else if(msg_show.equals("用户名已存在")){
	 						showregisterMessage("用户名已存在,请重新输入！");
	 					}else {
	 						showregisterMessage("注册失败！");
	 						
	 					}
	 					
	 				}
	 				break;
	 			}
	 		}
	 	};
	 	public void showregisterMessage(final String msg) {
			// 创建提示框提醒是否登录成功
			Builder builder = new Builder(Activity_Area.this);
			builder.setTitle("提示")
			.setMessage(msg)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if(msg.contains("成功")){
								//进入首次设置界面
								Intent intent = new Intent();
								intent.setClass(Activity_Area.this, IndexActivity.class);
								startActivity(intent);
							}else {
								//进入首次设置界面
								Intent intent = new Intent();
								intent.setClass(Activity_Area.this, Activity_Area.class);
								startActivity(intent);
							}

							
							dialog.dismiss();
						}
					}).create().show();
		}
		
        /**
         * 创建初始化视图的方法
         */
		private void initView() {
			reg_username = (EditText) findViewById(R.id.register_eTxtUser);
	    	reg_password = (EditText) findViewById(R.id.register_eTxtPwd);
	    	reg_repassword = (EditText) findViewById(R.id.register_eTxtPwdSure);
	    	//edtValidCode = (EditText) findViewById(R.id.edtValidCode);
	    	regButton = (Button) findViewById(R.id.register_btnRegister);
	    	Button register_btnCancel=(Button)findViewById(R.id.register_btnCancel);
	    	register_btnCancel.setOnClickListener(new btnCancelListener());	
	    	title_btn_sequence=(ImageView)findViewById(R.id.title_btn_sequence);
	    	title_btn_sequence.setOnClickListener(new btnbackListener());	
		}

		/**
		 * 设置事件的监听器的方法
		 */
		private void setListener() {
		
			regButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 name=reg_username.getText().toString();
					 pwd=reg_password.getText().toString();
					 rpwd= reg_repassword.getText().toString();
//					registerRemoteService(name,pwd);
					if(name.equals("")||pwd.equals("")||rpwd.equals("")){
						showMsg("信息不能为空!");
					}else{
					try {

						mdlg = DialogFactory.createLoadingDialog(
								Activity_Area.this, "注册中，请稍候……");
						mdlg.getWindow().setType(
								(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
						mdlg.show();
						new Thread() {
							public void run() {
								try {
									// 原来20s
									Thread.sleep(20000);
								} catch (Exception e) {
								}
								// 休眠10s之后，若数据仍在加载，则令加载窗体消失
								mMsgReciver.sendEmptyMessage(12);// 超时处理
							}
						}.start();
						new Thread() {
							public void run() {
								try {
									msg_show= registerRemoteService(name, pwd,rpwd);
									mMsgReciver.sendEmptyMessage(13);//反馈注册消息给用户
								} catch (Exception e) {
								}
							}
						}.start();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				}
			});
		}
		public void showMsg(String msg) {
			new Builder(Activity_Area.this).setTitle("温馨提示")
					.setMessage(msg + "!")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					}).create().show();
		}
    /**
     * 获取Struts2 Http 登录的请求信息
     * @param  userName
     * @param  password
    * @return 
     */
    public String registerRemoteService(String userName,String password,String repassword){
        
    	    Map<String, String> params=new LinkedHashMap<String, String>();
    	    params.put("userName",reg_username.getText().toString() );
    	    params.put("password",reg_password.getText().toString() );
    	   params.put("repassword",reg_repassword.getText().toString() );

    	    return msg;
    }
  //单击取消按钮将返回登陆界面
  		class btnCancelListener implements OnClickListener{
  			public void onClick(View v){
  				Intent intent=new Intent(Activity_Area.this, IndexActivity.class);
  				startActivity(intent);
  				Activity_Area.this.finish();
  			}
  		}
  		class btnbackListener implements OnClickListener{
  			public void onClick(View v){
  				Activity_Area.this.finish();
  			}
  		}
}
