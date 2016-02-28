package note.dao;

import java.util.Date;
import android.text.TextUtils;
import android.database.Cursor;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import android.util.SparseArray;


public class UsersDao{
 public static final String TABLENAME ="Users";
 public static final Object SYNC= new Object();
 private final SQLiteOpenHelper mOpenHelper;
 public UsersDao(SQLiteOpenHelper openHelper){
   mOpenHelper=openHelper;
   }
   public Cursor query(String whereClause, String []whereArgs){
     final String sql = "SELECT *"
     + " FROM " + (TextUtils.isEmpty(whereClause)? TABLENAME : TABLENAME+" WHERE "+whereClause);
     return mOpenHelper.getReadableDatabase().rawQuery(sql, whereArgs);
     }


     public SparseArray<Users> queryToList(String whereClause, String []whereArgs){
     Cursor cursor=null;
     int index=0;
     try{
       synchronized(SYNC){
      if ( (cursor = query(whereClause, whereArgs) )==null || cursor.getCount()<1)return null;
     SparseArray<Users> list = new SparseArray<Users>(cursor.getCount());
      while (cursor.moveToNext()){
      Users entity=new Users(); 
    entity.id=cursor.isNull(COLUMNINDEXS.id )? -1 :cursor.getInt(COLUMNINDEXS.id);
    entity.username=cursor.isNull(COLUMNINDEXS.username )? "" :cursor.getString(COLUMNINDEXS.username);
    entity.password=cursor.isNull(COLUMNINDEXS.password )? "" :cursor.getString(COLUMNINDEXS.password);
    entity.repassword=cursor.isNull(COLUMNINDEXS.repassword )? "" :cursor.getString(COLUMNINDEXS.repassword);
    entity.autologin=cursor.isNull(COLUMNINDEXS.autologin )? "" :cursor.getString(COLUMNINDEXS.autologin);
    entity.countlogin=cursor.isNull(COLUMNINDEXS.countlogin )? -1 :cursor.getInt(COLUMNINDEXS.countlogin);
    entity.lifestatus=cursor.isNull(COLUMNINDEXS.lifestatus )? -1 :cursor.getInt(COLUMNINDEXS.lifestatus);
    entity.upgradeflag=cursor.isNull(COLUMNINDEXS.upgradeflag )? -1 :cursor.getInt(COLUMNINDEXS.upgradeflag);
       list.append(index++,entity);
       }; 
      cursor.close(); 
      return list; 
      } 
      }catch(Exception ex){ 
  ex.printStackTrace();       }finally{ 
 if (cursor!= null) cursor.close();
    }   return null;
 }       public int insert(Users entity){
         SQLiteDatabase db=null;
          try{
          return insert0(db=mOpenHelper.getWritableDatabase(), entity);
          }finally{
          if (db!=null) db.close();
          }
        }
       public boolean update(Users entity){
         SQLiteDatabase db=null;
          try{
          return update0(db=mOpenHelper.getWritableDatabase(), entity, COLUMNS.id+"=?", new String[]{String.valueOf(entity.id)} );
          }finally{
          if (db!=null) db.close();
          }
        }
       public boolean delete(Users entity){
         SQLiteDatabase db=null;
          try{
          return delete0(db=mOpenHelper.getWritableDatabase(), COLUMNS.id+"=?", new String[]{String.valueOf(entity.id)} );
          }finally{
          if (db!=null) db.close();
          }
        }
   public static final class COLUMNINDEXS{
    public static final int id=0;
    public static final int username=1;
    public static final int password=2;
    public static final int repassword=3;
    public static final int createtime=4;
    public static final int autologin=5;
    public static final int updatetime=6;
    public static final int countlogin=7;
    public static final int lifestatus=8;
    public static final int upgradeflag=9;
   }
   public static final class COLUMNS{
    public static final String id="[id]";
    public static final String username="[username]";
    public static final String password="[password]";
    public static final String repassword="[repassword]";
    public static final String createtime="[createtime]";
    public static final String autologin="[autologin]";
    public static final String updatetime="[updatetime]";
    public static final String countlogin="[countlogin]";
    public static final String lifestatus="[lifestatus]";
    public static final String upgradeflag="[upgradeflag]";
   }
       private int insert0(SQLiteDatabase db, Users entity){ 
       ContentValues cv=new ContentValues(); 
    cv.put(COLUMNS.username, entity.username );
    cv.put(COLUMNS.password, entity.password );
    cv.put(COLUMNS.repassword, entity.repassword );
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    cv.put(COLUMNS.createtime, df.format(new Date()));
    cv.put(COLUMNS.autologin, entity.autologin );
    SimpleDateFormat dfu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    cv.put(COLUMNS.updatetime, dfu.format(new Date()));    cv.put(COLUMNS.countlogin, entity.countlogin );
    cv.put(COLUMNS.lifestatus, entity.lifestatus );
    cv.put(COLUMNS.upgradeflag, entity.upgradeflag );
          int strid=-1;  
        if(db.insert(TABLENAME, null, cv)>0){ 
         Cursor cursor = db.rawQuery("select last_insert_rowid() from "+TABLENAME,null); 
         if(cursor.moveToFirst()) 
          strid= cursor.getInt(0); 
          cursor.close();
            }
          return strid; 
         }
       private boolean update0(SQLiteDatabase db, Users entity, String whereClause, String []whereArgs){ 
       ContentValues cv=new ContentValues(1); 
    cv.put(COLUMNS.id, entity.id );
    cv.put(COLUMNS.username, entity.username );
    cv.put(COLUMNS.password, entity.password );
    cv.put(COLUMNS.repassword, entity.repassword );
    cv.put(COLUMNS.autologin, entity.autologin );
    SimpleDateFormat dfu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    cv.put(COLUMNS.updatetime, dfu.format(new Date()));
    cv.put(COLUMNS.countlogin, entity.countlogin );
    cv.put(COLUMNS.lifestatus, entity.lifestatus );
    cv.put(COLUMNS.upgradeflag, entity.upgradeflag );
        return db.update(TABLENAME, cv, whereClause, whereArgs) >0; 
     }
 private boolean delete0(SQLiteDatabase db, String whereClause, String []whereArgs){
        return db.delete(TABLENAME, whereClause, whereArgs) >0; 
     }
 public boolean drop(SQLiteDatabase db) {
        try { 
     db.execSQL("drop table if exists Users");
     return true;
     } catch (Exception e) {
     e.printStackTrace();
    return false;
     }
     }
      public void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS [users] ( [id] INTEGER NOT NULL PRIMARY KEY,   [username] NVARCHAR2,   [password] NVARCHAR2,   [repassword] NVARCHAR2,   [createtime] DATETIME,   [autologin] NVARCHAR2,   [updatetime] DATETIME NOT NULL DEFAULT (datetime('now')),   [countlogin] INTEGER,   [lifestatus] INTEGER,   [upgradeflag] BIGINT NOT NULL  )"); 
     }
     }
