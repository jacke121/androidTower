package note.dao;

import java.util.Date;
import android.text.TextUtils;
import android.database.Cursor;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import android.util.SparseArray;


public class NoteDao{
 public static final String TABLENAME ="Note";
 public static final Object SYNC= new Object();
 private final SQLiteOpenHelper mOpenHelper;
 public NoteDao(SQLiteOpenHelper openHelper){
   mOpenHelper=openHelper;
   }
   public Cursor query(String whereClause, String []whereArgs){
     final String sql = "SELECT *"
     + " FROM " + (TextUtils.isEmpty(whereClause)? TABLENAME : TABLENAME+" WHERE "+whereClause);
     return mOpenHelper.getReadableDatabase().rawQuery(sql, whereArgs);
     }


     public SparseArray<Note> queryToList(String whereClause, String []whereArgs){
     Cursor cursor=null;
     int index=0;
     try{
       synchronized(SYNC){
      if ( (cursor = query(whereClause, whereArgs) )==null || cursor.getCount()<1)return null;
     SparseArray<Note> list = new SparseArray<Note>(cursor.getCount());
      while (cursor.moveToNext()){
      Note entity=new Note(); 
    entity.id=cursor.isNull(COLUMNINDEXS.id )? -1 :cursor.getInt(COLUMNINDEXS.id);
    entity.parentid=cursor.isNull(COLUMNINDEXS.parentid )? -1 :cursor.getInt(COLUMNINDEXS.parentid);
    entity.username=cursor.isNull(COLUMNINDEXS.username )? "" :cursor.getString(COLUMNINDEXS.username);
    entity.levels=cursor.isNull(COLUMNINDEXS.levels )? -1 :cursor.getInt(COLUMNINDEXS.levels);
    entity.types=cursor.isNull(COLUMNINDEXS.types )? "" :cursor.getString(COLUMNINDEXS.types);
    entity.name=cursor.isNull(COLUMNINDEXS.name )? "" :cursor.getString(COLUMNINDEXS.name);
    entity.title=cursor.isNull(COLUMNINDEXS.title )? "" :cursor.getString(COLUMNINDEXS.title);
    entity.content=cursor.isNull(COLUMNINDEXS.content )? "" :cursor.getString(COLUMNINDEXS.content);
    entity.orders=cursor.isNull(COLUMNINDEXS.orders )? "" :cursor.getString(COLUMNINDEXS.orders);
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
 }       public int insert(Note entity){
         SQLiteDatabase db=null;
          try{
          return insert0(db=mOpenHelper.getWritableDatabase(), entity);
          }finally{
          if (db!=null) db.close();
          }
        }
       public boolean update(Note entity){
         SQLiteDatabase db=null;
          try{
          return update0(db=mOpenHelper.getWritableDatabase(), entity, COLUMNS.id+"=?", new String[]{String.valueOf(entity.id)} );
          }finally{
          if (db!=null) db.close();
          }
        }
       public boolean delete(Note entity){
         SQLiteDatabase db=null;
          try{
          return delete0(db=mOpenHelper.getWritableDatabase(), COLUMNS.id+"=?", new String[]{String.valueOf(entity.id)} );
          }finally{
          if (db!=null) db.close();
          }
        }
   public static final class COLUMNINDEXS{
    public static final int id=0;
    public static final int parentid=1;
    public static final int username=2;
    public static final int levels=3;
    public static final int types=4;
    public static final int name=5;
    public static final int title=6;
    public static final int content=7;
    public static final int orders=8;
    public static final int createtime=9;
    public static final int updatetime=10;
    public static final int lifestatus=11;
    public static final int upgradeflag=12;
   }
   public static final class COLUMNS{
    public static final String id="[id]";
    public static final String parentid="[parentid]";
    public static final String username="[username]";
    public static final String levels="[levels]";
    public static final String types="[types]";
    public static final String name="[name]";
    public static final String title="[title]";
    public static final String content="[content]";
    public static final String orders="[orders]";
    public static final String createtime="[createtime]";
    public static final String updatetime="[updatetime]";
    public static final String lifestatus="[lifestatus]";
    public static final String upgradeflag="[upgradeflag]";
   }
       private int insert0(SQLiteDatabase db, Note entity){ 
       ContentValues cv=new ContentValues(); 
    cv.put(COLUMNS.parentid, entity.parentid );
    cv.put(COLUMNS.username, entity.username );
    cv.put(COLUMNS.levels, entity.levels );
    cv.put(COLUMNS.types, entity.types );
    cv.put(COLUMNS.name, entity.name );
    cv.put(COLUMNS.title, entity.title );
    cv.put(COLUMNS.content, entity.content );
    cv.put(COLUMNS.orders, entity.orders );
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    cv.put(COLUMNS.createtime, df.format(new Date()));
    SimpleDateFormat dfu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    cv.put(COLUMNS.updatetime, dfu.format(new Date()));    cv.put(COLUMNS.lifestatus, entity.lifestatus );
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
       private boolean update0(SQLiteDatabase db, Note entity, String whereClause, String []whereArgs){ 
       ContentValues cv=new ContentValues(1); 
    cv.put(COLUMNS.id, entity.id );
    cv.put(COLUMNS.parentid, entity.parentid );
    cv.put(COLUMNS.username, entity.username );
    cv.put(COLUMNS.levels, entity.levels );
    cv.put(COLUMNS.types, entity.types );
    cv.put(COLUMNS.name, entity.name );
    cv.put(COLUMNS.title, entity.title );
    cv.put(COLUMNS.content, entity.content );
    cv.put(COLUMNS.orders, entity.orders );
    SimpleDateFormat dfu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    cv.put(COLUMNS.updatetime, dfu.format(new Date()));
    cv.put(COLUMNS.lifestatus, entity.lifestatus );
    cv.put(COLUMNS.upgradeflag, entity.upgradeflag );
        return db.update(TABLENAME, cv, whereClause, whereArgs) >0; 
     }
 private boolean delete0(SQLiteDatabase db, String whereClause, String []whereArgs){
        return db.delete(TABLENAME, whereClause, whereArgs) >0; 
     }
 public boolean drop(SQLiteDatabase db) {
        try { 
     db.execSQL("drop table if exists Note");
     return true;
     } catch (Exception e) {
     e.printStackTrace();
    return false;
     }
     }
      public void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS [note] (  [id] INTEGER NOT NULL PRIMARY KEY,   [parentid] INTEGER,   [username] NVARCHAR2,   [levels] INTEGER,   [types] NVARCHAR2,   [name] NVARCHAR2,   [title] NVARCHAR2,   [content] NVARCHAR2,   [orders] NVARCHAR2,   [createtime] DATETIME,   [updatetime]  DATETIME NOT NULL DEFAULT (datetime('now')),   [lifestatus] INTEGER,   [upgradeflag] INTEGER )"); 
     }
     }
