package my.JSB.android;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

public class DBHelper extends SQLiteOpenHelper  {
	private static final int DATABASE_VERSION=1;
	private static final String DATABASE_NAME="NotebookDB";
	//通过super调用父类当中的构造函数
	public DBHelper(Context context,String name,CursorFactory factory,int version){
		super(context,name,factory,version);
	}
	
	public DBHelper(Context context,String name,int version){
		super(context,name,null,version);
	}
	
	public DBHelper(Context context,String name){
		super(context,name,null,DATABASE_VERSION);
	}
	
	public DBHelper(Context context){
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db){
		String sql="create table user(userID  INTEGER primary key autoincrement,name text not null,pwd text not null)";
		String detailQql="create table note_detail(noteID  INTEGER primary key autoincrement,name text not null,date text not null, title text not null,contentDetail text not null)";
		db.execSQL(sql);
		db.execSQL(detailQql);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
		
	}

}
