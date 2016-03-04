package ui;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.baseDao.SqlHelper;
import com.lbg.yan01.MyApplication;
import com.lbg.yan01.R;



import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.util.SparseArray;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.EditText;
public class DetailContent extends Activity{

	private EditText eTxtTitle;
	private EditText eTxtContent;
	private EditText  eTxtDate;
	private String update_tag="0";//0表示新增信息，1表示修改信息
	private Button btnDel;
	private String noteID="-1";
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		 eTxtTitle=(EditText)findViewById(R.id.detail_eTxtitle);
		 eTxtContent=(EditText)findViewById(R.id.detail_eTxtContent);
		 eTxtDate=(EditText)findViewById(R.id.detail_eTxtDate);
		 btnDel=(Button)findViewById(R.id.detail_btndelete);
		 btnDel.setOnClickListener(new DelListener());
		Bundle extras=getIntent().getExtras();
		if(extras!=null){
			update_tag=extras.getString("update_tag");
			 if(update_tag.equals("1")){
					
					eTxtTitle.setText(extras.getString("title"));
					eTxtContent.setText(extras.getString("contentDetail"));
					eTxtDate.setText(extras.getString("date"));
		
				}
			 else{
				    eTxtTitle.setText("");
					eTxtContent.setText("");
					eTxtDate.setText("");
			 }
			 
		}
		else{
			setAlertDialog("警告","获取不到当前用户信息");
		}
		GregorianCalendar calendar=new  GregorianCalendar();
		Button btnInsert=(Button)findViewById(R.id.detail_btnFinish);
		Button btnCancel=(Button)findViewById(R.id.detail_btnCancel);
		Button btnDate=(Button)findViewById(R.id.detail_btnDate);
		final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {		
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				EditText  eTxtDate=(EditText)findViewById(R.id.detail_eTxtDate);
				eTxtDate.setText(year + "/" + monthOfYear + 1 + "/" + dayOfMonth);
			}	
		}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		//eTxtDate.setFocusable(false);
		btnDate.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				 
				
				datePickerDialog.show();
			}
		});
		btnCancel.setOnClickListener(new CancelListener());
		btnInsert.setOnClickListener(new InsertListener());
	}
	//新增、修改、删除详细信息
	class InsertListener implements OnClickListener{
		public void onClick(View v){
			String title=eTxtTitle.getText().toString().trim();
			String mainContent=eTxtContent.getText().toString().trim();
			String date=eTxtDate.getText().toString().trim();
		
			Bundle extras=getIntent().getExtras();
			if(extras!=null)
			{
				// update_tag=extras.getString("update_tag");
				 if(update_tag.equals("1")){
					noteID=extras.getString("noteID");
					if(title.equals("") && mainContent.equals("") && date.equals("")){
						setAlertDialog("警告","标题、日期、主要内容都不能为空！");
						
					}
					else{

						MyApplication myApplication = (MyApplication) getApplication();
						SqlHelper helper=myApplication.getSqlHelper();
						SQLiteDatabase db= helper.getWritableDatabase();
						ContentValues values=new ContentValues();
						values.put("title", title);
						values.put("contentDetail", mainContent);
						values.put("date", date);
						db.update("note_detail", values, "noteID=?", new String[]{noteID});
						db.close();
						setDialogActivity("温馨提示！","修改信息成功,系统将跳至主页面！");
					}
					
				}
				else
				{
					if(title.equals("") && mainContent.equals("") && date.equals("")){
						setAlertDialog("警告","标题、日期、主要内容都不能为空！");
						
					}
					else{
//						NoteDao CPDao = new NoteDao(IndexActivity.helper);
//						// CPDao.drop(helper.getWritableDatabase());
//						SparseArray<Note> list_callPolice = new SparseArray<Note>();
//
//						Note notes = new Note();
//						notes.parentid=1;
//						notes.types = "note";
//						notes.name=extras.getString("name") ;
////						notes.datetimes=date;
//						notes.name=title;
//						notes.content=mainContent;
//						notes.lifestatus = 1;
//						notes.upgradeflag = 1;
//						CPDao.insert(notes);
						setDialogActivity("温馨提示！","新增信息成功,系统将跳至主页面！");
					}
				}
				
				
			}
			else{
				setAlertDialog("警告","获取不到当前用户信息！");
			}
			
		}
	}
	//删除用户信息
	class DelListener implements OnClickListener{
		public void onClick(View v){
			setDialogActivityDelete("温馨提示！","确认删除当前信息吗？");
		}
	}
	//退出本活动窗体
	class CancelListener implements OnClickListener{
		public void onClick(View v){
			setDialogActivityCancel("温馨提示！","确定回到主界面吗？");
		}
	}
	//弹出警告对话框
    public void setAlertDialog(String title,String message){
    	Builder builder=new Builder(DetailContent.this);
    	builder.setTitle(title);
    	builder.setMessage(message);
    	builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
    		public void onClick(DialogInterface dialog,int which){
    			dialog.dismiss();
    			
    		}
    	});
    	builder.show();
    }
  //自定义弹出对话框并跳转至其他活动程序
  		public void setDialogActivity(String title,String message){
  			//Button button=new Button(Register.this);	
  			Builder builder=new Builder(DetailContent.this);
  			builder.setMessage(message);
  			builder.setTitle(title);
  			builder.setPositiveButton("确认", new DialogInterface.OnClickListener(){
  				public void onClick(DialogInterface dialog,int which){
  					dialog.dismiss();
  					   Intent intent=new Intent(DetailContent.this,NoteActivity.class);
  					   //intent.setClassName(Register.this, activity);
  					startActivity(intent);
  					 DetailContent.this.finish();
  				}
  				
  			});
  			builder.show();
  			
  		}
  		//自定义弹出对话框并选择是否退出本活动窗体
  		public void setDialogActivityCancel(String title,String message){
  			//Button button=new Button(Register.this);	
  			Builder builder=new Builder(DetailContent.this);
  			builder.setMessage(message);
  			builder.setTitle(title);
  			builder.setPositiveButton("确认", new DialogInterface.OnClickListener(){
  				public void onClick(DialogInterface dialog,int which){
  					Intent intent=new Intent(DetailContent.this,NoteActivity.class);
  					startActivity(intent);
  					DetailContent.this.finish();
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
  		//判断是否删除当前用户信息
  		public void setDialogActivityDelete(String title,String message){
  			//Button button=new Button(Register.this);	
  			Builder builder=new Builder(DetailContent.this);
  			builder.setMessage(message);
  			builder.setTitle(title);
  			builder.setPositiveButton("确认", new DialogInterface.OnClickListener(){
  				public void onClick(DialogInterface dialog,int which){
  					Bundle extras=getIntent().getExtras();
  					if(extras!=null){
  						if(update_tag.equals("1")){
  							 noteID=extras.getString("noteID");
  							 if(noteID.equals("-1")){
  								 setAlertDialog("警告","获取不到当前用户信息");
  							 }
  							 else{
  									setDialogActivity("温馨提示！","删除信息成功,系统将跳至主页面！");
  							 }
  					     }
  						 else
  						 {
  							 setAlertDialog("警告","没有可删除的当前用户信息！");
  						 }
  					}
  					else{
  						setAlertDialog("警告","获取不到当前用户信息！");
  					}
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
