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

import com.baseDao.Areas;
import com.baseDao.AreasDao;
import com.baseDao.SqlHelper;
import com.lbg.yan01.MyApplication;
import com.lbg.yan01.R;

import webClient.DialogFactory;
public class Activity_Area extends Activity implements OnClickListener {
	 public static EditText txt_qubian, txt_quxian, txt_gongbian, txt_danwei;
	 Button btn_save;
	 ImageView title_btn_sequence;
	 String msg=null;
		public static Dialog mdlg;
		public static String name, pwd, rpwd, registerMessage, uuid, uuid_psw,message;
		public static Activity_Area activityArea;
		public String msg_show ;
	SqlHelper helper;
	AreasDao areasDao;
	    @SuppressLint("NewApi")
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	    	  super.onCreate(savedInstanceState);
	    	  requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
			MyApplication myApplication = (MyApplication) getApplication();
			 helper=myApplication.getSqlHelper();
			areasDao = new AreasDao(helper);
			// 查询数据库

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
			txt_qubian = (EditText) findViewById(R.id.txt_qubian);
	    	txt_quxian = (EditText) findViewById(R.id.txt_quxian);
	    	txt_gongbian = (EditText) findViewById(R.id.txt_gongbian);
			txt_danwei= (EditText) findViewById(R.id.txt_danwei);
	    	btn_save = (Button) findViewById(R.id.btn_save);
	    	Button register_btnCancel=(Button)findViewById(R.id.register_btnCancel);
	    	register_btnCancel.setOnClickListener(this);
	    	title_btn_sequence=(ImageView)findViewById(R.id.title_btn_sequence);
	    	title_btn_sequence.setOnClickListener(this);
		}

		/**
		 * 设置事件的监听器的方法
		 */
		private void setListener() {
		
			btn_save.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					name = txt_qubian.getText().toString();
					pwd = txt_quxian.getText().toString();
					rpwd = txt_gongbian.getText().toString();
//					registerRemoteService(name,pwd);
					if (name.equals("") || pwd.equals("") || rpwd.equals("")) {
						showMsg("信息不能为空!");
					} else {
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
										msg_show = registerRemoteService(name, pwd, rpwd);
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
    	    params.put("userName", txt_qubian.getText().toString());
    	    params.put("password", txt_quxian.getText().toString());
    	   params.put("repassword", txt_gongbian.getText().toString());
    	    return msg;
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_save:
				Areas areas=new Areas();
				areas.city="";
				areas.area="";
				areasDao.insert(areas);
				break;
			case R.id.register_btnCancel:
				Intent intent=new Intent(Activity_Area.this, IndexActivity.class);
				startActivity(intent);
				Activity_Area.this.finish();
				break;
			case R.id.title_btn_sequence:
				Activity_Area.this.finish();
				break;
			default:
				break;
		}
	}
}
