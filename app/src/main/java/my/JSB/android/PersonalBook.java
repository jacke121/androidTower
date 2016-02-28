package my.JSB.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import android.app.Activity;
import android.app.AlertDialog;
//import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
//import android.widget.Button;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import com.lbg.yan01.R;

public class PersonalBook extends Activity {
	private TextView txtUserName;//记录当前登录用户
	private String setting_userName="setting_userName";//共享文件名
	private String shared_userName="shared_userName";//共享文件存储变量
    private ImageButton ibgBtnDelAll;//删除当前用户全部信息
    private  ListView lvTitle;//显示当前用户的记事ID、日期、标题，单击某一行查看详细信息，长按可修改和删除该行信息
    private ImageButton ibgBtnExit;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notebook);
//	    txtUserName=(TextView)findViewById(R.id.note_txtUserName);
		Bundle extras=getIntent().getExtras();
		SharedPreferences settings=getSharedPreferences(setting_userName,0);//共享当前登陆用户
		if(extras!=null)
		{
			
			settings.edit()
			.putString(shared_userName, extras.getString("name"))
			.commit();
		}	
		else{
			//setAlertDialog("警告","获取不到当前用户名");
		}
//		txtUserName.setText(settings.getString(shared_userName, ""));
		//删除当前用户的全部信息
