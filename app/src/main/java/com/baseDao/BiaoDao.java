package com.baseDao;

import java.util.Date;
import android.text.TextUtils;
import android.database.Cursor;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import android.util.SparseArray;
import android.database.sqlite.SQLiteStatement;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class BiaoDao{
Lock lock = new ReentrantLock();
    SimpleDateFormat dfu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 public static final String TABLENAME ="Biao";
  COLUMNINDEXS cOLUMNINDEXS=new COLUMNINDEXS();
  COLUMNS cOLUMNS=new COLUMNS();
 public final Object SYNC= new Object();
 private final SQLiteOpenHelper mOpenHelper;
 public BiaoDao(SQLiteOpenHelper openHelper){
   mOpenHelper=openHelper;
          mOpenHelper.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS [Biao] (  [id] INTEGER PRIMARY KEY AUTOINCREMENT,   [name] NVARCHAR2(50),     [code] NVARCHAR2(50),   [gantaid] INTEGER,   [taiquid] INT,   [yunxing] NVARCHAR2(11),   [zuobiao] NVARCHAR2(50),   [level] INT,   [createtime] DATETIME NOT NULL DEFAULT (datetime('now')),   [updatetime] DATETIME NOT NULL,   [LifeStatus] INTEGER NOT NULL,   [upgradeFlag] BIGINT NOT NULL,   [areaname] NVARCHAR2(100),   [danwei] NVARCHAR2(50))"); 
      }
   public Cursor query(String whereClause, String []whereArgs){
     final String sql = "SELECT *"
     + " FROM " + (TextUtils.isEmpty(whereClause)? TABLENAME : TABLENAME+" WHERE "+whereClause);
     return mOpenHelper.getReadableDatabase().rawQuery(sql, whereArgs);
     }


       public  boolean insertList( SparseArray<Biao> list) {
              if ( null == list || list.size() <= 0) {
                  return false;
              }
                       SQLiteDatabase db = mOpenHelper.getWritableDatabase();
                      try {
   	            String sql ="insert into Biao(" 
   + cOLUMNS.id+","   + cOLUMNS.name+","   + cOLUMNS.code+","   + cOLUMNS.gantaid+","   + cOLUMNS.taiquid+","   + cOLUMNS.yunxing+","   + cOLUMNS.zuobiao+","   + cOLUMNS.level+","   + cOLUMNS.createtime+","   + cOLUMNS.updatetime+","   + cOLUMNS.lifeStatus+","   + cOLUMNS.upgradeFlag+","   + cOLUMNS.areaname+","   + cOLUMNS.danwei        + ") " + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
   	            SQLiteStatement stat = db.compileStatement(sql);
   	            db.beginTransaction();
   	             for (int i=0;i<list.size();i++)  {
   Biao entity = list.get(i);  
  if(null==entity.id)    stat.bindNull(1); else
   stat.bindLong(1,entity.id);
  if(null==entity.name||entity.name.length()==0)    stat.bindNull(2);else
   stat.bindString(2,entity.name);
  if(null==entity.code||entity.code.length()==0)    stat.bindNull(3);else
   stat.bindString(3,entity.code);
  if(null==entity.gantaid)    stat.bindNull(4); else
   stat.bindLong(4,entity.gantaid);
  if(null==entity.taiquid)    stat.bindNull(5); else
   stat.bindLong(5,entity.taiquid);
  if(null==entity.yunxing||entity.yunxing.length()==0)    stat.bindNull(6);else
   stat.bindString(6,entity.yunxing);
  if(null==entity.zuobiao||entity.zuobiao.length()==0)    stat.bindNull(7);else
   stat.bindString(7,entity.zuobiao);
  if(null==entity.level)    stat.bindNull(8); else
   stat.bindLong(8,entity.level);
   stat.bindString(9,dfu.format(new Date()));
   stat.bindString(10,dfu.format(new Date()));
  if(null==entity.lifeStatus)    stat.bindLong(11,1);else
   stat.bindLong(11,entity.lifeStatus);
  if(null==entity.upgradeFlag)    stat.bindLong(12,1);else
   stat.bindLong(12,entity.upgradeFlag);
  if(null==entity.areaname||entity.areaname.length()==0)    stat.bindNull(13);else
   stat.bindString(13,entity.areaname);
  if(null==entity.danwei||entity.danwei.length()==0)    stat.bindNull(14);else
   stat.bindString(14,entity.danwei);
  		              long result = stat.executeInsert();
  		                if (result < 0) {
  		                    return false;
  		                }
  		            entity.id=(int)result;
  		            }
  		            db.setTransactionSuccessful();
  		        } catch (Exception e) {
  		            e.printStackTrace();
             return false;
         } finally {
             try {
                     if (null != db) {
                         db.endTransaction();
                     }
                 } catch (Exception e) {
                     e.printStackTrace();
     	            }
             }
             return true;
         }
       public  boolean updateList( SparseArray<Biao> list) {
              if ( null == list || list.size() <= 0) {
                  return false;
              }
                       SQLiteDatabase db = mOpenHelper.getWritableDatabase();
                      try {
   	            String sql ="update Biao set id=?,name=?,code=?,gantaid=?,taiquid=?,yunxing=?,zuobiao=?,level=?,createtime=?,updatetime=?,lifeStatus=?,upgradeFlag=?,areaname=?,danwei=? where id=?";
   	            SQLiteStatement stat = db.compileStatement(sql);
   	            db.beginTransaction();
   	             for (int i=0;i<list.size();i++)  {
   Biao entity = list.get(i);  
  if(null==entity.id)    stat.bindNull(1); else
   stat.bindLong(1,entity.id);
  if(null==entity.name||entity.name.length()==0)    stat.bindNull(2);else
   stat.bindString(2,entity.name);
  if(null==entity.code||entity.code.length()==0)    stat.bindNull(3);else
   stat.bindString(3,entity.code);
  if(null==entity.gantaid)    stat.bindNull(4); else
   stat.bindLong(4,entity.gantaid);
  if(null==entity.taiquid)    stat.bindNull(5); else
   stat.bindLong(5,entity.taiquid);
  if(null==entity.yunxing||entity.yunxing.length()==0)    stat.bindNull(6);else
   stat.bindString(6,entity.yunxing);
  if(null==entity.zuobiao||entity.zuobiao.length()==0)    stat.bindNull(7);else
   stat.bindString(7,entity.zuobiao);
  if(null==entity.level)    stat.bindNull(8); else
   stat.bindLong(8,entity.level);
   stat.bindString(9,dfu.format(new Date()));
   stat.bindString(10,dfu.format(new Date()));
  if(null==entity.lifeStatus)    stat.bindLong(11,1);else
   stat.bindLong(11,entity.lifeStatus);
  if(null==entity.upgradeFlag)    stat.bindLong(12,1);else
   stat.bindLong(12,entity.upgradeFlag);
  if(null==entity.areaname||entity.areaname.length()==0)    stat.bindNull(13);else
   stat.bindString(13,entity.areaname);
  if(null==entity.danwei||entity.danwei.length()==0)    stat.bindNull(14);else
   stat.bindString(14,entity.danwei);
   stat.bindLong(15,entity.id);
  		              long result = stat.executeUpdateDelete();
  		                if (result < 0) {
  		                    return false;
  		                }
  		            }
  		            db.setTransactionSuccessful();
  		        } catch (Exception e) {
  		            e.printStackTrace();
             return false;
         } finally {
             try {
                     if (null != db) {
                         db.endTransaction();
                     }
                 } catch (Exception e) {
                     e.printStackTrace();
     	            }
             }
             return true;
         }
     public SparseArray<Biao> queryBySql(String sql, String []whereArgs){
     Cursor cursor=null;
     int index=0;
     try{
       synchronized(SYNC){
      if ( (cursor =  mOpenHelper.getReadableDatabase().rawQuery(sql, whereArgs))==null || cursor.getCount()<1)return null;
     SparseArray<Biao> list = new SparseArray<Biao>(cursor.getCount());
      while (cursor.moveToNext()){
      Biao entity=new Biao(); 
    entity.id=cursor.isNull(cOLUMNINDEXS.id )? -1 :cursor.getInt(cOLUMNINDEXS.id);
    entity.name=cursor.isNull(cOLUMNINDEXS.name )? "" :cursor.getString(cOLUMNINDEXS.name);
    entity.code=cursor.isNull(cOLUMNINDEXS.code )? "" :cursor.getString(cOLUMNINDEXS.code);
    entity.gantaid=cursor.isNull(cOLUMNINDEXS.gantaid )? -1 :cursor.getInt(cOLUMNINDEXS.gantaid);
    entity.taiquid=cursor.isNull(cOLUMNINDEXS.taiquid )? -1 :cursor.getInt(cOLUMNINDEXS.taiquid);
    entity.yunxing=cursor.isNull(cOLUMNINDEXS.yunxing )? "" :cursor.getString(cOLUMNINDEXS.yunxing);
    entity.zuobiao=cursor.isNull(cOLUMNINDEXS.zuobiao )? "" :cursor.getString(cOLUMNINDEXS.zuobiao);
    entity.level=cursor.isNull(cOLUMNINDEXS.level )? -1 :cursor.getInt(cOLUMNINDEXS.level);
    entity.lifeStatus=cursor.isNull(cOLUMNINDEXS.lifeStatus )? -1 :cursor.getInt(cOLUMNINDEXS.lifeStatus);
    entity.upgradeFlag=cursor.isNull(cOLUMNINDEXS.upgradeFlag )?0 :cursor.getLong(cOLUMNINDEXS.upgradeFlag);
    entity.areaname=cursor.isNull(cOLUMNINDEXS.areaname )? "" :cursor.getString(cOLUMNINDEXS.areaname);
    entity.danwei=cursor.isNull(cOLUMNINDEXS.danwei )? "" :cursor.getString(cOLUMNINDEXS.danwei);
       list.append(index++,entity);
       }
      cursor.close(); 
      return list; 
      }
      }catch(Exception ex){ 
  ex.printStackTrace();       }finally{ 
 if (cursor!= null) cursor.close();
    }   return null;
 }
     public SparseArray<Biao> queryToList(String whereClause, String []whereArgs){
     Cursor cursor=null;
     int index=0;
     try{
       synchronized(SYNC){
      if ( (cursor = query(whereClause, whereArgs) )==null || cursor.getCount()<1)return null;
     SparseArray<Biao> list = new SparseArray<Biao>(cursor.getCount());
      while (cursor.moveToNext()){
      Biao entity=new Biao(); 
    entity.id=cursor.isNull(cOLUMNINDEXS.id )? -1 :cursor.getInt(cOLUMNINDEXS.id);
    entity.name=cursor.isNull(cOLUMNINDEXS.name )? "" :cursor.getString(cOLUMNINDEXS.name);
    entity.code=cursor.isNull(cOLUMNINDEXS.code )? "" :cursor.getString(cOLUMNINDEXS.code);
    entity.gantaid=cursor.isNull(cOLUMNINDEXS.gantaid )? -1 :cursor.getInt(cOLUMNINDEXS.gantaid);
    entity.taiquid=cursor.isNull(cOLUMNINDEXS.taiquid )? -1 :cursor.getInt(cOLUMNINDEXS.taiquid);
    entity.yunxing=cursor.isNull(cOLUMNINDEXS.yunxing )? "" :cursor.getString(cOLUMNINDEXS.yunxing);
    entity.zuobiao=cursor.isNull(cOLUMNINDEXS.zuobiao )? "" :cursor.getString(cOLUMNINDEXS.zuobiao);
    entity.level=cursor.isNull(cOLUMNINDEXS.level )? -1 :cursor.getInt(cOLUMNINDEXS.level);
    entity.lifeStatus=cursor.isNull(cOLUMNINDEXS.lifeStatus )? -1 :cursor.getInt(cOLUMNINDEXS.lifeStatus);
    entity.upgradeFlag=cursor.isNull(cOLUMNINDEXS.upgradeFlag )?0 :cursor.getLong(cOLUMNINDEXS.upgradeFlag);
    entity.areaname=cursor.isNull(cOLUMNINDEXS.areaname )? "" :cursor.getString(cOLUMNINDEXS.areaname);
    entity.danwei=cursor.isNull(cOLUMNINDEXS.danwei )? "" :cursor.getString(cOLUMNINDEXS.danwei);
       list.append(index++,entity);
       }
      cursor.close(); 
      return list; 
      }
      }catch(Exception ex){ 
  ex.printStackTrace();       }finally{ 
 if (cursor!= null) cursor.close();
    }   return null;
 }       public boolean update(Biao entity){
         lock.lock();
         SQLiteDatabase db=mOpenHelper.getWritableDatabase();
         try{
          return update0(db, entity, cOLUMNS.id+"=?", new String[]{String.valueOf(entity.id)} );
         }catch (Exception e) { e.printStackTrace();
} finally {
         lock.unlock();
         }
                      return false;
         }
          public boolean clearData(SQLiteDatabase db) {
                  try {
                      db.execSQL("delete from Biao");
                      return true;
                  } catch (Exception e) {
                      e.printStackTrace();
                      return false;
                  }
              }
        public boolean delete(Biao entity){
         SQLiteDatabase db=null;
          try{
          return delete0(db=mOpenHelper.getWritableDatabase(), cOLUMNS.id+"=?", new String[]{String.valueOf(entity.id)} );
          }finally{
          }
        }
		public Long getUpgrade( SQLiteDatabase db ){
	    Long strid = 0l;
	    Cursor cursor =  mOpenHelper.getReadableDatabase().rawQuery("select last_insert_rowid() from Biao", null);
	    if (cursor.moveToFirst())
	        strid = cursor.getLong(0);
	    cursor.close();
	    return strid+1;
	}
   public final class COLUMNINDEXS{
    public final int id=0;
    public final int name=1;
    public final int code=2;
    public final int gantaid=3;
    public final int taiquid=4;
    public final int yunxing=5;
    public final int zuobiao=6;
    public final int level=7;
    public final int createtime=8;
    public final int updatetime=9;
    public final int lifeStatus=10;
    public final int upgradeFlag=11;
    public final int areaname=12;
    public final int danwei=13;
   }
   public final class COLUMNS{
    public final String id="[id]";
    public final String name="[name]";
    public final String code="[code]";
    public final String gantaid="[gantaid]";
    public final String taiquid="[taiquid]";
    public final String yunxing="[yunxing]";
    public final String zuobiao="[zuobiao]";
    public final String level="[level]";
    public final String createtime="[createtime]";
    public final String updatetime="[updatetime]";
    public final String lifeStatus="[lifeStatus]";
    public final String upgradeFlag="[upgradeFlag]";
    public final String areaname="[areaname]";
    public final String danwei="[danwei]";
   }
       private boolean update0(SQLiteDatabase db, Biao entity, String whereClause, String []whereArgs){ 
       ContentValues cv=new ContentValues(1); 
    cv.put(cOLUMNS.id, entity.id );
    cv.put(cOLUMNS.name, entity.name );
    cv.put(cOLUMNS.code, entity.code );
    cv.put(cOLUMNS.gantaid, entity.gantaid );
    cv.put(cOLUMNS.taiquid, entity.taiquid );
    cv.put(cOLUMNS.yunxing, entity.yunxing );
    cv.put(cOLUMNS.zuobiao, entity.zuobiao );
    cv.put(cOLUMNS.level, entity.level );
    cv.put(cOLUMNS.updatetime, dfu.format(new Date()));
    cv.put(cOLUMNS.lifeStatus, entity.lifeStatus );
    cv.put(cOLUMNS.upgradeFlag, entity.upgradeFlag );
    cv.put(cOLUMNS.areaname, entity.areaname );
    cv.put(cOLUMNS.danwei, entity.danwei );
        return db.update(TABLENAME, cv, whereClause, whereArgs) >0; 
     }
 private boolean delete0(SQLiteDatabase db, String whereClause, String []whereArgs){
        return db.delete(TABLENAME, whereClause, whereArgs) >0; 
     }
 public boolean drop(SQLiteDatabase db) {
        try { 
     db.execSQL("drop table if exists Biao");
     return true;
     } catch (Exception e) {
     e.printStackTrace();
    return false;
     }
     }
         }
