package ui;


import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.baseDao.SqlHelper;
import com.lbg.yan01.MyApplication;
import com.lbg.yan01.R;

public class SettingActivity extends Activity {

	ListView list_settinginfo;
	SettingAdapter settingAdapter;
	ImageView title_btn_back;
	public static SqlHelper helper;
	String updateurl = IndexActivity.serverIp +"/note_listAll.action";
	String message = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		setContentView(R.layout.setting);

		list_settinginfo = (ListView) findViewById(R.id.list_settinginfo);
		settingAdapter = new SettingAdapter(SettingActivity.this);
		list_settinginfo.setAdapter(settingAdapter);
		title_btn_back = (ImageView) findViewById(R.id.title_btn_back);
		title_btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跳转至设置界面
				SettingActivity.this.finish();
			}
		});
		list_settinginfo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				if (position == 0) {// 用戶管理
					Intent int_s = new Intent(SettingActivity.this, IndexActivity.class);
					startActivity(int_s);
				} else if (position == 1) {// 退出

					setDialogActivityExit("温馨提示！", "您确认退出程序吗？");
				} else if (position == 2) {// 同步更新


				}
			}
		});
	}

	public int queryMaxUpdateId() {

		Cursor cursor = null;
		int Id = -1;
		String strSql;
		try {
//			String userName = IndexActivity.sharedPreferences_userInfo.getString("userName", "");
//			strSql = "select max(upgradeflag) as maxID from note where username='" + userName + "'";
//
//			MyApplication myApplication = (MyApplication) getApplication();
//			helper=myApplication.getSqlHelper();
//			cursor = helper.getReadableDatabase().rawQuery(strSql, null);
//			if (cursor != null) {
//				cursor.moveToFirst();
//				Id = cursor.getInt(cursor.getColumnIndex("maxID"));
//				cursor.close();
//			} else {
//				return 0;
//			}
		} catch (Exception e) {
			if (cursor != null) {
				cursor.close();
			}
		}

		return Id;
	}

	// 自定义弹出对话框并选择是否退出程序
	public void setDialogActivityExit(String title, String message) {
		// Button button=new Button(Register.this);
		Builder builder = new Builder(SettingActivity.this);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// dialog.dismiss();
				if (NoteActivity.noteActivity != null) {
					NoteActivity.noteActivity.finish();
				}
				SettingActivity.this.finish();
			}

		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.show();

	}
	
}