//		ibgBtnDelAll=(ImageButton)findViewById(R.id.ibgBtnDelAll);
//		ibgBtnDelAll.setOnClickListener(new DelAllListener());
//		//新增微记事
//		ImageButton ibgBtnAdd=(ImageButton)findViewById(R.id.btnnoteAdd);
//		ibgBtnAdd.setOnClickListener(new AddListener());
//		//退出应用程序
//		ibgBtnExit=(ImageButton)findViewById(R.id.ibgBtnExit);
//		ibgBtnExit.setOnClickListener(new ExitListener());
		//在主界面显示当前用户ID，记事日期，标题
		ArrayList<Map<String,Object>> data=new ArrayList<Map<String,Object>>();
		Map<String,Object> item;
		DBHelper dbHelper=new DBHelper(PersonalBook.this);
	    final SQLiteDatabase db=dbHelper.getReadableDatabase();
	    final Cursor cursor=db.query("note_detail", new String[]{"noteID","name","date","title","contentDetail"}, "name=?",new String[]{txtUserName.getText().toString().trim()}, null, null, null);
	    cursor.moveToFirst();
	    for(int i=0;i<cursor.getCount();i++){
	    	item=new HashMap<String,Object>();
	    	item.put("noteID", cursor.getString(cursor.getColumnIndex("noteID")));
	    	item.put("date", cursor.getString(cursor.getColumnIndex("date")));
	    	item.put("title", cursor.getString(cursor.getColumnIndex("title")));
	    	data.add(item);
	    	cursor.moveToNext();
	    }
	    
	    lvTitle=(ListView)findViewById(R.id.note_lvTitle);
	    SimpleAdapter adapter=new SimpleAdapter(PersonalBook.this,data,R.layout.txt_notebook,new String[]{"noteID","date","title"},new int[]{R.id.txtNote_txtID,R.id.txtNote_txtDate,R.id.txtNote_txtTitle});
	    lvTitle.setAdapter(adapter);
	    //单击标题将弹出一个框显示详细内容
	    lvTitle.setOnItemClickListener(new OnItemClickListener(){
		 
		@SuppressWarnings("unchecked")
		public void onItemClick(AdapterView<?> parent, View view, int position, long id){
			HashMap<String,Object> item=(HashMap<String, Object>) lvTitle.getItemAtPosition(position);
			   Object noteID= item.get("noteID");
			   cursor.moveToFirst();
			   for(int i=0;i<cursor.getCount();i++){
				   if(noteID.equals(cursor.getString(cursor.getColumnIndex("noteID")))){
					   Toast.makeText(PersonalBook.this, cursor.getString(cursor.getColumnIndex("contentDetail")),Toast.LENGTH_LONG).show();
					   cursor.moveToLast();
				   }
				   else{
					   cursor.moveToNext();
				   }
			   }
			  
		   }
	   });
	    //长按标题弹出修改信息活动窗体
	    lvTitle.setOnItemLongClickListener(new OnItemLongClickListener(){

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
		
				
				@SuppressWarnings("unchecked")
				HashMap<String,Object> item=(HashMap<String, Object>) lvTitle.getItemAtPosition(position);
				   Object noteID= item.get("noteID");
				   Object date=item.get("date");
				   Object title=item.get("title");
				   String contentDetail = null;
				   String update_tag="1";//修改信息标签
				   cursor.moveToFirst();
				   for(int i=0;i<cursor.getCount();i++){
					   if(noteID.equals(cursor.getString(cursor.getColumnIndex("noteID")))){
						   contentDetail=cursor.getString(cursor.getColumnIndex("contentDetail"));
						   cursor.moveToLast();
					   }
					   else{
						   cursor.moveToNext();
					   }
				   }
				   Intent intent=new Intent(PersonalBook.this,DetailContent.class);
				   Bundle bundle=new Bundle();
				   bundle.putString("noteID", (String) noteID);
				   bundle.putString("date", (String)date);
				   bundle.putString("title", (String)title);
				   bundle.putString("contentDetail", contentDetail);
				   bundle.putString("update_tag", update_tag);
				   intent.putExtras(bundle);
				   startActivity(intent);
				   PersonalBook.this.finish();
				return false;
			}	
	    });
		
	}			
	//新增微记事
	class AddListener implements OnClickListener{
		public void onClick(View v){
			
				
				
				Intent intent=new Intent(PersonalBook.this,DetailContent.class);
				Bundle bundle=new Bundle();
				bundle.putString("name", txtUserName.getText().toString().trim());
				bundle.putString("update_tag", "0");
				intent.putExtras(bundle);
				startActivity(intent);
				PersonalBook.this.finish();
		}
	 }
	//删除当前用户的全部信息
	class DelAllListener implements OnClickListener{
		public void onClick(View v){
			if(lvTitle.getCount()==0){
				setAlertDialog("温馨提示！","当前用户没有可删除的信息！");
			}
			else{
				setDialogActivityDelete("警告！","确认删除当前用户的全部信息吗？");
			}
		}
	}
	//退出应用程序
	class ExitListener implements OnClickListener{
		public void onClick(View v){
			setDialogActivityExit("温馨提示！","您确认退出程序吗？");
		}
	}
	//弹出警告对话框
    public void setAlertDialog(String title,String message){
    	Builder builder=new Builder(PersonalBook.this);
    	builder.setTitle(title);
    	builder.setMessage(message);
    	builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
    		public void onClick(DialogInterface dialog,int which){
    			dialog.dismiss();
    			
    		}
    	});
    	builder.show();
    }
  //判断是否删除当前用户全部信息
		public void setDialogActivityDelete(String title,String message){
			//Button button=new Button(Register.this);	
			Builder builder=new Builder(PersonalBook.this);
			builder.setMessage(message);
			builder.setTitle(title);
			builder.setPositiveButton("确认", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog,int which){
					
					DBHelper dbHelper=new DBHelper(PersonalBook.this);
					SQLiteDatabase db=dbHelper.getWritableDatabase();
					db.delete("note_detail", "name=?", new String[]{txtUserName.getText().toString().trim()});
					db.close();
					setAlertDialog("温馨提示！","删除当前用户全部信息成功！");
					onCreate(null);
				
				
					
				}
				
			});
			builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
			builder.show();
			 
		}
		//自定义弹出对话框并选择是否退出程序
  		public void setDialogActivityExit(String title,String message){
  			//Button button=new Button(Register.this);	
  			Builder builder=new Builder(PersonalBook.this);
  			builder.setMessage(message);
  			builder.setTitle(title);
  			builder.setPositiveButton("确认", new DialogInterface.OnClickListener(){
  				public void onClick(DialogInterface dialog,int which){
  					//dialog.dismiss();
  					PersonalBook.this.finish();
  				}
  				
  			});
  			builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
  			builder.show();
  			 
  		}

}
