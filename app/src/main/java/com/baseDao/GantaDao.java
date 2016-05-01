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
import java.text.ParseException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class GantaDao{
Lock lock = new ReentrantLock();
    SimpleDateFormat dfu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 public static final String TABLENAME ="Ganta";
  COLUMNINDEXS cOLUMNINDEXS=new COLUMNINDEXS();
  COLUMNS cOLUMNS=new COLUMNS();
 public final Object SYNC= new Object();
 private final SQLiteOpenHelper mOpenHelper;
 public GantaDao(SQLiteOpenHelper openHelper){
   mOpenHelper=openHelper;
          mOpenHelper.getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS [Ganta] (  [id] INTEGER PRIMARY KEY AUTOINCREMENT,   [name] NVARCHAR2(50),   [areaid] INTEGER,   [dianya] NVARCHAR2(50),   [caizhi] NVARCHAR2(11),   [xingzhi] NVARCHAR2(11),   [taiquid] INT,   [huilu] INT,   [yunxing] NVARCHAR2(11),   [zuobiao] NVARCHAR2(50),   [level] INT,   [parentid] INTEGER,   [picquanmao] VARCHAR2(100),   [pictatou] NVARCHAR2(100),   [picmingpai] NVARCHAR2(100),   [createtime] DATETIME NOT NULL DEFAULT (datetime('now')),   [updatetime] DATETIME NOT NULL,   [LifeStatus] INTEGER NOT NULL,   [upgradeFlag] BIGINT NOT NULL,   [areaname] NVARCHAR2(100),   [danwei] NVARCHAR2(50))"); 
      }
   public Cursor query(String whereClause, String []whereArgs){
     final String sql = "SELECT *"
     + " FROM " + (TextUtils.isEmpty(whereClause)? TABLENAME : TABLENAME+" WHERE "+whereClause);
     return mOpenHelper.getReadableDatabase().rawQuery(sql, whereArgs);
     }


       public  boolean insertList( SparseArray<Ganta> list) {
              if ( null == list || list.size() <= 0) {
                  return false;
              }
                       SQLiteDatabase db = mOpenHelper.getWritableDatabase();
                      try {
   	            String sql ="insert into Ganta(" 
   + cOLUMNS.id+","   + cOLUMNS.name+","   + cOLUMNS.areaid+","   + cOLUMNS.dianya+","   + cOLUMNS.caizhi+","   + cOLUMNS.xingzhi+","   + cOLUMNS.taiquid+","   + cOLUMNS.huilu+","   + cOLUMNS.yunxing+","   + cOLUMNS.zuobiao+","   + cOLUMNS.level+","   + cOLUMNS.parentid+","   + cOLUMNS.picquanmao+","   + cOLUMNS.pictatou+","   + cOLUMNS.picmingpai+","   + cOLUMNS.createtime+","   + cOLUMNS.updatetime+","   + cOLUMNS.lifeStatus+","   + cOLUMNS.upgradeFlag+","   + cOLUMNS.areaname+","   + cOLUMNS.danwei        + ") " + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
   	            SQLiteStatement stat = db.compileStatement(sql);
   	            db.beginTransaction();
   	             for (int i=0;i<list.size();i++)  {
   Ganta entity = list.get(i);  
  if(null==entity.id)    stat.bindNull(1); else
   stat.bindLong(1,entity.id);
  if(null==entity.name||entity.name.length()==0)    stat.bindNull(2);else
   stat.bindString(2,entity.name);
  if(null==entity.areaid)    stat.bindNull(3); else
   stat.bindLong(3,entity.areaid);
  if(null==entity.dianya||entity.dianya.length()==0)    stat.bindNull(4);else
   stat.bindString(4,entity.dianya);
  if(null==entity.caizhi||entity.caizhi.length()==0)    stat.bindNull(5);else
   stat.bindString(5,entity.caizhi);
  if(null==entity.xingzhi||entity.xingzhi.length()==0)    stat.bindNull(6);else
   stat.bindString(6,entity.xingzhi);
  if(null==entity.taiquid)    stat.bindNull(7); else
   stat.bindLong(7,entity.taiquid);
  if(null==entity.huilu)    stat.bindNull(8); else
   stat.bindLong(8,entity.huilu);
  if(null==entity.yunxing||entity.yunxing.length()==0)    stat.bindNull(9);else
   stat.bindString(9,entity.yunxing);
  if(null==entity.zuobiao||entity.zuobiao.length()==0)    stat.bindNull(10);else
   stat.bindString(10,entity.zuobiao);
  if(null==entity.level)    stat.bindNull(11); else
   stat.bindLong(11,entity.level);
  if(null==entity.parentid)    stat.bindNull(12); else
   stat.bindLong(12,entity.parentid);
  if(null==entity.picquanmao||entity.picquanmao.length()==0)    stat.bindNull(13);else
   stat.bindString(13,entity.picquanmao);
  if(null==entity.pictatou||entity.pictatou.length()==0)    stat.bindNull(14);else
   stat.bindString(14,entity.pictatou);
  if(null==entity.picmingpai||entity.picmingpai.length()==0)    stat.bindNull(15);else
   stat.bindString(15,entity.picmingpai);
   stat.bindString(16,dfu.format(new Date()));
   stat.bindString(17,dfu.format(new Date()));
  if(null==entity.lifeStatus)    stat.bindLong(18,1);else
   stat.bindLong(18,entity.lifeStatus);
  if(null==entity.upgradeFlag)    stat.bindLong(19,1);else
   stat.bindLong(19,entity.upgradeFlag);
  if(null==entity.areaname||entity.areaname.length()==0)    stat.bindNull(20);else
   stat.bindString(20,entity.areaname);
  if(null==entity.danwei||entity.danwei.length()==0)    stat.bindNull(21);else
   stat.bindString(21,entity.danwei);
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
       public  boolean updateList( SparseArray<Ganta> list) {
              if ( null == list || list.size() <= 0) {
                  return false;
              }
                       SQLiteDatabase db = mOpenHelper.getWritableDatabase();
                      try {
   	            String sql ="update Ganta set id=?,name=?,areaid=?,dianya=?,caizhi=?,xingzhi=?,taiquid=?,huilu=?,yunxing=?,zuobiao=?,level=?,parentid=?,picquanmao=?,pictatou=?,picmingpai=?,createtime=?,updatetime=?,lifeStatus=?,upgradeFlag=?,areaname=?,danwei=? where id=?";
   	            SQLiteStatement stat = db.compileStatement(sql);
   	            db.beginTransaction();
   	             for (int i=0;i<list.size();i++)  {
   Ganta entity = list.get(i);  
  if(null==entity.id)    stat.bindNull(1); else
   stat.bindLong(1,entity.id);
  if(null==entity.name||entity.name.length()==0)    stat.bindNull(2);else
   stat.bindString(2,entity.name);
  if(null==entity.areaid)    stat.bindNull(3); else
   stat.bindLong(3,entity.areaid);
  if(null==entity.dianya||entity.dianya.length()==0)    stat.bindNull(4);else
   stat.bindString(4,entity.dianya);
  if(null==entity.caizhi||entity.caizhi.length()==0)    stat.bindNull(5);else
   stat.bindString(5,entity.caizhi);
  if(null==entity.xingzhi||entity.xingzhi.length()==0)    stat.bindNull(6);else
   stat.bindString(6,entity.xingzhi);
  if(null==entity.taiquid)    stat.bindNull(7); else
   stat.bindLong(7,entity.taiquid);
  if(null==entity.huilu)    stat.bindNull(8); else
   stat.bindLong(8,entity.huilu);
  if(null==entity.yunxing||entity.yunxing.length()==0)    stat.bindNull(9);else
   stat.bindString(9,entity.yunxing);
  if(null==entity.zuobiao||entity.zuobiao.length()==0)    stat.bindNull(10);else
   stat.bindString(10,entity.zuobiao);
  if(null==entity.level)    stat.bindNull(11); else
   stat.bindLong(11,entity.level);
  if(null==entity.parentid)    stat.bindNull(12); else
   stat.bindLong(12,entity.parentid);
  if(null==entity.picquanmao||entity.picquanmao.length()==0)    stat.bindNull(13);else
   stat.bindString(13,entity.picquanmao);
  if(null==entity.pictatou||entity.pictatou.length()==0)    stat.bindNull(14);else
   stat.bindString(14,entity.pictatou);
  if(null==entity.picmingpai||entity.picmingpai.length()==0)    stat.bindNull(15);else
   stat.bindString(15,entity.picmingpai);
   stat.bindString(16,dfu.format(new Date()));
   stat.bindString(17,dfu.format(new Date()));
  if(null==entity.lifeStatus)    stat.bindLong(18,1);else
   stat.bindLong(18,entity.lifeStatus);
  if(null==entity.upgradeFlag)    stat.bindLong(19,1);else
   stat.bindLong(19,entity.upgradeFlag);
  if(null==entity.areaname||entity.areaname.length()==0)    stat.bindNull(20);else
   stat.bindString(20,entity.areaname);
  if(null==entity.danwei||entity.danwei.length()==0)    stat.bindNull(21);else
   stat.bindString(21,entity.danwei);
   stat.bindLong(22,entity.id);
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
     public SparseArray<Ganta> queryBySql(String sql, String []whereArgs){
     Cursor cursor=null;
     int index=0;
     try{
       synchronized(SYNC){
      if ( (cursor =  mOpenHelper.getReadableDatabase().rawQuery(sql, whereArgs))==null || cursor.getCount()<1)return null;
     SparseArray<Ganta> list = new SparseArray<Ganta>(cursor.getCount());
      while (cursor.moveToNext()){
      Ganta entity=new Ganta(); 
    entity.id=cursor.isNull(cOLUMNINDEXS.id )? -1 :cursor.getInt(cOLUMNINDEXS.id);
    entity.name=cursor.isNull(cOLUMNINDEXS.name )? "" :cursor.getString(cOLUMNINDEXS.name);
    entity.areaid=cursor.isNull(cOLUMNINDEXS.areaid )? -1 :cursor.getInt(cOLUMNINDEXS.areaid);
    entity.dianya=cursor.isNull(cOLUMNINDEXS.dianya )? "" :cursor.getString(cOLUMNINDEXS.dianya);
    entity.caizhi=cursor.isNull(cOLUMNINDEXS.caizhi )? "" :cursor.getString(cOLUMNINDEXS.caizhi);
    entity.xingzhi=cursor.isNull(cOLUMNINDEXS.xingzhi )? "" :cursor.getString(cOLUMNINDEXS.xingzhi);
    entity.taiquid=cursor.isNull(cOLUMNINDEXS.taiquid )? -1 :cursor.getInt(cOLUMNINDEXS.taiquid);
    entity.huilu=cursor.isNull(cOLUMNINDEXS.huilu )? -1 :cursor.getInt(cOLUMNINDEXS.huilu);
    entity.yunxing=cursor.isNull(cOLUMNINDEXS.yunxing )? "" :cursor.getString(cOLUMNINDEXS.yunxing);
    entity.zuobiao=cursor.isNull(cOLUMNINDEXS.zuobiao )? "" :cursor.getString(cOLUMNINDEXS.zuobiao);
    entity.level=cursor.isNull(cOLUMNINDEXS.level )? -1 :cursor.getInt(cOLUMNINDEXS.level);
    entity.parentid=cursor.isNull(cOLUMNINDEXS.parentid )? -1 :cursor.getInt(cOLUMNINDEXS.parentid);
    entity.picquanmao=cursor.isNull(cOLUMNINDEXS.picquanmao )? "" :cursor.getString(cOLUMNINDEXS.picquanmao);
    entity.pictatou=cursor.isNull(cOLUMNINDEXS.pictatou )? "" :cursor.getString(cOLUMNINDEXS.pictatou);
    entity.picmingpai=cursor.isNull(cOLUMNINDEXS.picmingpai )? "" :cursor.getString(cOLUMNINDEXS.picmingpai);
    	Date datecreatetime  = null;   	try {   	if(!cursor.isNull(cOLUMNINDEXS.createtime )){   		datecreatetime = (Date) dfu.parse(cursor.getString(cOLUMNINDEXS.createtime));   			}   			} catch (ParseException e) {   			e.printStackTrace();   			}   	entity.createtime=datecreatetime;    	Date dateupdatetime  = null;   	try {   	if(!cursor.isNull(cOLUMNINDEXS.updatetime )){   		dateupdatetime = (Date) dfu.parse(cursor.getString(cOLUMNINDEXS.updatetime));   			}   			} catch (ParseException e) {   			e.printStackTrace();   			}   	entity.updatetime=dateupdatetime;    entity.lifeStatus=cursor.isNull(cOLUMNINDEXS.lifeStatus )? -1 :cursor.getInt(cOLUMNINDEXS.lifeStatus);
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
     public SparseArray<Ganta> queryToList(String whereClause, String []whereArgs){
     Cursor cursor=null;
     int index=0;
     try{
       synchronized(SYNC){
      if ( (cursor = query(whereClause, whereArgs) )==null || cursor.getCount()<1)return null;
     SparseArray<Ganta> list = new SparseArray<Ganta>(cursor.getCount());
      while (cursor.moveToNext()){
      Ganta entity=new Ganta(); 
    entity.id=cursor.isNull(cOLUMNINDEXS.id )? -1 :cursor.getInt(cOLUMNINDEXS.id);
    entity.name=cursor.isNull(cOLUMNINDEXS.name )? "" :cursor.getString(cOLUMNINDEXS.name);
    entity.areaid=cursor.isNull(cOLUMNINDEXS.areaid )? -1 :cursor.getInt(cOLUMNINDEXS.areaid);
    entity.dianya=cursor.isNull(cOLUMNINDEXS.dianya )? "" :cursor.getString(cOLUMNINDEXS.dianya);
    entity.caizhi=cursor.isNull(cOLUMNINDEXS.caizhi )? "" :cursor.getString(cOLUMNINDEXS.caizhi);
    entity.xingzhi=cursor.isNull(cOLUMNINDEXS.xingzhi )? "" :cursor.getString(cOLUMNINDEXS.xingzhi);
    entity.taiquid=cursor.isNull(cOLUMNINDEXS.taiquid )? -1 :cursor.getInt(cOLUMNINDEXS.taiquid);
    entity.huilu=cursor.isNull(cOLUMNINDEXS.huilu )? -1 :cursor.getInt(cOLUMNINDEXS.huilu);
    entity.yunxing=cursor.isNull(cOLUMNINDEXS.yunxing )? "" :cursor.getString(cOLUMNINDEXS.yunxing);
    entity.zuobiao=cursor.isNull(cOLUMNINDEXS.zuobiao )? "" :cursor.getString(cOLUMNINDEXS.zuobiao);
    entity.level=cursor.isNull(cOLUMNINDEXS.level )? -1 :cursor.getInt(cOLUMNINDEXS.level);
    entity.parentid=cursor.isNull(cOLUMNINDEXS.parentid )? -1 :cursor.getInt(cOLUMNINDEXS.parentid);
    entity.picquanmao=cursor.isNull(cOLUMNINDEXS.picquanmao )? "" :cursor.getString(cOLUMNINDEXS.picquanmao);
    entity.pictatou=cursor.isNull(cOLUMNINDEXS.pictatou )? "" :cursor.getString(cOLUMNINDEXS.pictatou);
    entity.picmingpai=cursor.isNull(cOLUMNINDEXS.picmingpai )? "" :cursor.getString(cOLUMNINDEXS.picmingpai);
    	Date datecreatetime  = null;   	try {   	if(!cursor.isNull(cOLUMNINDEXS.createtime )){   		datecreatetime = (Date) dfu.parse(cursor.getString(cOLUMNINDEXS.createtime));   			}   			} catch (ParseException e) {   			e.printStackTrace();   			}   	entity.createtime=datecreatetime;    	Date dateupdatetime  = null;   	try {   	if(!cursor.isNull(cOLUMNINDEXS.updatetime )){   		dateupdatetime = (Date) dfu.parse(cursor.getString(cOLUMNINDEXS.updatetime));   			}   			} catch (ParseException e) {   			e.printStackTrace();   			}   	entity.updatetime=dateupdatetime;    entity.lifeStatus=cursor.isNull(cOLUMNINDEXS.lifeStatus )? -1 :cursor.getInt(cOLUMNINDEXS.lifeStatus);
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
 }       public boolean update(Ganta entity){
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
                      db.execSQL("delete from Ganta");
                      return true;
                  } catch (Exception e) {
                      e.printStackTrace();
                      return false;
                  }
              }
        public boolean delete(Ganta entity){
         SQLiteDatabase db=null;
          try{
          return delete0(db=mOpenHelper.getWritableDatabase(), cOLUMNS.id+"=?", new String[]{String.valueOf(entity.id)} );
          }finally{
          }
        }
		public Long getUpgrade( SQLiteDatabase db ){
	    Long strid = 0l;
	    Cursor cursor =  mOpenHelper.getReadableDatabase().rawQuery("select last_insert_rowid() from Ganta", null);
	    if (cursor.moveToFirst())
	        strid = cursor.getLong(0);
	    cursor.close();
	    return strid+1;
	}
   public final class COLUMNINDEXS{
    public final int id=0;
    public final int name=1;
    public final int areaid=2;
    public final int dianya=3;
    public final int caizhi=4;
    public final int xingzhi=5;
    public final int taiquid=6;
    public final int huilu=7;
    public final int yunxing=8;
    public final int zuobiao=9;
    public final int level=10;
    public final int parentid=11;
    public final int picquanmao=12;
    public final int pictatou=13;
    public final int picmingpai=14;
    public final int createtime=15;
    public final int updatetime=16;
    public final int lifeStatus=17;
    public final int upgradeFlag=18;
    public final int areaname=19;
    public final int danwei=20;
   }
   public final class COLUMNS{
    public final String id="[id]";
    public final String name="[name]";
    public final String areaid="[areaid]";
    public final String dianya="[dianya]";
    public final String caizhi="[caizhi]";
    public final String xingzhi="[xingzhi]";
    public final String taiquid="[taiquid]";
    public final String huilu="[huilu]";
    public final String yunxing="[yunxing]";
    public final String zuobiao="[zuobiao]";
    public final String level="[level]";
    public final String parentid="[parentid]";
    public final String picquanmao="[picquanmao]";
    public final String pictatou="[pictatou]";
    public final String picmingpai="[picmingpai]";
    public final String createtime="[createtime]";
    public final String updatetime="[updatetime]";
    public final String lifeStatus="[lifeStatus]";
    public final String upgradeFlag="[upgradeFlag]";
    public final String areaname="[areaname]";
    public final String danwei="[danwei]";
   }
       private boolean update0(SQLiteDatabase db, Ganta entity, String whereClause, String []whereArgs){ 
       ContentValues cv=new ContentValues(1); 
    cv.put(cOLUMNS.id, entity.id );
    cv.put(cOLUMNS.name, entity.name );
    cv.put(cOLUMNS.areaid, entity.areaid );
    cv.put(cOLUMNS.dianya, entity.dianya );
    cv.put(cOLUMNS.caizhi, entity.caizhi );
    cv.put(cOLUMNS.xingzhi, entity.xingzhi );
    cv.put(cOLUMNS.taiquid, entity.taiquid );
    cv.put(cOLUMNS.huilu, entity.huilu );
    cv.put(cOLUMNS.yunxing, entity.yunxing );
    cv.put(cOLUMNS.zuobiao, entity.zuobiao );
    cv.put(cOLUMNS.level, entity.level );
    cv.put(cOLUMNS.parentid, entity.parentid );
    cv.put(cOLUMNS.picquanmao, entity.picquanmao );
    cv.put(cOLUMNS.pictatou, entity.pictatou );
    cv.put(cOLUMNS.picmingpai, entity.picmingpai );
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
     db.execSQL("drop table if exists Ganta");
     return true;
     } catch (Exception e) {
     e.printStackTrace();
    return false;
     }
     }
         }
